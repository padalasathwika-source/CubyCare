package co.csedge.cubycare.data.repository

import co.csedge.cubycare.data.model.CubyJoyActivity
import java.util.UUID

object CubyJoyProvider {

    fun getActivitiesForAge(ageRange: String): List<CubyJoyActivity> {
        val exact = allActivities.filter { it.ageRange.equals(ageRange, ignoreCase = true) }
        if (exact.isNotEmpty()) return exact

        val lower = ageRange.lowercase()
        return when {
            lower.contains("0-6") || lower.contains("6-12") || lower.contains("month") || lower.contains("infant") || lower.contains("baby") -> {
                allActivities.filter { it.ageRange == "6 months" }
            }
            lower.contains("1-2") || lower.contains("2") || lower.contains("toddler") -> {
                allActivities.filter { it.ageRange == "2 years" }
            }
            lower.contains("3") || lower.contains("4") || lower.contains("5") || lower.contains("preschool") -> {
                allActivities.filter { it.ageRange == "4–5 years" }
            }
            else -> {
                allActivities.filter { it.ageRange == "6 months" }
            }
        }
    }

    val ageRanges = listOf(
        "6 months",
        "2 years",
        "4–5 years"
    )

    private val allActivities = listOf(
        // 6 Months
        CubyJoyActivity(
            id = UUID.randomUUID().toString(),
            ageRange = "6 months",
            name = "Tummy time",
            reason = "Strengthens your baby's neck, shoulder, and arm muscles. It promotes motor skills necessary for crawling and rolling over.",
            examples = "Place baby on a colorful play mat. Put a small mirror or bright contrast cards just out of reach."
        ),
        CubyJoyActivity(
            id = UUID.randomUUID().toString(),
            ageRange = "6 months",
            name = "Reaching for toys",
            reason = "Improves hand-eye coordination and fine motor skills. It encourages curiosity and spatial awareness.",
            examples = "Soft plush blocks, sensory rings, crinkle toys, or a dangling activity gym."
        ),
        CubyJoyActivity(
            id = UUID.randomUUID().toString(),
            ageRange = "6 months",
            name = "Talking and singing to the baby",
            reason = "Stimulates auditory development, promotes early language skills, and strengthens the emotional bond between parent and child.",
            examples = "Sing 'Twinkle Twinkle Little Star' or 'The Wheels on the Bus'. Describe what you are doing while changing diapers."
        ),
        CubyJoyActivity(
            id = UUID.randomUUID().toString(),
            ageRange = "6 months",
            name = "Peek-a-boo games",
            reason = "Teaches object permanence (the understanding that things still exist even when hidden) and provides positive social interaction.",
            examples = "Use your hands or a soft blanket to hide your face, then enthusiastically say 'Peek-a-boo!'"
        ),

        // 2 Years
        CubyJoyActivity(
            id = UUID.randomUUID().toString(),
            ageRange = "2 years",
            name = "Stacking blocks",
            reason = "Develops fine motor skills, hand-eye coordination, and spatial understanding. It introduces basic concepts of balance and gravity.",
            examples = "Mega Bloks, large wooden stacking cubes, or even soft sensory nesting blocks."
        ),
        CubyJoyActivity(
            id = UUID.randomUUID().toString(),
            ageRange = "2 years",
            name = "Naming objects",
            reason = "Rapidly expands vocabulary, encourages clear pronunciation, and helps the child categorize the world around them.",
            examples = "Point to body parts ('Where is your nose?'), animal flashcards, or name fruits during grocery shopping."
        ),
        CubyJoyActivity(
            id = UUID.randomUUID().toString(),
            ageRange = "2 years",
            name = "Simple puzzles",
            reason = "Enhances cognitive skills, problem-solving abilities, and shape recognition.",
            examples = "Peg puzzles with 3-5 large pieces (like farm animals or vehicles)."
        ),
        CubyJoyActivity(
            id = UUID.randomUUID().toString(),
            ageRange = "2 years",
            name = "Drawing with crayons",
            reason = "Encourages creative expression and strengthens the small muscles in the hands required for future writing skills.",
            examples = "Large, easy-to-grip jumbo crayons and thick paper. Focus on scribbling rather than coloring inside lines."
        ),

        // 4-5 Years
        CubyJoyActivity(
            id = UUID.randomUUID().toString(),
            ageRange = "4–5 years",
            name = "Storytelling",
            reason = "Boosts imagination, comprehension, and complex language skills. It teaches narrative structure and empathy.",
            examples = "Read picture books like 'The Very Hungry Caterpillar' or ask them to make up a story about a flying dog."
        ),
        CubyJoyActivity(
            id = UUID.randomUUID().toString(),
            ageRange = "4–5 years",
            name = "Drawing",
            reason = "Allows for advanced creative expression, improves fine motor control, and helps children process their emotions visually.",
            examples = "Use washable markers, colored pencils, or finger paints. Ask them to draw their family or a favorite memory."
        ),
        CubyJoyActivity(
            id = UUID.randomUUID().toString(),
            ageRange = "4–5 years",
            name = "Sorting games",
            reason = "Introduces early math concepts (categorization by color, shape, size) and develops analytical thinking.",
            examples = "Sort colorful buttons, match colored socks, or separate toy cars by color and size."
        ),
        CubyJoyActivity(
            id = UUID.randomUUID().toString(),
            ageRange = "4–5 years",
            name = "Memory games",
            reason = "Improves short-term memory, concentration, and attention to detail. It also teaches patience and turn-taking.",
            examples = "Card matching games (Memory/Concentration), 'I Went to the Market' word game, or hiding a toy under cups."
        ),
        CubyJoyActivity(
            id = UUID.randomUUID().toString(),
            ageRange = "4–5 years",
            name = "Simple household tasks",
            reason = "Builds confidence, a sense of responsibility, and independence. It helps children feel like valued, contributing family members.",
            examples = "Helping set the table (napkins and spoons), putting away their toys, or matching clean socks."
        )
    )
}
