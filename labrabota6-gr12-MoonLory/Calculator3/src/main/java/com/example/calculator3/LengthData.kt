package com.calculator3.calculator3

class LengthData {
    companion object {
        const val OUTPUT_SAVE_ID = "LENGTH_DATA_ID"

        val lengths: HashMap<String, Double> = hashMapOf(
            "METER" to 1.0,
            "CENTIMETER" to 0.01,
            "FEET" to 0.3048,
            "INCH" to 0.0254,
            "KILOMETER" to 1000.0,
            "MILE" to 1609.344,
            "MILLIMETER" to 0.001,
            "YARD" to 0.9144)
    }
}