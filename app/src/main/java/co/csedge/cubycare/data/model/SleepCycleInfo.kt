package co.csedge.cubycare.data.model

data class SleepCycleInfo(
    val ageRange: String,
    val sleepDuration: String,
    val numberOfNaps: String,
    val nightAwakenings: String,
    val whenDisturbed: String = "",
    val mildRemedies: String = "",
    val severeSymptoms: String = ""
)
