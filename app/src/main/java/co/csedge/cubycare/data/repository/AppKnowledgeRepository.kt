package co.csedge.cubycare.data.repository

import co.csedge.cubycare.data.model.Child

data class KnowledgeItem(
    val id: String,
    val category: String,
    val title: String,
    val content: String,
    val tags: List<String>,
    val route: String? = null,
    val recommendedAgeMonths: Int? = null
)

object AppKnowledgeRepository {

    fun getAllKnowledgeItems(activeChild: Child? = null): List<KnowledgeItem> {
        val items = mutableListOf<KnowledgeItem>()

        // 1. Vaccines Knowledge
        try {
            val vaccines = VaccineScheduleProvider.generateIAPSchedule(System.currentTimeMillis())
            vaccines.forEach { v ->
                items.add(
                    KnowledgeItem(
                        id = "vac_${v.name}_${v.recommendedAgeMonths}",
                        category = "VACCINE",
                        title = "${v.name} (${v.recommendedAge})",
                        content = "Recommended Age: ${v.recommendedAge}. Dose: ${v.doseNumber}. Type: ${v.type}. Reason & Disease Prevention: ${v.reason}",
                        tags = listOf("vaccine", "immunization", "shot", v.name.lowercase(), v.recommendedAge.lowercase(), v.reason.lowercase()),
                        route = "vaccines",
                        recommendedAgeMonths = v.recommendedAgeMonths
                    )
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        // 2. Milestones Knowledge
        try {
            val milestones = MilestoneProvider.generateDefaultMilestones()
            milestones.forEach { m ->
                items.add(
                    KnowledgeItem(
                        id = "mile_${m.id}",
                        category = "MILESTONE",
                        title = "${m.title} (${m.domain})",
                        content = "Domain: ${m.domain}. Age Group: ${m.ageRange}. Skill/Milestone: ${m.title}",
                        tags = listOf("milestone", "growth", "skill", m.domain.lowercase(), m.title.lowercase(), m.ageRange.lowercase()),
                        route = "milestones",
                        recommendedAgeMonths = m.ageMonths.toInt()
                    )
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        // 3. Emergency & Alert System Knowledge
        items.addAll(
            listOf(
                KnowledgeItem(
                    id = "alert_fever",
                    category = "EMERGENCY",
                    title = "High Fever in Infants (Above 100.4°F / 38°C)",
                    content = "Symptoms: Warm forehead, lethargy, irritability, flushed skin. Home Remedies: Lukewarm sponge bath, light cotton clothes, keep hydrated with milk/water. Detect Severity: High fever with stiff neck, unresponsiveness, or seizures is severe. Consult Doctor Immediately: If age < 3 months, fever > 102°F, or lasts over 24 hours.",
                    tags = listOf("fever", "temperature", "hot", "alert", "emergency", "sponge bath", "paracetamol", "heat"),
                    route = "cuby_alert"
                ),
                KnowledgeItem(
                    id = "alert_breathing",
                    category = "EMERGENCY",
                    title = "Breathing Difficulty & Respiratory Distress",
                    content = "Symptoms: Rapid breathing, chest indrawing, wheezing, flaring nostrils, blue lips. Home Remedies: Keep child upright, use cool mist humidifier, ensure clear airway. Detect Severity: Severe if chest sinks deeply or lips turn blue. Consult Doctor Immediately: Emergency! Go to hospital immediately if child struggles for breath.",
                    tags = listOf("breathing", "cough", "wheezing", "chest", "indrawing", "asthma", "choking", "oxygen", "emergency"),
                    route = "cuby_alert"
                ),
                KnowledgeItem(
                    id = "alert_dehydration",
                    category = "EMERGENCY",
                    title = "Dehydration, Vomiting & Diarrhea",
                    content = "Symptoms: Dry mouth, no tears when crying, sunken eyes, fewer wet diapers (< 4 in 24 hours), lethargy. Home Remedies: Give ORS (Oral Rehydration Solution) frequently in small sips, continue breastfeeding. Detect Severity: Severe if child cannot keep any liquid down or is unresponsive. Consult Doctor Immediately: If vomiting lasts > 12 hours or sunken fontanelle is noticed.",
                    tags = listOf("vomiting", "diarrhea", "dehydration", "ors", "water", "diaper", "loose motion", "stomach", "loose stool"),
                    route = "cuby_alert"
                ),
                KnowledgeItem(
                    id = "alert_convulsions",
                    category = "EMERGENCY",
                    title = "Seizures & Febrile Fits",
                    content = "Symptoms: Sudden jerking of limbs, rolling eyes, loss of consciousness during high fever. Home Remedies: Place child on their side on a soft surface, turn head to prevent choking. Do NOT put anything in child's mouth. Detect Severity: Extremely critical emergency. Consult Doctor Immediately: Call ambulance or rush to nearest emergency room immediately.",
                    tags = listOf("seizure", "fit", "febrile", "convulsion", "jerking", "unresponsive", "emergency"),
                    route = "cuby_alert"
                ),
                KnowledgeItem(
                    id = "alert_allergic_reaction",
                    category = "EMERGENCY",
                    title = "Severe Allergic Reaction & Anaphylaxis",
                    content = "Symptoms: Swelling of face/lips, hives/rash, difficulty breathing, vomiting shortly after new food or medicine. Home Remedies: Stop offending food/medicine immediately. Keep airway clear. Detect Severity: Severe if swelling affects throat or causes wheezing. Consult Doctor Immediately: Immediate emergency medical care required.",
                    tags = listOf("allergy", "anaphylaxis", "hives", "swelling", "peanut", "egg", "rash", "reaction", "emergency"),
                    route = "cuby_alert"
                )
            )
        )

        // 4. Parenting Advice Knowledge
        items.addAll(
            listOf(
                KnowledgeItem(
                    id = "parenting_crying",
                    category = "PARENTING",
                    title = "Excessive Crying & Infant Colic",
                    content = "Reason: Colic, gas, hunger, overtiredness, or discomfort. Actionable Steps: Use the 5 S's (Swaddle, Side/Stomach hold, Shush, Swing, Suck). Gently burp baby after every feed. Massaging tummy in clock-wise circles helps relieve gas. Lookout: Persistent crying for > 3 hours a day, 3 days a week. Consult Doctor: If accompanied by fever, vomiting, or refusal to feed.",
                    tags = listOf("crying", "colic", "gas", "burp", "swaddle", "soothe", "fussing", "inconsolable"),
                    route = "cuby_parenting"
                ),
                KnowledgeItem(
                    id = "parenting_sleep",
                    category = "PARENTING",
                    title = "Establishing Safe & Healthy Sleep Routines",
                    content = "Reason: Infants need 14-17 hours of sleep per day with clear day/night cues. Actionable Steps: Keep bedroom dim and quiet. Put baby to sleep on their back on a firm mattress (Back to Sleep). Create a consistent bedtime ritual (bath, lullaby, cuddle). Avoid loose blankets or pillows in crib. Consult Doctor: If child snores loudly, gasps for air, or wakes up constantly in distress.",
                    tags = listOf("sleep", "nap", "routine", "bedtime", "crib", "sids", "night", "tired", "wake window"),
                    route = "cuby_parenting"
                ),
                KnowledgeItem(
                    id = "parenting_teething",
                    category = "PARENTING",
                    title = "Teething Pain Relief & Care",
                    content = "Reason: First teeth usually emerge between 4 to 7 months. Actionable Steps: Provide a chilled (not frozen) teething ring or clean silicone teether. Gently massage gums with a clean finger. Wipe drool frequently to prevent rashes. Consult Doctor: If child has high fever (>101°F) or diarrhea (teething causes mild irritability, not severe illness).",
                    tags = listOf("teeth", "teething", "gums", "drooling", "chewing", "teether", "tooth"),
                    route = "cuby_parenting"
                )
            )
        )

        // 5. Nutrition & Diet Knowledge
        items.addAll(
            listOf(
                KnowledgeItem(
                    id = "nutri_0_6m",
                    category = "NUTRITION",
                    title = "Nutrition Guidelines: 0 to 6 Months",
                    content = "Dietary Guidance: Exclusive breastfeeding or infant formula ONLY. Do NOT give water, cow's milk, honey, or solid foods before 6 months. Feeding Frequency: On demand, typically every 2 to 3 hours (8 to 12 times a day). Supplement: Vitamin D drops as advised by pediatrician.",
                    tags = listOf("nutrition", "diet", "food", "breastfeeding", "formula", "milk", "0-6 months", "newborn", "exclusive"),
                    route = "nutrition",
                    recommendedAgeMonths = 3
                ),
                KnowledgeItem(
                    id = "nutri_6_12m",
                    category = "NUTRITION",
                    title = "Weaning & Solid Food Intro: 6 to 12 Months",
                    content = "Dietary Guidance: Introduce single-ingredient purees and soft mashes (Ragi porridge, apple puree, mashed banana, khichdi, dal mash, pumpkin). Follow 3-day wait rule for new foods to test for allergies. Continue breast milk or formula as primary nutrition source. Avoid added salt, sugar, and honey until 1 year.",
                    tags = listOf("nutrition", "solid food", "weaning", "puree", "khichdi", "ragi", "banana", "6-12 months", "diet"),
                    route = "nutrition",
                    recommendedAgeMonths = 9
                ),
                KnowledgeItem(
                    id = "nutri_1_3y",
                    category = "NUTRITION",
                    title = "Toddler Nutrition & Balanced Meals: 1 to 3+ Years",
                    content = "Dietary Guidance: Family table foods cut into small bites. Include 3 main meals and 2 healthy snacks daily. Offer whole cow's milk, lentils (dal), roti, rice, eggs, vegetables, cottage cheese (paneer), fruits, and healthy fats (ghee). Limit fruit juices and sugary treats.",
                    tags = listOf("toddler", "nutrition", "meal plan", "roti", "paneer", "egg", "fruit", "1-3 years", "balanced diet"),
                    route = "nutrition",
                    recommendedAgeMonths = 24
                )
            )
        )

        // 6. Pediatric Disorders Knowledge
        try {
            val disorderList = listOf("Down Syndrome", "Cystic Fibrosis", "Autism Spectrum")
            disorderList.forEach { name ->
                val d = DisorderRepository.getDisorderInfo(name)
                items.add(
                    KnowledgeItem(
                        id = "disorder_${d.name}",
                        category = "DISORDER",
                        title = d.name,
                        content = "Reasons: ${d.reasons}. Main Symptoms: ${d.mainSymptoms}. Care & Medication: ${d.simpleMedication}. Avoidance: ${d.avoidance}",
                        tags = listOf("disorder", "illness", "condition", d.name.lowercase()),
                        route = "health_tracker"
                    )
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        // 7. Health Advice & Symptoms Knowledge
        try {
            val ageRanges = listOf("0-6 Months", "6-12 Months", "1-5 Years")
            HealthAdviceProvider.symptomsList.forEach { symptom ->
                ageRanges.forEach { age ->
                    val advice = HealthAdviceProvider.getSymptomAdvice(age, symptom)
                    items.add(
                        KnowledgeItem(
                            id = "advice_${symptom}_$age",
                            category = "HEALTH_ADVICE",
                            title = "Health Advice: $symptom ($age)",
                            content = "Description: ${advice.description}. Home Care: ${advice.homeRemedies.joinToString("; ")}. Doctor Warning Factors: ${advice.doctorFactors.joinToString("; ")}",
                            tags = listOf("health", "advice", "illness", symptom.lowercase(), age.lowercase()),
                            route = "health_tracker"
                        )
                    )
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        // 8. Sleep Cycles Knowledge
        try {
            SleepCycleProvider.ageRanges.forEach { ageRange ->
                val s = SleepCycleProvider.getSleepInfoForAge(ageRange)
                items.add(
                    KnowledgeItem(
                        id = "sleep_${s.ageRange}",
                        category = "SLEEP",
                        title = "Sleep Cycle Guide: ${s.ageRange}",
                        content = "Total Sleep: ${s.sleepDuration}. Naps: ${s.numberOfNaps}. Night Awakenings: ${s.nightAwakenings}. When Disturbed: ${s.whenDisturbed}. Remedies: ${s.mildRemedies}. Severe Warnings: ${s.severeSymptoms}",
                        tags = listOf("sleep", "nap", "wake window", "bedtime", s.ageRange.lowercase()),
                        route = "cuby_naps"
                    )
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        // 9. Teething & Oral Care Knowledge
        try {
            SmileCareProvider.ageRanges.forEach { ageRange ->
                val sc = SmileCareProvider.getSmileInfoForAge(ageRange)
                items.add(
                    KnowledgeItem(
                        id = "smile_${sc.ageRange}",
                        category = "ORAL_CARE",
                        title = "Oral & Dental Care: ${sc.ageRange}",
                        content = "First Tooth: ${sc.firstTooth}. Brushing Advice: ${sc.brushing}. Fluoride Guidance: ${sc.fluoride}. Decay Prevention: ${sc.decayPrevention}",
                        tags = listOf("teeth", "dental", "brushing", "smile", "toothpaste", sc.ageRange.lowercase()),
                        route = "cuby_smile"
                    )
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        // 10. Allergies Knowledge
        try {
            val allergyNames = listOf("Peanut", "Milk", "Egg", "Dust")
            allergyNames.forEach { name ->
                val al = AllergyProvider.getAllergyInfo(name)
                items.add(
                    KnowledgeItem(
                        id = "allergy_${al.name}",
                        category = "ALLERGY",
                        title = "Allergy Profile: ${al.name}",
                        content = "Causes: ${al.causes}. Main Symptoms: ${al.mainSymptoms}. Home Remedies: ${al.homeRemedies}. Avoidance Strategy: ${al.avoidance}",
                        tags = listOf("allergy", "allergic", "reaction", al.name.lowercase(), name.lowercase()),
                        route = "allergies"
                    )
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        // 11. Play & Joy Knowledge
        try {
            CubyJoyProvider.ageRanges.forEach { ageRange ->
                val activities = CubyJoyProvider.getActivitiesForAge(ageRange)
                activities.forEach { act ->
                    items.add(
                        KnowledgeItem(
                            id = "joy_${act.id}",
                            category = "PLAY",
                            title = "Play Activity: ${act.name} (${act.ageRange})",
                            content = "Benefits & Development: ${act.reason}. Play Examples: ${act.examples}",
                            tags = listOf("play", "activity", "game", "toy", "brain", act.name.lowercase(), act.ageRange.lowercase()),
                            route = "cuby_joy"
                        )
                    )
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        // 11b. Detailed Individual Vaccine Educational Mechanics
        try {
            val commonVaccines = listOf("BCG", "OPV", "Hepatitis B", "DTwP", "IPV", "Hib", "Rotavirus", "PCV", "MMR", "Varicella", "Typhoid", "HPV")
            commonVaccines.forEach { vName ->
                val vInfo = VaccineKnowledgeProvider.getInfoForVaccine(vName)
                items.add(
                    KnowledgeItem(
                        id = "vac_detail_${vInfo.vaccineName}",
                        category = "VACCINE_DETAIL",
                        title = "${vInfo.vaccineName} Vaccine: How it Works & Why Needed",
                        content = "Medical Name: ${vInfo.fullMedicalName}. Disease Prevented: ${vInfo.diseasePrevented}. Why Needed: ${vInfo.whyNeeded}. How It Works: ${vInfo.howItWorks}. Route: ${vInfo.administrationRoute}. Normal Post-Vaccine Reactions: ${vInfo.normalReactions}.",
                        tags = listOf("vaccine", "how it works", "why needed", vInfo.vaccineName.lowercase(), vInfo.diseasePrevented.lowercase(), "immunization"),
                        route = "vaccines"
                    )
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        // 13. First Aid & Injury Care Knowledge
        items.addAll(
            listOf(
                KnowledgeItem(
                    id = "fa_burns",
                    category = "FIRST_AID",
                    title = "First Aid for Minor Burns & Scalds",
                    content = "Immediate Action: Hold burned area under cool running tap water for 10-15 minutes immediately. Do NOT use ice, butter, or toothpaste. Gently pat dry and cover with a sterile non-stick bandage. See Doctor: If burn is on face, hands, joints, or forms large painful blisters.",
                    tags = listOf("burn", "first aid", "scald", "hot water", "blister", "cool water"),
                    route = "cuby_alert"
                ),
                KnowledgeItem(
                    id = "fa_choking",
                    category = "FIRST_AID",
                    title = "Emergency Infant & Toddler Choking First Aid",
                    content = "Infants (< 1 yr): Lay baby face down along your forearm supporting head. Deliver 5 firm back blows between shoulder blades using palm heel. If not dislodged, turn face up and give 5 chest thrusts. Toddlers (> 1 yr): Perform Heimlich maneuver (abdominal thrusts) standing behind child. Emergency: Call ambulance immediately if child cannot breathe, cough, or speak.",
                    tags = listOf("choking", "choke", "first aid", "heimlich", "back blow", "chest thrust", "emergency"),
                    route = "cuby_alert"
                ),
                KnowledgeItem(
                    id = "fa_cuts",
                    category = "FIRST_AID",
                    title = "First Aid for Cuts, Scrapes & Minor Wounds",
                    content = "Action: Apply direct gentle pressure with a clean cloth to stop bleeding. Wash wound thoroughly under clean running water with mild soap. Apply an antiseptic cream and cover with a clean sterile bandage. See Doctor: If bleeding does not stop after 10 minutes of continuous pressure or if wound is deep.",
                    tags = listOf("cut", "wound", "scrape", "bleed", "first aid", "bandage", "antiseptic"),
                    route = "cuby_alert"
                ),
                KnowledgeItem(
                    id = "fa_headbump",
                    category = "FIRST_AID",
                    title = "First Aid for Head Bumps & Minor Head Trauma",
                    content = "Action: Apply a cold compress or ice pack wrapped in a cloth for 10-15 minutes to reduce swelling. Observe child closely for 24 hours. Emergency Doctor Visit: If child vomits more than once, shows extreme drowsiness, unequal pupil size, fluid from nose/ears, or loses consciousness.",
                    tags = listOf("head bump", "head injury", "fall", "swelling", "vomiting", "concussion", "first aid"),
                    route = "cuby_alert"
                )
            )
        )

        // 14. Child Behavior & Digital Screen Time Guidance
        items.addAll(
            listOf(
                KnowledgeItem(
                    id = "beh_screentime",
                    category = "BEHAVIOR",
                    title = "WHO & AAP Screen Time Guidelines for Children",
                    content = "Under 2 Years: ZERO screen time recommended (except video calls with family). 2 to 5 Years: Maximum 1 hour of high-quality co-viewing educational screen time per day. Impact: Excessive screen time delays speech, impairs sleep quality, and increases behavioral tantrums.",
                    tags = listOf("screen time", "tv", "mobile", "phone", "speech delay", "who guidelines", "behavior"),
                    route = "cuby_parenting"
                ),
                KnowledgeItem(
                    id = "beh_tantrums",
                    category = "BEHAVIOR",
                    title = "Managing Toddler Temper Tantrums & Meltdowns",
                    content = "Why It Happens: Toddlers lack emotional regulation and language skills to express frustration. Response: Stay calm, ensure safety, avoid shouting or bribing. Use positive reinforcement when child cools down. Teach emotional naming ('I see you are upset').",
                    tags = listOf("tantrum", "crying", "anger", "meltdown", "toddler", "behavior", "discipline"),
                    route = "cuby_parenting"
                )
            )
        )

        // 15. Seasonal Protection & Environmental Care
        items.addAll(
            listOf(
                KnowledgeItem(
                    id = "season_mosquito",
                    category = "SEASONAL_CARE",
                    title = "Monsoon Mosquito Protection & Dengue Prevention",
                    content = "Protection: Use pediatrician-approved fabric roll-on repellents, wear full-sleeve light cotton clothes, install bed nets and window meshes. Drain any stagnant water around the home to stop mosquito breeding.",
                    tags = listOf("mosquito", "dengue", "malaria", "repellent", "monsoon", "bites"),
                    route = "cuby_parenting"
                ),
                KnowledgeItem(
                    id = "season_summer",
                    category = "SEASONAL_CARE",
                    title = "Summer Heatstroke & Hydration Guidelines",
                    content = "Action: Keep child in cool shaded areas, dress in loose breathable cotton, offer frequent water sips, fresh fruit mashes, or coconut water. Avoid direct midday sun exposure between 11 AM and 4 PM.",
                    tags = listOf("summer", "heat", "heatstroke", "hydration", "water", "sun protection"),
                    route = "nutrition"
                )
            )
        )

        // 16. Postpartum & Parental Wellness
        items.addAll(
            listOf(
                KnowledgeItem(
                    id = "postpartum_mental",
                    category = "PARENTAL_CARE",
                    title = "Postpartum Depression (PPD) & Maternal Mental Health",
                    content = "Symptoms: Persistent sadness, anxiety, extreme fatigue, feeling overwhelmed, difficulty bonding with baby. Action: PPD is common and treatable. Seek support from partner, family, and consult a doctor or counselor early.",
                    tags = listOf("postpartum", "mother", "depression", "ppd", "mental health", "parent care"),
                    route = "cuby_parenting"
                )
            )
        )

        // 17. Gender-Specific Health, Growth & Hygiene Knowledge (Boys vs Girls)
        items.addAll(
            listOf(
                KnowledgeItem(
                    id = "gender_boy_growth",
                    category = "GENDER_GROWTH",
                    title = "WHO Growth Standards for Baby Boys (0 to 5 Years)",
                    content = "Weight & Height Standards: At Birth (3.3kg, 49.9cm, Head 34.5cm); 6 Months (7.9kg, 67.6cm, Head 43.3cm); 12 Months (9.6kg, 75.7cm, Head 46.1cm); 24 Months (12.2kg, 87.1cm). Boys tend to be slightly heavier and taller on average compared to girls of the same age.",
                    tags = listOf("boy", "boys", "boy growth", "boy weight", "boy height", "who growth chart", "male baby"),
                    route = "growth"
                ),
                KnowledgeItem(
                    id = "gender_boy_care",
                    category = "GENDER_CARE",
                    title = "Baby Boy Anatomical Care & Foreskin Hygiene Rules",
                    content = "Foreskin Care: Do NOT forcibly retract baby boy's foreskin. In infants, the foreskin is naturally attached to the glans penis. Forcible pulling causes painful tearing, bleeding, and scarring (phimosis). Wash only the outside gently with warm water. Natural separation occurs between 2 to 5+ years. Testicular Health: Check that both testicles are descended into the scrotum (report undescended testicles by 6 months).",
                    tags = listOf("boy care", "foreskin", "phimosis", "penis care", "testicles", "cryptorchidism", "boy hygiene", "male baby"),
                    route = "cuby_parenting"
                ),
                KnowledgeItem(
                    id = "gender_girl_growth",
                    category = "GENDER_GROWTH",
                    title = "WHO Growth Standards for Baby Girls (0 to 5 Years)",
                    content = "Weight & Height Standards: At Birth (3.2kg, 49.1cm, Head 33.9cm); 6 Months (7.3kg, 65.7cm, Head 42.2cm); 12 Months (8.9kg, 74.0cm, Head 44.9cm); 24 Months (11.5kg, 85.7cm). Girls follow WHO standard percentile growth curves tailored specifically for female infants.",
                    tags = listOf("girl", "girls", "girl growth", "girl weight", "girl height", "who growth chart", "female baby"),
                    route = "growth"
                ),
                KnowledgeItem(
                    id = "gender_girl_care",
                    category = "GENDER_CARE",
                    title = "Baby Girl Anatomical Care & Hygiene (UTI Prevention)",
                    content = "Wiping Rule: ALWAYS wipe front-to-back (from vagina toward anus). Never wipe back-to-front as it transfers intestinal E. coli bacteria into the short female urethra, leading to Urinary Tract Infections (UTIs). Newborn Spotting: Mild white discharge or slight blood-tinged spotting in first 1-2 weeks is normal maternal hormone withdrawal ('pseudo-menstruation'). Clean gently with warm water. Labial Adhesions: Monitor for thin labial fusion; consult pediatrician if urination is obstructed.",
                    tags = listOf("girl care", "front to back", "uti", "vaginal discharge", "labia", "girl hygiene", "female baby"),
                    route = "cuby_parenting"
                ),
                KnowledgeItem(
                    id = "gender_hpv_vaccine",
                    category = "GENDER_VACCINE",
                    title = "HPV Vaccine Guidelines for Girls & Boys",
                    content = "Target Age: Recommended for girls starting at age 9 to 14 years (2-dose series spaced 6 months apart) to prevent Human Papillomavirus, the leading cause of cervical cancer. Also recommended for boys for universal HPV protection.",
                    tags = listOf("hpv", "cervical cancer", "girls vaccine", "teen vaccine", "hpv dose", "boy vaccine"),
                    route = "vaccines"
                )
            )
        )

        // 12. User's Active Child Health Profile (If logged in)
        activeChild?.let { child ->
            items.add(
                KnowledgeItem(
                    id = "child_active_profile",
                    category = "HEALTH_PROFILE",
                    title = "${child.name}'s Health & Personal Profile",
                    content = "Child Name: ${child.name}. Age: ${child.ageFormatted} (${child.ageInMonths} months). Gender: ${child.gender}. Birth Weight: ${child.birthWeight}. Birth Length: ${child.birthLength}. Blood Group: ${child.bloodGroup.ifEmpty { "Not specified" }}. Allergies: ${child.allergies.ifEmpty { "None recorded" }}. Medical Conditions: ${child.currentMedicalConditions.ifEmpty { "None recorded" }}. Logged Medicines: ${if (child.medicines.isEmpty()) "No active medicines" else child.medicines.joinToString { "${it.name} (${it.dose})" }}. Logged Growth Records: ${if (child.growthLogs.isEmpty()) "No height/weight logs" else "Latest recorded weight: ${child.growthLogs.last().weightKg}kg, length: ${child.growthLogs.last().lengthCm}cm"}.",
                    tags = listOf("my child", "profile", child.name.lowercase(), "growth", "medicine", "age", "weight", "height", "blood group"),
                    route = "profile"
                )
            )
        }

        return items
    }
}
