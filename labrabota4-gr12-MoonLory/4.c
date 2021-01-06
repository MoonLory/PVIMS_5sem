#define FUSE_USE_VERSION 30
#define _FILE_OFFSET_BITS 64

#include <fuse.h>
#include <unistd.h>
#include <sys/types.h>
#include <stdio.h>
#include <string.h>
#include <errno.h>
#include <fcntl.h>

const int N=13;

char file_system_path[256] = "";
char* example_txt = "Hello world! Student Grishkin Andrei,1823113";
char test_txt[13][50];
char* readme_txt = "Student Grishkin Andrei,1823113";

static int get_attribute(const char* path, struct stat *st){
	st->st_uid = getuid();
	memset(st,0,sizeof(struct stat));
	if(strcmp(path,"/") == 0){
		st->st_mode = S_IFDIR | 0755;
	}else if(strcmp(path,"/bin") == 0){
		st->st_mode = S_IFDIR | 022;
	}else if(strcmp(path,"/bar") == 0){
		st->st_mode = S_IFDIR | 555;
	}else if(strcmp(path, "/baz") == 0){
		st->st_mode = S_IFDIR | 744;
	}else if(strcmp(path, "/baz/readme.txt") == 0){
		st->st_mode = S_IFREG | 644;
		st->st_size = strlen(readme_txt);
	}else if(strcmp(path, "/baz/example") == 0){
		st->st_mode = S_IFREG | 222;
		st->st_size = strlen(example_txt);
	}else if(strcmp(path,"/foo") == 0){
		st->st_mode = S_IFDIR | 711;
	}else if(strcmp(path,"/foo/pwd") == 0){
		st->st_mode = S_IFREG | 777;
		struct stat buffer;
		stat("/usr/bin/pwd",&buffer);
		st->st_size = buffer.st_size;
	}else if(strcmp(path,"/foo/test.txt") == 0){
		st->st_mode = S_IFREG | 000;
		int size = 0;
		for(int i = 0;i<N;i++){
			size+=strlen(test_txt[i])+1;
		}
		st->st_size = size;
	}else{
		return -ENOENT;
	}
	return 0;
}

static int read_directory(const char* path, void* buf, fuse_fill_dir_t filler, off_t offset, struct fuse_file_info * fi){
	filler(buf,".",NULL,0);
	filler(buf,"..",NULL,0);
	if(strcmp(path,"/") == 0){
		filler(buf,"bin",NULL,0);
		filler(buf,"bar",NULL,0);
		filler(buf,"baz",NULL,0);
		filler(buf,"foo",NULL,0);
		if(strcmp(file_system_path,"/mnt/fuse/")!=0){
			filler(buf,file_system_path+1,NULL,0);
		}
		return 0;
	}else if(strcmp(path,"/bin") == 0){
		return 0;
	}else if(strcmp(path,"/bar") == 0){		
		return 0;
	}else if(strcmp(path,"/baz") == 0){
		filler(buf,"example",NULL,0);
		filler(buf,"readme.txt",NULL,0);
		return 0;
	}else if (strcmp(path,"/foo") == 0){
		filler(buf,"pwd",NULL,0);
		filler(buf,"test.txt",NULL,0);
		return 0;
	}
	return -ENOENT;
}

static int read_file(const char* path, char* buf, size_t size,off_t offset, struct fuse_file_info* fi){
	size_t length;
	char* read_data;

	if(strcmp(path,"/baz/readme.txt") == 0){
		length = strlen(readme_txt);
		read_data = readme_txt;
	}else if(strcmp(path,"/baz/example") == 0){
		length = strlen(example_txt);
		read_data = example_txt;
	}else if(strcmp(path,"/foo/pwd") == 0){
		struct stat touch_stat;
		stat("/usr/bin/pwd",&touch_stat);
		length = touch_stat.st_size;
		
		FILE* f = fopen("/usr/bin/pwd","rb");
		unsigned char buffer[length];
		fread(buffer,length,1,f);
		read_data = buffer;
		fclose(f);
	}else if(strcmp(path,"/foo/test.txt") == 0){
		char temp[N*50+400];
		strcpy(temp,"");
		for(int i = 0;i<N;i++){
			strcat(temp,test_txt[i]);
			strcat(temp,"\n");
		}
		length = strlen(temp);
		read_data = temp;
	}else{
		return -ENOENT;
	}

	if(offset<length){
		if(offset+size>length){
			size = length-offset;
		}
		memcpy(buf, read_data+offset,size);
		return size;
	}

	return 0;
}

int main(int argc, char** argv){
	strcpy(file_system_path,argv[1]);
	char number_line[8];
	for(int i = 0;i<N;i++){
		sprintf(number_line,"%d: ",i+1);
		strcat(test_txt[i],"I LOVE PVIMS (TRUE INFORMATION)");
	}

	 struct fuse_operations operations = {
		.getattr = get_attribute,
		.readdir = read_directory,
		.read = read_file,
	};
	return fuse_main(argc,argv,&operations,NULL);
}
