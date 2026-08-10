package co.csedge.cubycare.data.repository

data class SymptomInfo(
    val name: String,
    val description: String,
    val homeRemedies: List<String>,
    val doctorFactors: List<String>
)

object HealthAdviceProvider {
    val symptomsList = listOf(
        "Temperature", "Cough", "Vomiting", "Diarrhea", "Appetite", 
        "Urine frequency", "Bowel movements", "Rash", "Pain"
    )

    fun getSymptomAdvice(ageRange: String, symptomName: String): SymptomInfo {
        // Fallback or default content structure
        var desc = "General information about this symptom."
        var remedies = listOf("Monitor closely.", "Keep the child comfortable and hydrated.")
        var doctors = listOf("If symptoms worsen significantly or the child is unresponsive, seek immediate medical care.")

        if (ageRange == "0-6 Months") {
            when (symptomName) {
                "Temperature" -> {
                    desc = "Normal body temperature for an infant is around 97.5°F to 99.5°F."
                    remedies = listOf("Dress in light clothing.", "Give plenty of breastmilk or formula to keep hydrated.", "Do NOT use cold baths or alcohol rubs.")
                    doctors = listOf("Any fever (100.4°F / 38°C or higher) in a baby under 3 months is an emergency.", "Fever lasting more than 24 hours in a baby 3-6 months.")
                }
                "Cough" -> {
                    desc = "Coughing helps clear the airway, but infants have very small airways."
                    remedies = listOf("Use a cool-mist humidifier in their room.", "Use a rubber bulb syringe and saline drops for nasal congestion.", "Do NOT give over-the-counter cough or cold medicines.", "NEVER give honey to a baby under 1 year.")
                    doctors = listOf("Breathing rapidly, working hard to breathe, or grunting.", "Cough sounds like a 'bark' or is accompanied by a 'whoop' sound.", "Blue coloration around the lips.")
                }
                "Vomiting" -> {
                    desc = "Spitting up is normal, but forceful vomiting is not."
                    remedies = listOf("Feed smaller, more frequent amounts.", "Keep the baby upright after feeding.", "Offer small amounts of oral rehydration solution if advised by a doctor.")
                    doctors = listOf("Vomiting forcefully (projectile vomiting).", "Vomit is green (bile) or contains blood.", "Signs of dehydration (no wet diapers for 6 hours, crying without tears).")
                }
                "Diarrhea" -> {
                    desc = "Infant stool can be loose, especially if breastfed, but a sudden increase in frequency and wateriness is diarrhea."
                    remedies = listOf("Continue breastfeeding or formula feeding to prevent dehydration.", "Use barrier cream to prevent diaper rash.")
                    doctors = listOf("Stool contains blood or mucus.", "Signs of dehydration.", "Accompanied by a high fever.")
                }
                "Appetite" -> {
                    desc = "Infants generally feed every 2-4 hours. A sudden drop in appetite can indicate illness."
                    remedies = listOf("Offer the breast or bottle more frequently without forcing.", "Ensure a calm, distraction-free environment for feeding.")
                    doctors = listOf("Refuses to feed for multiple consecutive feedings.", "Too lethargic to wake up for feeds.", "Weight loss or lack of wet diapers.")
                }
                "Urine frequency" -> {
                    desc = "A healthy infant usually has 6 or more wet diapers a day."
                    remedies = listOf("Offer more breastmilk or formula if urine output seems slightly reduced but the baby is otherwise well.")
                    doctors = listOf("No wet diaper for 6 hours or more.", "Urine is very dark or smells unusually strong.", "Crying or apparent pain during urination.")
                }
                "Bowel movements" -> {
                    desc = "Frequency varies greatly. Breastfed babies may go after every feed or once every few days."
                    remedies = listOf("For constipation (hard, pebble-like stool), you can do gentle 'bicycle legs' exercises.", "A warm bath may help relax their bowels.")
                    doctors = listOf("Stool is black (after the meconium stage), white, or contains red blood.", "Baby is in severe pain when trying to pass stool.", "No bowel movement for several days accompanied by vomiting or a swollen belly.")
                }
                "Rash" -> {
                    desc = "Infant skin is sensitive. Baby acne, cradle cap, and diaper rash are common."
                    remedies = listOf("Keep the skin clean and dry.", "Use a thick barrier cream (zinc oxide) for diaper rash.", "Wash with mild, fragrance-free baby soap.")
                    doctors = listOf("Rash looks like small purple or red dots that do not fade when you press on them (petechiae).", "Rash is accompanied by a fever.", "Oozing, blisters, or signs of infection.")
                }
                "Pain" -> {
                    desc = "Infants express pain through crying, fussiness, or body tension."
                    remedies = listOf("Skin-to-skin contact and gentle rocking.", "Swaddling and using a pacifier.", "Do NOT give pain medication unless directed by a pediatrician.")
                    doctors = listOf("Inconsolable crying lasting for hours.", "Crying when touched or moved in a specific way.", "Accompanied by a fever or vomiting.")
                }
            }
        } else if (ageRange == "6-12 Months") {
             when (symptomName) {
                "Temperature" -> {
                    desc = "Fevers are a natural response to infection."
                    remedies = listOf("Offer water or oral rehydration solutions in addition to milk.", "Dress lightly.", "Acetaminophen or Ibuprofen ONLY if advised by your doctor.")
                    doctors = listOf("Fever of 102.2°F (39°C) or higher.", "Fever lasting more than 3 days.", "Child is extremely lethargic or unresponsive.")
                }
                "Cough" -> {
                    desc = "Coughs are common and often due to viral colds."
                    remedies = listOf("Warm fluids (like warm water or diluted apple juice) can soothe the throat.", "Cool-mist humidifier.", "NEVER give honey before 1 year of age.")
                    doctors = listOf("Breathing rapidly or chest retractions (sucking in ribs).", "Wheezing or noisy breathing.", "Coughing so hard it causes vomiting.")
                }
                "Vomiting" -> {
                    desc = "Can be caused by stomach bugs or introducing new foods."
                    remedies = listOf("Wait 30-60 minutes after vomiting before offering fluids.", "Offer 1-2 teaspoons of oral rehydration solution every 5-10 minutes.", "Gradually reintroduce bland solid foods when vomiting stops.")
                    doctors = listOf("Vomiting continues for more than 12-24 hours.", "Vomit contains blood or is bright green.", "Signs of dehydration (fewer than 3 wet diapers in 24 hours).")
                }
                "Diarrhea" -> {
                    desc = "Loose stools can occur with diet changes, teething, or infections."
                    remedies = listOf("Keep hydrated with breastmilk, formula, or oral rehydration solution.", "Avoid fruit juices which can make diarrhea worse.", "Offer binding foods like bananas, rice, or applesauce if eating solids.")
                    doctors = listOf("Diarrhea lasts more than 2-3 days.", "Stool contains blood.", "Signs of severe dehydration.")
                }
                "Appetite" -> {
                    desc = "Appetite may fluctuate, especially during teething or illness."
                    remedies = listOf("Offer favorite, easy-to-swallow foods.", "Focus on hydration rather than forcing solids.", "Offer teething toys if gums are swollen.")
                    doctors = listOf("Refuses all liquids for over 6-8 hours.", "Significant weight loss.", "Signs of dehydration.")
                }
                "Urine frequency" -> {
                    desc = "A good indicator of hydration status."
                    remedies = listOf("Increase fluid intake with water or milk.", "Offer hydrating foods like watermelon or cucumbers if eating solids.")
                    doctors = listOf("Fewer than 3 wet diapers in a 24-hour period.", "Dark, concentrated urine.", "No tears when crying.")
                }
                "Bowel movements" -> {
                    desc = "Solid foods change the texture and frequency of bowel movements."
                    remedies = listOf("For constipation, offer 1-2 ounces of prune or pear juice.", "Ensure they are getting enough water.", "Offer high-fiber purees like peas or prunes.")
                    doctors = listOf("Severe pain during bowel movements.", "Blood in the stool.", "Constipation lasting more than a few days despite dietary changes.")
                }
                "Rash" -> {
                    desc = "Rashes can be caused by new foods, viruses, or contact dermatitis."
                    remedies = listOf("Apply cool compresses to itchy areas.", "Use fragrance-free lotions.", "Oatmeal baths can soothe irritated skin.")
                    doctors = listOf("Rash does not blanch (fade) when pressed.", "Hives accompanied by swelling of the face or difficulty breathing (call 911).", "Rash that looks infected or spreads rapidly.")
                }
                "Pain" -> {
                    desc = "Pain may stem from teething, earaches, or minor injuries."
                    remedies = listOf("Provide cold teething rings for gum pain.", "Lots of cuddles and distraction.", "Acetaminophen or Ibuprofen as directed by a doctor.")
                    doctors = listOf("Child is pulling at their ears and has a fever.", "Unusual or inconsolable crying.", "Limping or refusing to use an arm or leg.")
                }
            }
        } else { // 1-5 Years
             when (symptomName) {
                "Temperature" -> {
                    desc = "Toddlers and preschoolers often get fevers from common viruses."
                    remedies = listOf("Keep them hydrated with water, clear broths, or popsicles.", "Ensure they rest.", "Use fever-reducing medication (Acetaminophen/Ibuprofen) if they are uncomfortable, following package dosing by weight.")
                    doctors = listOf("Fever lasting more than 3 days.", "Fever accompanied by a stiff neck, extreme headache, or unexplained rash.", "Child is exceptionally lethargic or confused.")
                }
                "Cough" -> {
                    desc = "Often worse at night and can linger for weeks after a cold."
                    remedies = listOf("1/2 to 1 teaspoon of honey can help soothe a cough (ONLY for children over 1 year).", "Cool-mist humidifier in their room.", "Elevate their head slightly while sleeping.")
                    doctors = listOf("Severe coughing spasms.", "Wheezing or difficulty breathing.", "Coughing up blood.")
                }
                "Vomiting" -> {
                    desc = "Usually caused by viral gastroenteritis."
                    remedies = listOf("Give the stomach a rest for 1 hour after vomiting.", "Offer sips of water or oral rehydration solution.", "Introduce bland foods (BRAT diet: bananas, rice, applesauce, toast) when they feel hungry.")
                    doctors = listOf("Vomiting lasts more than 24 hours.", "Cannot keep even small sips of fluid down.", "Vomit contains blood or resembles coffee grounds.")
                }
                "Diarrhea" -> {
                    desc = "Common with viral infections or after taking antibiotics."
                    remedies = listOf("Ensure plenty of fluids to prevent dehydration.", "Offer a regular, healthy diet but avoid sugary drinks and juices.", "Probiotics may help (consult your doctor).")
                    doctors = listOf("Diarrhea lasts more than a week.", "Stool contains blood or mucus.", "Severe abdominal pain.")
                }
                "Appetite" -> {
                    desc = "Toddler appetites are notoriously erratic."
                    remedies = listOf("Offer small, frequent, nutrient-dense snacks.", "Do not pressure or force them to eat.", "Ensure they are drinking enough fluids.")
                    doctors = listOf("Refuses to drink fluids.", "Complains of pain when swallowing.", "Significant weight loss or lethargy.")
                }
                "Urine frequency" -> {
                    desc = "Frequency varies, but should occur several times a day."
                    remedies = listOf("Encourage drinking by offering fun cups or straws.", "Offer foods with high water content.")
                    doctors = listOf("No urination for 8 hours.", "Pain or burning during urination (could be a UTI).", "Urine is very dark or smells foul.")
                }
                "Bowel movements" -> {
                    desc = "Constipation is common during potty training or diet changes."
                    remedies = listOf("Increase fiber intake (fruits, vegetables, whole grains).", "Increase water intake.", "Encourage physical activity.")
                    doctors = listOf("Chronic constipation.", "Blood on the stool or toilet paper.", "Child is holding in their stool due to pain.")
                }
                "Rash" -> {
                    desc = "Common from viruses (like Hand, Foot, and Mouth), allergies, or heat."
                    remedies = listOf("Apply calamine lotion for itching.", "Keep the skin moisturized with unscented creams.", "Avoid harsh soaps and hot baths.")
                    doctors = listOf("Rash consisting of tiny red/purple spots that don't fade under pressure.", "Hives with breathing difficulty or facial swelling.", "A rash that spreads quickly or looks like a target (bullseye).")
                }
                "Pain" -> {
                    desc = "Toddlers can experience growing pains, minor injuries, or stomach aches."
                    remedies = listOf("Gentle massage for growing pains or sore muscles.", "A warm heating pad for mild stomach cramps.", "Over-the-counter pain relievers if appropriate.")
                    doctors = listOf("Severe, localized abdominal pain (especially on the lower right side).", "Pain that wakes them up from sleep consistently.", "Limping or inability to bear weight on a leg.")
                }
            }
        }

        return SymptomInfo(symptomName, desc, remedies, doctors)
    }
}
