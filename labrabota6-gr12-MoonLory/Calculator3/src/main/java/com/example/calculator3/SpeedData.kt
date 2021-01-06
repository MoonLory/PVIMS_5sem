package com.calculator3.calculator3

class SpeedData {
    companion object {
        const val OUTPUT_SAVE_ID = "SPEED_DATA_ID"

        val speedes: HashMap<String, Double> = hashMapOf(
            "METER/SECOND" to 1.0,
            "KILOMETER/HOUR" to 0.278,
            "MILE/HOUR" to 0.447,
            "METER/HOUR" to 0.00027,
            "METER/MINUTE" to 0.0166,
            "KILOMETER/MINUTE" to 16.66,
            "KILOMETER/SECOND" to 1000.0)
    }
}