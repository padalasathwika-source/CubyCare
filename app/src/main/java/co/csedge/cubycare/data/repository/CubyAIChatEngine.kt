package co.csedge.cubycare.data.repository

import co.csedge.cubycare.data.model.Child
import java.util.Locale

data class AIResponse(
    val answer: String,
    val matchedCategory: String?,
    val routeToOpen: String?,
    val routeLabel: String?,
    val relatedTopics: List<String> = emptyList()
)

object CubyAIChatEngine {

    fun askQuestion(query: String, activeChild: Child? = null): AIResponse {
        val cleanQuery = query.trim().lowercase(Locale.ROOT)
        if (cleanQuery.isEmpty()) {
            return AIResponse(
                answer = "Hello! I am your Cuby AI Assistant 🤖✨. Ask me anything about child growth, vaccinations, emergency remedies, nutrition, sleep cycles, developmental milestones, or your child's profile!",
                matchedCategory = null,
                routeToOpen = null,
                routeLabel = null
            )
        }

        val allKnowledge = AppKnowledgeRepository.getAllKnowledgeItems(activeChild)
        val queryTokens = cleanQuery.split(Regex("\\s+")).filter { it.length > 2 }

        // Score each knowledge item based on keyword matches, exact tag matches, and title matches
        val scoredItems = allKnowledge.map { item ->
            var score = 0
            val itemText = "${item.title} ${item.content} ${item.category}".lowercase(Locale.ROOT)
            
            queryTokens.forEach { token ->
                if (item.title.lowercase(Locale.ROOT).contains(token)) {
                    score += 5
                }
                if (item.tags.any { tag -> tag.contains(token) || token.contains(tag) }) {
                    score += 4
                }
                if (itemText.contains(token)) {
                    score += 2
                }
            }

            // Bonus if child's age is close to recommended age
            activeChild?.let { child ->
                item.recommendedAgeMonths?.let { recAge ->
                    val ageDiff = kotlin.math.abs(child.ageInMonths - recAge)
                    if (ageDiff <= 3) score += 3
                }
            }

            Pair(item, score)
        }.filter { it.second > 0 }
        .sortedByDescending { it.second }

        if (scoredItems.isEmpty()) {
            return AIResponse(
                answer = "I searched through all CubyCare app databases (Vaccines, Emergency Alerts, Milestones, Nutrition, Sleep, Disorders, Allergies, and Child Profile), but couldn't find an exact match for \"$query\".\n\n💡 Try asking:\n• \"Vaccine schedule for 6 weeks\"\n• \"Emergency remedy for high fever\"\n• \"What can I feed a 8 month old?\"\n• \"Summary of my child's profile\"",
                matchedCategory = null,
                routeToOpen = null,
                routeLabel = null
            )
        }

        val topMatches = scoredItems.take(3).map { it.first }
        val primary = topMatches.first()

        val answerBuilder = StringBuilder()
        answerBuilder.append("🤖 **Cuby AI Search Result**\n\n")

        topMatches.forEachIndexed { index, item ->
            if (index == 0) {
                answerBuilder.append("📍 **${item.title}**\n")
                answerBuilder.append("${item.content}\n\n")
            } else {
                answerBuilder.append("🔍 **Related Insight (${item.category}):**\n")
                answerBuilder.append("• ${item.title}: ${item.content.take(160)}...\n\n")
            }
        }

        if (primary.category == "EMERGENCY") {
            answerBuilder.append("⚠️ *Medical Disclaimer: If your child shows severe or worsening symptoms, please consult a registered pediatrician or visit the nearest healthcare emergency room immediately.*")
        } else {
            answerBuilder.append("✨ *This information was retrieved from your CubyCare app knowledge base.*")
        }

        val routeLabel = when (primary.route) {
            "vaccines" -> "View Vaccine Schedule 💉"
            "cuby_alert" -> "Open Cuby Alert System 🚨"
            "milestones" -> "View Growth Milestones 📈"
            "nutrition" -> "Open Nutrition Guide 🍎"
            "cuby_parenting" -> "View Parenting Tips 👶"
            "health_tracker" -> "View Health Tracker 🩺"
            "cuby_naps" -> "View Sleep Cycles 😴"
            "cuby_smile" -> "View Teething & Oral Care 🦷"
            "allergies" -> "View Allergy Guide 🛡️"
            "cuby_joy" -> "View Play Activities 🎨"
            "profile" -> "View Child Profile 👤"
            else -> null
        }

        return AIResponse(
            answer = answerBuilder.toString(),
            matchedCategory = primary.category,
            routeToOpen = primary.route,
            routeLabel = routeLabel,
            relatedTopics = topMatches.map { it.title }
        )
    }
}
