package co.csedge.cubycare.data.model

data class DevelopmentalMilestone(
    val id: String = "",
    val domain: String = "", // e.g., "Gross Motor", "Fine Motor", "Language/Communication", "Cognitive", "Social/Emotional"
    val title: String = "", // e.g., "Holds head steady", "Rolls over"
    val ageMonths: Double = 0.0,
    val ageRange: String = "", // e.g., "2 Months", "4 Months"
    val isCompleted: Boolean = false,
    val completedDateMillis: Long? = null
)
