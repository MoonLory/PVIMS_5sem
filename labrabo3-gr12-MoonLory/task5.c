#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <stdbool.h>
#include <stdatomic.h>
#include <pthread.h>
#include <semaphore.h>
#include <dirent.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <unistd.h>

#define BUFFER_SIZE 9964

typedef struct dir_queue
{
	char *dir_path;
	struct dir_queue *next;
}
dir_queue;

dir_queue *dir_queue_head, *dir_queue_tail;
sem_t dir_queue_mutex;

void init_dir_queue()
{
	dir_queue_head = dir_queue_tail = NULL;
	sem_init(&dir_queue_mutex, 0, 1);
}

void enqueue_dir(const char *dir_path)
{
	dir_queue *element = malloc(sizeof(dir_queue));
	element->dir_path = malloc(strlen(dir_path) + 1);
	strcpy(element->dir_path, dir_path);
	element->next = NULL;

	sem_wait(&dir_queue_mutex);
	if (!dir_queue_head)
		dir_queue_head = dir_queue_tail = element;
	else
	{
		dir_queue_tail->next = element;
		dir_queue_tail = element;
	}
	sem_post(&dir_queue_mutex);
}

char * dequeue_dir()
{
	char *result = NULL;

	sem_wait(&dir_queue_mutex);
	if (!dir_queue_head)
	{
		sem_post(&dir_queue_mutex);
		return NULL;
	}	
	dir_queue *temp = dir_queue_head;
	dir_queue_head = dir_queue_head->next;
	if (!dir_queue_head)
		dir_queue_tail = NULL;
	sem_post(&dir_queue_mutex);
	
	result = temp->dir_path;
	free(temp);
	return result;
}

void destroy_dir_queue()
{
	sem_destroy(&dir_queue_mutex);
	for (dir_queue *curr = dir_queue_head; curr;)
	{
		dir_queue *temp = curr;
		curr = curr->next;
		free(temp->dir_path);
		free(temp);
	}
	dir_queue_tail = NULL;
}

sem_t console_mutex, file_mutex;
FILE *output_log_file;

void init_printing(const char *file_name)
{
	output_log_file = fopen(file_name, "w");
	sem_init(&console_mutex, 0, 1);
	sem_init(&file_mutex, 0, 1);
}

void print_to_file(FILE *file, const char *msg, sem_t *mutex)
{
	sem_wait(mutex);
	
	fputs(msg, file);
	fputc('\n', file);

	sem_post(mutex);
}

void print_msg(const char *msg)
{
	print_to_file(output_log_file, msg, &file_mutex);
	print_to_file(stdout, msg, &console_mutex);
}

void destroy_printing()
{
	fclose(output_log_file);
	sem_destroy(&console_mutex);
	sem_destroy(&file_mutex);
}

sem_t dir_thread_semaphore;

void * dir_thread_func(void *dir)
{
	char rel_file_name[2 * BUFFER_SIZE], full_file_name[2 * BUFFER_SIZE], message_buffer[5 * BUFFER_SIZE];

	char *dir_path = dir;
	int dir_path_length = strlen(dir_path);
	strcpy(rel_file_name, dir_path);

	pthread_t thread_id = pthread_self();

	sprintf(message_buffer, "Thread #%u working in the dir %s",
		(unsigned)thread_id, dir_path);
	print_msg(message_buffer);

	DIR *dir_stream = opendir(dir_path);
	if (dir_stream == NULL)
	{
		sprintf(message_buffer, "Thread #%u can't open the dir %s!",
			(unsigned)thread_id, dir_path);
		print_msg(message_buffer);

		free(dir_path);
		sem_post(&dir_thread_semaphore);
		pthread_exit(NULL);
	}
	struct dirent *dir_entry;
	struct stat dir_entry_stat;

	int total_files = 0, total_viewed_files = 0;
	long long total_file_size = 0;
	char largest_file[2 * BUFFER_SIZE];
	off_t largest_file_size = -1;
	
	while (dir_entry = readdir(dir_stream))
	{
		if (!strcmp(dir_entry->d_name, ".") ||
		    !strcmp(dir_entry->d_name, ".."))
			continue;

		++total_files;

		rel_file_name[dir_path_length] = '/';
		rel_file_name[dir_path_length + 1] = '\0';
		strcat(rel_file_name, dir_entry->d_name);

		if (stat(rel_file_name, &dir_entry_stat) < 0)
			continue;
		total_file_size += dir_entry_stat.st_size;
		
		if ((dir_entry_stat.st_mode & S_IFMT) == S_IFLNK)
			continue;	
		else if ((dir_entry_stat.st_mode & S_IFMT) == S_IFDIR)
			enqueue_dir(rel_file_name);	
		else if ((dir_entry_stat.st_mode & S_IFMT) == S_IFREG)
		{
			++total_viewed_files;
			realpath(rel_file_name, full_file_name);

			sprintf(message_buffer, "\tThread #%u viewing file %s, size = %li byte(s)",
				(unsigned)thread_id, full_file_name, dir_entry_stat.st_size);
			print_msg(message_buffer);

			if (dir_entry_stat.st_size > largest_file_size)
			{
				largest_file_size = dir_entry_stat.st_size;
				strcpy(largest_file, full_file_name);
			}	
		}
	}

	sprintf
	(message_buffer, "Thread #%u done working.\nFound %i file(s) in the dir %s, total size = %lli byte(s).\n",
	(unsigned)thread_id, total_files, dir_path, total_file_size);
        if (total_viewed_files == 0)
		sprintf(message_buffer + strlen(message_buffer), "No regular files found.");
	else
	{
		sprintf(message_buffer + strlen(message_buffer), 
			"Found %i regular file(s). The largest file is %s (size = %li byte(s)).",	
		total_viewed_files, largest_file, largest_file_size);
	}

	print_msg(message_buffer);
	
	closedir(dir);
	free(dir_path);
	sem_post(&dir_thread_semaphore);
}

atomic_bool stop_traversal = false;

void * daemon_thread_func(void *number_of_traversers)
{
	int traversers = *((int *)number_of_traversers);
	sem_init(&dir_thread_semaphore, 0, traversers);

	while (!stop_traversal)
	{
		while (dir_queue_head)
		{
			if (stop_traversal)
				break;
			if (sem_trywait(&dir_thread_semaphore) < 0)
				continue;

			char *dir = dequeue_dir();
			pthread_t thread_id;
			pthread_create(&thread_id, NULL, dir_thread_func, dir);
			pthread_detach(thread_id);
		}
	}
	
	int sem_value;
	do
	{
		sem_getvalue(&dir_thread_semaphore, &sem_value);
	}
	while (sem_value < traversers);
	
	sem_destroy(&dir_thread_semaphore);
}

int main(int argc, char **argv)
{
	if (argc != 4)
	{
		puts("Incorrect number of parameters; must be exactly 3: the dir name, the log name, and the number of simultaneous threads");
		return -1;
	}

	int traversers = strtol(argv[3], NULL, 10);
	if (traversers <= 0)
	{
		puts("Incorrect number of threads; must be nonnegative");
		return -2;
	}

	init_dir_queue();
	init_printing(argv[2]);

	enqueue_dir(argv[1]);
	pthread_t daemon_thread_id;
	pthread_create(&daemon_thread_id, NULL, daemon_thread_func, &traversers);

	puts("Press any key to stop traversing...");
	getchar();
	stop_traversal = true;
	pthread_join(daemon_thread_id, NULL);

	destroy_printing();
	destroy_dir_queue();
	return 0;
}
