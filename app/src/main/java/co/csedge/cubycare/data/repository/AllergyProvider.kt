package co.csedge.cubycare.data.repository

import co.csedge.cubycare.data.model.Allergy
import co.csedge.cubycare.data.model.FlowchartStep

class AllergyProvider {
    companion object {
        fun getAllergyInfo(allergyString: String): Allergy {
            val lowercaseAllergy = allergyString.lowercase()
            
            // Peanut / Nuts
            if (lowercaseAllergy.contains("peanut") || lowercaseAllergy.contains("nut")) {
                return Allergy(
                    name = "Peanut / Tree Nut Allergy",
                    duration = "Often lifelong, though some outgrow it",
                    causes = "The immune system overreacts to proteins found in peanuts or tree nuts.",
                    isCurable = false,
                    homeRemedies = "Strict avoidance of nuts. Over-the-counter antihistamines for mild reactions. Use an epinephrine auto-injector (EpiPen) immediately for severe reactions.",
                    pediatricianName = "Dr. Robert Smith",
                    pediatricianSpecialization = "Pediatric Allergist",
                    pediatricianLocation = "City Allergy Center",
                    hospitalTimings = "Mon - Fri: 9:00 AM - 5:00 PM\nSat: 9:00 AM - 1:00 PM",
                    exactHospitalAddress = "101 Wellness Blvd, Cityville",
                    mainSymptoms = "Hives, redness or swelling, digestive problems, tightening of the throat, shortness of breath, or anaphylaxis.",
                    avoidance = "Read food labels carefully. Avoid cross-contamination in kitchens. Inform schools and caregivers about the allergy.",
                    flowchartSteps = listOf(
                        FlowchartStep("Exposure", "Child eats or is exposed to peanut/nut proteins."),
                        FlowchartStep("Immune Response", "The immune system incorrectly identifies the protein as harmful and releases histamine."),
                        FlowchartStep("Symptoms", "Histamine causes inflammation, leading to hives, swelling, or respiratory issues."),
                        FlowchartStep("Treatment", "Mild cases use antihistamines; severe cases require immediate epinephrine and medical help.")
                    )
                )
            }
            
            // Cow's Milk / Dairy
            if (lowercaseAllergy.contains("milk") || lowercaseAllergy.contains("dairy")) {
                return Allergy(
                    name = "Cow's Milk Allergy",
                    duration = "Most children outgrow it by age 3 to 5",
                    causes = "Immune reaction to casein or whey proteins found in cow's milk.",
                    isCurable = false,
                    homeRemedies = "Use hypoallergenic formulas if bottle-feeding. If breastfeeding, the mother may need to avoid dairy. Antihistamines for mild accidental exposure.",
                    pediatricianName = "Dr. Linda Adams",
                    pediatricianSpecialization = "Pediatric Allergist / Immunologist",
                    pediatricianLocation = "National Pediatric Clinic",
                    hospitalTimings = "Mon - Fri: 8:00 AM - 4:00 PM",
                    exactHospitalAddress = "442 Healthcare Way, Metropolis",
                    mainSymptoms = "Wheezing, vomiting, hives, digestive problems (diarrhea, bloody stools), or anaphylaxis in severe cases.",
                    avoidance = "Avoid all cow's milk products (cheese, yogurt, butter). Look for hidden dairy in baked goods and processed foods.",
                    flowchartSteps = listOf(
                        FlowchartStep("Protein Ingestion", "Child consumes milk proteins (casein or whey)."),
                        FlowchartStep("Antibody Production", "IgE antibodies are produced against the proteins."),
                        FlowchartStep("Allergic Reaction", "Release of chemicals causing skin, respiratory, or gastrointestinal symptoms."),
                        FlowchartStep("Management", "Dietary elimination and symptom management until outgrown.")
                    )
                )
            }
            
            // Dust / Mites
            if (lowercaseAllergy.contains("dust") || lowercaseAllergy.contains("mite")) {
                return Allergy(
                    name = "Dust Mite Allergy",
                    duration = "Often lifelong, managed with environment control",
                    causes = "Allergic reaction to tiny bugs that commonly live in house dust.",
                    isCurable = false,
                    homeRemedies = "Use allergen-proof bed covers. Wash bedding weekly in hot water. Keep humidity low. Nasal saline sprays or antihistamines can help manage symptoms.",
                    pediatricianName = "Dr. Alan Green",
                    pediatricianSpecialization = "Pediatric Pulmonologist / Allergist",
                    pediatricianLocation = "Breathe Easy Clinic",
                    hospitalTimings = "Mon - Sat: 10:00 AM - 6:00 PM",
                    exactHospitalAddress = "77 Clear Air St, Suburbia",
                    mainSymptoms = "Sneezing, runny nose, itchy or watery eyes, nasal congestion, cough, and exacerbated asthma symptoms.",
                    avoidance = "Remove carpets if possible, especially in the bedroom. Vacuum regularly with a HEPA filter. Keep stuffed toys off the bed or wash them frequently.",
                    flowchartSteps = listOf(
                        FlowchartStep("Inhalation", "Child inhales dust mite waste particles from bedding or air."),
                        FlowchartStep("Nasal Inflammation", "The nasal passages become inflamed (allergic rhinitis)."),
                        FlowchartStep("Symptom Onset", "Sneezing, congestion, and itchy eyes begin."),
                        FlowchartStep("Environmental Control", "Reducing dust mites in the home significantly reduces symptoms.")
                    )
                )
            }

            // Fallback for any other / generic or multiple allergies
            val displayName = if (allergyString.contains(",")) "Multiple Allergies" else allergyString.trim().replaceFirstChar { it.uppercase() }
            return Allergy(
                name = displayName,
                duration = "Varies by specific allergy",
                causes = "The immune system overreacts to a typically harmless substance.",
                isCurable = false,
                homeRemedies = "Avoidance of the allergen. Over-the-counter antihistamines for mild reactions. Always carry an epinephrine auto-injector if prescribed.",
                pediatricianName = "Dr. Emily Chen",
                pediatricianSpecialization = "Pediatric Specialist",
                pediatricianLocation = "Regional Children's Healthcare Center",
                hospitalTimings = "Mon - Sun: 24/7 (Emergency)\nMon - Fri: 9:00 AM - 6:00 PM (OPD)",
                exactHospitalAddress = "789 Healthway Drive, Regional Hub",
                mainSymptoms = "Can range from mild (hives, sneezing, mild stomach ache) to severe (difficulty breathing, swelling of the face/throat, anaphylaxis).",
                avoidance = "Strictly avoid the known allergens. Educate caregivers and teachers. Read labels on food and products carefully.",
                flowchartSteps = listOf(
                    FlowchartStep("Allergen Exposure", "The child comes into contact with the allergen (eaten, inhaled, touched)."),
                    FlowchartStep("Immune System Reaction", "The body mistakenly identifies the substance as dangerous."),
                    FlowchartStep("Chemical Release", "Histamines and other chemicals are released into the bloodstream."),
                    FlowchartStep("Physical Symptoms", "These chemicals cause the symptoms of an allergic reaction.")
                )
            )
        }
    }
}
