package com.calculator3.calculator3

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.calculator3.calculator3.LengthData.Companion.lengths
import com.calculator3.calculator3.SpeedData.Companion.speedes
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), View.OnClickListener, AdapterView.OnItemSelectedListener {

    private enum class SavedInstanceMainActivityStateEnum(val value: String) {
        isCurrentValuesIsLengths("IS_CURRENT_VALUES_LENGTHS_MAIN_ACTIVITY"),
        isDoubleMode("IS_DOUBLE_MODE_MAIN_ACTIVITY"),
        inputValueTextView("INPUT_VALUE_TEXT_VIEW_MAIN_ACTIVITY"),
        resultTextView("RESULT_TEXT_VIEW_MAIN_ACTIVITY"),
        currentFromValue("CURRENT_FROM_VALUE_MAIN_ACTIVITY"),
        currentToValue("CURRENT_TO_VALUE_MAIN_ACTIVITY")
    }

    private var defaultLengthValue = "METER"
    private var defaultSpeedsValue = "METER/SECOND"

    private var currentFromValue: String? = "METER"
    private var currentToValue: String? = "METER"
    private var isDoubleMode = false
    private var currentDictToConvert = lengths
    private var isCurrentValuesIsLengths = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState != null) {
            isCurrentValuesIsLengths = savedInstanceState.getBoolean(SavedInstanceMainActivityStateEnum.isCurrentValuesIsLengths.value)
            isDoubleMode = savedInstanceState.getBoolean(SavedInstanceMainActivityStateEnum.isDoubleMode.value)
            inputValueTextView.text = savedInstanceState.getString(SavedInstanceMainActivityStateEnum.inputValueTextView.value)
            resultTextView.text = savedInstanceState.getString(SavedInstanceMainActivityStateEnum.resultTextView.value)
            currentFromValue = savedInstanceState.getString(SavedInstanceMainActivityStateEnum.currentFromValue.value)
            currentToValue = savedInstanceState.getString(SavedInstanceMainActivityStateEnum.currentToValue.value)
        }

        currentDictToConvert = when {
            isCurrentValuesIsLengths -> lengths
            else -> speedes
        }

        addValuesToSpiner()
        addListnerToCalcButtons()
    }

    private fun addValuesToSpiner() {
        val spinnerArrayAdapter = ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, currentDictToConvert.keys.toTypedArray())
        if (isCurrentValuesIsLengths) {
            val indexFrom = currentDictToConvert.keys.toTypedArray().indexOf(if (currentFromValue == null)  defaultLengthValue else currentFromValue)
            val indexTo = currentDictToConvert.keys.toTypedArray().indexOf(if (currentToValue == null)  defaultLengthValue else currentToValue)
            fromSpinner.setSelection(indexFrom)
            toSpinner.setSelection(indexTo)
        } else {
            val indexFrom = currentDictToConvert.keys.toTypedArray().indexOf(if (currentFromValue == null)  defaultSpeedsValue else currentFromValue)
            val indexTo = currentDictToConvert.keys.toTypedArray().indexOf(if (currentToValue == null)  defaultSpeedsValue else currentToValue)
            fromSpinner.setSelection(indexFrom)
            toSpinner.setSelection(indexTo)
        }
        fromSpinner.adapter = spinnerArrayAdapter
        toSpinner.adapter = spinnerArrayAdapter

        fromSpinner.onItemSelectedListener = this
        toSpinner.onItemSelectedListener = this
    }

    private fun addListnerToCalcButtons() {
        calc_button0.setOnClickListener(this)
        calc_button1.setOnClickListener(this)
        calc_button2.setOnClickListener(this)
        calc_button3.setOnClickListener(this)
        calc_button4.setOnClickListener(this)
        calc_button5.setOnClickListener(this)
        calc_button6.setOnClickListener(this)
        calc_button7.setOnClickListener(this)
        calc_button8.setOnClickListener(this)
        calc_button9.setOnClickListener(this)
        calc_button_double.setOnClickListener(this)
        calc_button_erase.setOnClickListener(this)
        swap_convert_values_button.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        var value = ""
        when (v.id) {
            calc_button0.id -> {
                value = "0"
            }
            calc_button1.id -> {
                value = "1"
            }
            calc_button2.id -> {
                value = "2"
            }
            calc_button3.id -> {
                value = "3"
            }
            calc_button4.id -> {
                value = "4"
            }
            calc_button5.id -> {
                value = "5"
            }
            calc_button6.id -> {
                value = "6"
            }
            calc_button7.id -> {
                value = "7"
            }
            calc_button8.id -> {
                value = "8"
            }
            calc_button9.id -> {
                value = "9"
            }
            calc_button_double.id -> {
                value = "."
                if (isDoubleMode) {
                    return
                }
                isDoubleMode = true
            }
            calc_button_erase.id -> {
                inputValueTextView.text = ""
                resultTextView.text = ""
                isDoubleMode = false
                return
            }
            swap_convert_values_button.id -> {
                currentFromValue = null
                currentToValue = null
                isCurrentValuesIsLengths = !isCurrentValuesIsLengths
                currentDictToConvert = when {
                    isCurrentValuesIsLengths -> lengths
                    else -> speedes
                }

                addValuesToSpiner()
                return
            }
        }
        var result = inputValueTextView.text.toString()
        if (value == ".") {
            inputValueTextView.text = (result + resources.getString(R.string.numbers_double_separator))
        } else {
            inputValueTextView.text = (result + value)
        }
        recountValues()
    }

    private fun recountValues() {
        val fromRate = currentDictToConvert[currentFromValue]
        val toRate = currentDictToConvert[currentToValue]
        val inputNumber = inputValueTextView.text.toString().toDoubleOrNull()
        if (inputNumber != null) {
            val output = (inputNumber * fromRate!!) / toRate!!
            resultTextView.text = String.format("%.3f", output)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState?.putBoolean(SavedInstanceMainActivityStateEnum.isCurrentValuesIsLengths.value, isCurrentValuesIsLengths)
        outState?.putBoolean(SavedInstanceMainActivityStateEnum.isDoubleMode.value, isDoubleMode)
        outState?.putString(SavedInstanceMainActivityStateEnum.inputValueTextView.value, inputValueTextView.text.toString())
        outState?.putString(SavedInstanceMainActivityStateEnum.resultTextView.value, resultTextView.text.toString())
        outState?.putString(SavedInstanceMainActivityStateEnum.currentFromValue.value, currentFromValue)
        outState?.putString(SavedInstanceMainActivityStateEnum.currentToValue.value, currentToValue)

    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        currentFromValue = fromSpinner.selectedItem.toString()
        currentToValue = toSpinner.selectedItem.toString()
        recountValues()
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {}

}