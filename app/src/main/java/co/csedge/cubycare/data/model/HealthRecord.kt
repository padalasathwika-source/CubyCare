package co.csedge.cubycare.data.model

data class HealthRecord(
    val id: String = "",
    val dateMillis: Long = System.currentTimeMillis(),
    val temperature: String = "",
    val cough: String = "None",
    val cold: String = "None",
    val vomiting: String = "None",
    val diarrhea: String = "None",
    val appetite: String = "Normal",
    val urineFrequency: String = "Normal",
    val bowelMovements: String = "Normal",
    val rash: String = "None",
    val pain: String = "None",
    val medicationGiven: String = ""
)
