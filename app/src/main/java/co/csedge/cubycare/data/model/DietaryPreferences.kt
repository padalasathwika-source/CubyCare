package co.csedge.cubycare.data.model

data class DietaryPreferences(
    val isVegetarian: Boolean = false,
    val regionalPreference: String = "Any", // e.g. "South Indian", "North Indian"
    val foodAllergies: String = "",
    val pickyEatingNotes: String = ""
)
