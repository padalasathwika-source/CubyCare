package co.csedge.cubycare.data.model

data class Vaccine(
    val id: String = "",
    val name: String = "",
    val recommendedAge: String = "",
    val recommendedAgeMonths: Int = 0, // Useful for sorting
    val doseNumber: Int = 1,
    val administeredDateMillis: Long? = null,
    val nextDueDateMillis: Long? = null,
    val reason: String = "",
    val type: String = "MANDATORY", // "MANDATORY", "OPTIONAL", "PRIVATE"
    val certificateUrl: String? = null
)
