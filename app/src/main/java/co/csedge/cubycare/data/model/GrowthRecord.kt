package co.csedge.cubycare.data.model

data class GrowthRecord(
    val id: String = "",
    val dateMillis: Long = 0L,
    val weightKg: Double = 0.0,
    val lengthCm: Double = 0.0,
    val headCircumferenceCm: Double = 0.0,
    val isEstimated: Boolean = false,
    val monthIndex: Int? = null
) {
    // Calculates BMI: Weight (kg) / (Height (m))^2
    val bmi: Double
        get() {
            if (lengthCm <= 0.0) return 0.0
            val heightM = lengthCm / 100.0
            return weightKg / (heightM * heightM)
        }
}
