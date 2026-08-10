import os
replacements = {
    " -> Color(0xFFE53935)
                                                else -> Color(0xFFFFA000)
                                            }
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = entry.food,
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = ": "dynamic_3",
    ") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = quantity,
                        onValueChange = { quantity = it },
                        label = { Text(": "dynamic_18",
    ") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = food,
                        onValueChange = { food = it },
                        label = { Text(": "dynamic_17",
    ") },
                                    modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
                                )
                            }
                        }
                    }

                    item {
                        Text(
                            text = ": "dynamic_15",
    ") },
                                    modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
                                )
                                
                                OutlinedTextField(
                                    value = foodAllergies,
                                    onValueChange = { 
                                        foodAllergies = it
                                        savePreferences()
                                    },
                                    label = { Text(": "dynamic_13",
    ") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = timeStr,
                        onValueChange = { timeStr = it },
                        label = { Text(": "dynamic_19",
    "
                        )
                        days.forEach { (day, plan) ->
                            item { WeeklyPlanCard(day, plan) }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun WeeklyPlanCard(day: String, plan: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = day,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = plan,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.DarkGray,
                lineHeight = 22.sp // Improved spacing
            )
        }
    }
}

@Composable
fun GuidelineCard(title: String, reason: String, examples: List<String>) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color.DarkGray
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.Top) {
                Icon(
                    imageVector = Icons.Filled.Info,
                    contentDescription = ": "dynamic_1",
    " -> {
                        val days = listOf(
                            ": "dynamic_4",
    ") },
                                    modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
                                )
                                
                                OutlinedTextField(
                                    value = pickyEatingNotes,
                                    onValueChange = { 
                                        pickyEatingNotes = it
                                        savePreferences()
                                    },
                                    label = { Text(": "dynamic_14",
    ") }
        var quantity by remember { mutableStateOf(": "dynamic_11",
    ") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    
                    Text(": "dynamic_16",
    ") }

        AlertDialog(
            onDismissRequest = { showAddEntryDialog = false },
            title = { Text(": "dynamic_9",
    ") }
        var response by remember { mutableStateOf(": "dynamic_12",
    "$ageRange Diet": "dynamic_7",
    ") }
                        }
                    }
                    ": "dynamic_10",
    "
                        )
                        days.forEach { (day, plan) ->
                            item { WeeklyPlanCard(day, plan) }
                        }
                    }
                    ": "dynamic_0",
    "),
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.DarkGray,
                                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                                )
                            }
                        }
                    }

                }
            } else {
                // Diary & Settings
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    item {
                        Text(
                            text = ": "dynamic_20",
    "),
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(16.dp)
                            )
                        }
                    } else {
                        items(child.foodDiary.sortedByDescending { it.dateMillis }) { entry ->
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(
                                            text = entry.timeStr,
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                        Text(
                                            text = ": "dynamic_21",
    " -> Color(0xFF4CAF50)
                                                ": "dynamic_2",
    " to ": "dynamic_6",
    " specifically": "dynamic_5",
    ").forEach { option ->
                            FilterChip(
                                selected = response == option,
                                onClick = { response = option },
                                label = { Text(option) }
                            )
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    if (food.isNotBlank()) {
                        val newEntry = FoodDiaryEntry(
                            id = UUID.randomUUID().toString(),
                            timeStr = timeStr,
                            food = food,
                            quantity = quantity,
                            response = response
                        )
                        val updatedList = child.foodDiary + newEntry
                        onUpdateChild(child.copy(foodDiary = updatedList))
                        showAddEntryDialog = false
                    }
                }) {
                    Text(": "dynamic_22",
    ")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddEntryDialog = false }) {
                    Text(tr(": "dynamic_8",
    ",
                            ": "dynamic_23",
    ",
                                            style = MaterialTheme.typography.bodySmall,
                                            fontWeight = FontWeight.Bold,
                                            color = when (entry.response) {
                                                ": "dynamic_24",
    "1 Month": "dynamic_33",
    ",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(": "dynamic_26",
    "11 Months": "dynamic_36",
    "10 Months": "dynamic_34",
    ",
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}


    if (showAddEntryDialog) {
        var timeStr by remember { mutableStateOf(SimpleDateFormat(": "dynamic_25",
    ",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    
                    if (child.foodDiary.isEmpty()) {
                        item {
                            Text(
                                text = tr(": "dynamic_27",
    "0–6 months": "dynamic_32",
    "15 Months": "dynamic_40",
    "10 Weeks": "dynamic_35",
    "14 Weeks": "dynamic_39",
    "16-18 Months": "dynamic_41",
    "12 Months": "dynamic_37",
    ", modifier = Modifier.weight(1f))
                                    Switch(
                                        checked = isVegetarian,
                                        onCheckedChange = { 
                                            isVegetarian = it
                                            savePreferences()
                                        }
                                    )
                                }
                                
                                OutlinedTextField(
                                    value = regionalPreference,
                                    onValueChange = { 
                                        regionalPreference = it 
                                        savePreferences()
                                    },
                                    label = { Text(": "dynamic_31",
    ",
                    tint = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.size(20.dp).padding(top = 2.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = reason,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.DarkGray
                )
            }
            if (examples.isNotEmpty()) {
                Spacer(modifier = Modifier.height(12.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFF3F4F6), RoundedCornerShape(8.dp))
                        .padding(12.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        examples.forEach { example ->
                            Text(
                                text = ": "dynamic_28",
    ", Locale.getDefault()).format(Date())) }
        var food by remember { mutableStateOf(": "dynamic_29",
    "1–5 years": "dynamic_43",
    "1–3 meaningful words": "dynamic_42",
    "12 Months (1 Year)": "dynamic_38",
    ", modifier = Modifier.padding(top = 8.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                        listOf(": "dynamic_30",
    "2 Months": "dynamic_44",
    "2 Years (24 Months)": "dynamic_45",
    "24 Months": "dynamic_46",
    "4 Years": "dynamic_50",
    "3 Years": "dynamic_48",
    "3 Months": "dynamic_47",
    "5 Years": "dynamic_53",
    "6 Months": "dynamic_55",
    "4 Months": "dynamic_49",
    "6–12 months": "dynamic_57",
    "50+ words": "dynamic_54",
    "5 Months": "dynamic_52",
    "7 Months": "dynamic_58",
    "8 Months": "dynamic_59",
    "6 Weeks": "dynamic_56",
    "9 Months": "dynamic_60",
    "4-5 Years": "dynamic_51",
    "A baby under 1 year gets a sunburn, or if an older child has a sunburn with blisters, fever, or signs of heatstroke.": "dynamic_61",
    "A baby's skin is thin and highly susceptible to sunburns and long-term UV damage.": "dynamic_62",
    "Actionable Steps:": "dynamic_65",
    "A burn blisters, is larger than the child's palm, is on the face/hands/genitals, or appears charred/white.": "dynamic_63",
    "A major developmental milestone fostering independence and hygiene.": "dynamic_64",
    "Allergen Introduction": "dynamic_68",
    "Back": "dynamic_79",
    "Asks many questions": "dynamic_70",
    "As children become mobile, falls, bumps, and household hazards become major risks.": "dynamic_69",
    "Add Entry": "dynamic_67",
    "BCG": "dynamic_74",
    "Active children need plenty of fluids to stay hydrated, especially in warmer climates or during play.": "dynamic_66",
    "At Birth": "dynamic_71",
    "Babbles consonants (ba, da)": "dynamic_76",
    "Babbles": "dynamic_75",
    "Avoid honey (botulism risk), whole nuts (choking hazard), added salt/sugar (kidney strain), and cow's milk as a primary drink (hard to digest).": "dynamic_73",
    "Balances briefly on one foot": "dynamic_81",
    "Balanced Meal Suggestions": "dynamic_80",
    "Balances on one foot for 10 seconds": "dynamic_82",
    "At this age, babies rely exclusively on breast milk or infant formula. Feed on demand, usually 8-12 times a day.": "dynamic_72",
    "Baby-proof the house: anchor heavy furniture to walls, install stair gates, and pad sharp corners.": "dynamic_78",
    "Bears weight on legs when supported": "dynamic_83",
    "Begins cooing": "dynamic_84",
    "Baby skin burns much faster and at lower temperatures than adult skin.": "dynamic_77",
    "Begins crawling movements": "dynamic_85",
    "Begins problem solving": "dynamic_86",
    "Begins social smile": "dynamic_87",
    "Bluish Discoloration (Cyanosis)": "dynamic_91",
    "Birth (0 Month)": "dynamic_90",
    "Better neck control": "dynamic_88",
    "Better pincer grasp": "dynamic_89",
    "Breastfeeding Guidance": "dynamic_93",
    "Brings hands near face": "dynamic_96",
    "Breast milk provides optimal nutrition and antibodies. It is recommended exclusively for the first 6 months to support immunity and growth.": "dynamic_92",
    "Brings everything to mouth": "dynamic_95",
    "Calms when held": "dynamic_101",
    "Breathing remains extremely fast, nostrils flare continuously, or breathing pauses occur.": "dynamic_94",
    "Builds tower of 8–10 blocks": "dynamic_97",
    "Buttons clothes": "dynamic_99",
    "Burns Prevention": "dynamic_98",
    "Calcium-rich Foods": "dynamic_100",
    "Catches a bounced ball": "dynamic_102",
    "Check the color of the lips, tongue, and nail beds under good lighting.": "dynamic_105",
    "Check for obvious causes: hunger, dirty diaper, fever, or a 'hair tourniquet' (hair wrapped tightly around a toe/finger).": "dynamic_103",
    "Check if they react to pain (like a gentle pinch) or loud noises.": "dynamic_104",
    "Child is rescued from water, even if they seem okay (to rule out secondary/dry drowning), or if they are unresponsive.": "dynamic_111",
    "Child falls and loses consciousness, vomits repeatedly, acts lethargic, or has unequal pupil sizes.": "dynamic_109",
    "Child Not Feeding": "dynamic_108",
    "Check the depth and size of a burn. Blistering indicates a second-degree burn.": "dynamic_106",
    "Check toys frequently for broken pieces, exposed batteries, or peeling paint.": "dynamic_107",
    "Child is experiencing severe constipation, pain during urination, or regresses significantly after being trained.": "dynamic_110",
    "Claps hands": "dynamic_116",
    "Child swallows a small battery (button battery) or a magnet—this is an extreme medical emergency.": "dynamic_112",
    "Cognitive": "dynamic_119",
    "Child was fine and suddenly becomes extremely weak, pale, or limp.": "dynamic_113",
    "Climbs stairs alternating feet": "dynamic_117",
    "Choking is a leading cause of accidental injury in toddlers because they explore the world with their mouths.": "dynamic_115",
    "Climbs stairs with support": "dynamic_118",
    "Consult Doctor When:": "dynamic_121",
    "Choking Prevention": "dynamic_114",
    "Complementary Feeding Introduction": "dynamic_120",
    "Cooperative play": "dynamic_122",
    "Coos frequently": "dynamic_123",
    "Copies actions": "dynamic_126",
    "Copies a square": "dynamic_125",
    "Copies a circle": "dynamic_124",
    "Crawls": "dynamic_131",
    "Counts to 10": "dynamic_128",
    "Count breaths per minute. Observe if the chest caves in deeply or if the child cannot cry or speak.": "dynamic_127",
    "Counts to 20 or more": "dynamic_129",
    "Cruises holding furniture": "dynamic_134",
    "Counts to 3–5": "dynamic_130",
    "Crawls well": "dynamic_132",
    "Cut round foods (grapes, hotdogs) lengthwise. Keep coins, marbles, and small parts completely out of reach.": "dynamic_137",
    "DTwP / DTaP": "dynamic_138",
    "Cuby Alert System": "dynamic_135",
    "Cries to communicate needs": "dynamic_133",
    "DTwP / DTaP Booster 2": "dynamic_140",
    "Curious toddlers can easily ingest toxic household cleaning supplies, cosmetics, or medications.": "dynamic_136",
    "DTwP / DTaP Booster 1": "dynamic_139",
    "Diary & Settings": "dynamic_142",
    "Demonstrates empathy and helps others": "dynamic_141",
    "Different cries for different needs": "dynamic_143",
    "Difficult to wake up, unusually sleepy, not making eye contact.": "dynamic_144",
    "Draws a person with several body parts": "dynamic_146",
    "Difficulty Breathing": "dynamic_145",
    "Dry mouth, no tears when crying, sunken eyes, no wet diaper for >6 hours.": "dynamic_150",
    "Draws detailed people": "dynamic_147",
    "Enjoys interaction": "dynamic_151",
    "Drowning can happen quickly and silently in as little as 1-2 inches of water.": "dynamic_149",
    "Dress in light clothing, offer plenty of fluids, use a lukewarm sponge bath (never cold).": "dynamic_148",
    "Ensure all caregivers strictly wash hands, especially during flu and RSV seasons.": "dynamic_154",
    "Enjoys peek-a-boo": "dynamic_153",
    "Enjoys mirror play": "dynamic_152",
    "Ensure toys are age-appropriate, larger than a toilet paper roll tube (to prevent choking), and have no loose strings/buttons.": "dynamic_157",
    "Ensure they are well-fed and hydrated. Try to engage them with loud sounds or light.": "dynamic_156",
    "Essential for building strong bones and teeth during these rapid growth years.": "dynamic_158",
    "Ensure frequent feeding (breastmilk or formula) to help excrete bilirubin. Follow doctor's advice on indirect sunlight exposure.": "dynamic_155",
    "Evidence-based pediatric medical guidance to keep your baby safe, healthy, and thriving.": "dynamic_159",
    "Example: A tiny bit of peanut butter thinned with water, or a small taste of scrambled egg.": "dynamic_160",
    "Example: Do not add salt to baby's purees.": "dynamic_162",
    "Example: Consult pediatrician if baby seems unusually fussy during feeds.": "dynamic_161",
    "Example: Follow package instructions for safe preparation.": "dynamic_163",
    "Example: Mark each diaper change in the app.": "dynamic_164",
    "Example: Mashed lentils (dal), pureed meats, or iron-fortified cereals.": "dynamic_165",
    "Example: Note the start and end time of each feed.": "dynamic_166",
    "Example: Offer plain water between meals instead of sugary juices.": "dynamic_167",
    "Example: Paneer (cottage cheese), eggs, lentils (dal), chickpeas (chole), or lean chicken.": "dynamic_169",
    "Example: Pureed sweet potato -> Mashed banana -> Soft avocado chunks.": "dynamic_170",
    "Example: Whole wheat chapati/roti with a side of mixed vegetable curry (sabzi).": "dynamic_173",
    "Example: Single-grain infant cereals, pureed fruits or vegetables.": "dynamic_171",
    "Example: On-demand feeding every 2-3 hours.": "dynamic_168",
    "Example: Sliced apples, cooked carrots, spinach (palak), or a side of fresh cucumber.": "dynamic_172",
    "Explores cause and effect": "dynamic_177",
    "Example: Yogurt (dahi), milk, cheese, ragi (finger millet), or fortified plant milks.": "dynamic_174",
    "Expect about 6 or more wet diapers a day. This is a reliable indicator that your baby is well-hydrated and consuming enough milk.": "dynamic_176",
    "Excessive screentime can delay speech, impair cognitive development, and disrupt sleep.": "dynamic_175",
    "Explores surroundings": "dynamic_179",
    "Explores objects": "dynamic_178",
    "Expresses emotions appropriately": "dynamic_180",
    "Feeding Concerns": "dynamic_182",
    "Feeding Frequency Tracking": "dynamic_183",
    "Fast breathing, flaring nostrils, grunting sounds, chest pulling in.": "dynamic_181",
    "Fever lasts >24 hours (under 2 years) or >72 hours (older), or is accompanied by extreme lethargy or a stiff neck.": "dynamic_184",
    "First Polio booster.": "dynamic_187",
    "Finds hidden objects": "dynamic_185",
    "Follows game rules": "dynamic_189",
    "Fine Motor": "dynamic_186",
    "Follows moving objects": "dynamic_190",
    "First booster for Diphtheria, Tetanus, Pertussis.": "dynamic_188",
    "Follows two-step commands": "dynamic_192",
    "Follows one-step commands": "dynamic_191",
    "Food Texture Progression": "dynamic_193",
    "Friday": "dynamic_196",
    "Foods to Avoid": "dynamic_194",
    "Fruits and Vegetables": "dynamic_197",
    "Formula Feeding": "dynamic_195",
    "Gives objects to others": "dynamic_198",
    "Gradually move from purees to mashed and then soft finger foods. This strengthens oral muscles necessary for speech and eating.": "dynamic_199",
    "Grasps a finger placed in palm": "dynamic_200",
    "Grasps objects voluntarily": "dynamic_201",
    "Guidelines": "dynamic_203",
    "Gross Motor": "dynamic_202",
    "Guidelines for $ageRange": "dynamic_204",
    "Hands mostly closed": "dynamic_207",
    "Hepatitis B": "dynamic_210",
    "Hands open more often": "dynamic_208",
    "Hand Washing": "dynamic_206",
    "Haemophilus influenzae type B Booster.": "dynamic_205",
    "Hib Booster": "dynamic_212",
    "Hepatitis A": "dynamic_209",
    "Hives all over, swelling of face/lips, wheezing, sudden vomiting.": "dynamic_213",
    "Hib": "dynamic_211",
    "Holds head steadily": "dynamic_214",
    "Holds head up longer": "dynamic_215",
    "Hops on one foot": "dynamic_219",
    "Home Remedies (Before Severe):": "dynamic_218",
    "Holds toys with both hands": "dynamic_217",
    "Holds rattle briefly": "dynamic_216",
    "How to Detect Severity:": "dynamic_220",
    "Hydration": "dynamic_221",
    "IPV": "dynamic_222",
    "IPV Booster 1": "dynamic_223",
    "Identifies several colors": "dynamic_225",
    "If the child cannot breathe or turns blue, call emergency services immediately and perform infant CPR/back blows.": "dynamic_226",
    "IPV Booster 2": "dynamic_224",
    "Imitates adults": "dynamic_228",
    "If using formula, iron-fortified formula is recommended as a safe alternative that provides necessary nutrients for early development.": "dynamic_227",
    "Inactivated Polio Vaccine.": "dynamic_229",
    "Incorporate familiar, culturally rich foods that are naturally nutritious.": "dynamic_231",
    "Inconsolable Crying (Colic or Pain)": "dynamic_230",
    "Influenza": "dynamic_232",
    "Info": "dynamic_233",
    "Introduce common allergens one at a time and wait a few days between new foods. Early introduction may help prevent food allergies.": "dynamic_236",
    "Injury Prevention": "dynamic_234",
    "Intense, prolonged crying that sounds like screaming. Drawing legs up to the belly.": "dynamic_235",
    "Jerking movements, stiffness, staring spells, loss of consciousness.": "dynamic_239",
    "Iron-rich Foods": "dynamic_237",
    "It is their first seizure, lasts >5 minutes, or they have difficulty breathing afterward.": "dynamic_238",
    "Jumps with both feet": "dynamic_241",
    "Jumps rope (beginning)": "dynamic_240",
    "Keep all medicines, laundry pods, and cleaning supplies locked in high cabinets. Keep Poison Control's number saved.": "dynamic_242",
    "Keep babies <6 months in the shade. Dress in lightweight, long sleeves and a wide-brimmed hat. Use baby-safe sunscreen (zinc oxide) for >6 months.": "dynamic_243",
    "Kicks and throws a ball": "dynamic_245",
    "Keep the child upright, use a cool-mist humidifier, clear nasal passages with saline drops.": "dynamic_244",
    "Knows full name and age": "dynamic_246",
    "Language": "dynamic_247",
    "Laughs": "dynamic_248",
    "Laughs loudly": "dynamic_249",
    "Lips, tongue, or skin turning blue or grayish.": "dynamic_252",
    "Lifts head briefly during tummy time": "dynamic_251",
    "Lay the child flat, raise their legs slightly if pale, and check breathing.": "dynamic_250",
    "Look for white spots or brown discoloration on the teeth, which indicate early decay.": "dynamic_255",
    "Look for open pill bottles, strange chemical smells on breath, sudden drooling, or unexplained vomiting.": "dynamic_253",
    "Looks for moving objects": "dynamic_257",
    "MANDATORY": "dynamic_258",
    "Look for readiness signs (staying dry for 2 hours, showing interest). Keep it positive, use a small potty, and offer praise.": "dynamic_254",
    "MMR Booster": "dynamic_260",
    "Look out for excessive spitting up, refusal to eat, or poor weight gain. Early detection helps address underlying issues quickly.": "dynamic_256",
    "MMR": "dynamic_259",
    "Makes eye contact for a few seconds": "dynamic_261",
    "Makes vowel sounds": "dynamic_262",
    "Matches shapes and colors": "dynamic_263",
    "Mature pincer grasp develops": "dynamic_264",
    "Meals should include a mix of carbohydrates, protein, and healthy fats to provide sustained energy for active toddlers.": "dynamic_265",
    "Moves arms and legs equally": "dynamic_267",
    "Never leave a child unattended in the bathtub. Drain tubs immediately. Put fences around pools.": "dynamic_271",
    "Monday": "dynamic_266",
    "Names familiar objects": "dynamic_268",
    "Neat pincer grasp": "dynamic_270",
    "Natural iron stores deplete around 6 months. Iron is crucial for brain development and red blood cell formation.": "dynamic_269",
    "None. This is an immediate sign of lack of oxygen.": "dynamic_273",
    "Newborns typically eat 8-12 times a day. Tracking helps ensure they are getting enough milk for steady weight gain.": "dynamic_272",
    "Not generally applicable, but consult a doctor if poor hygiene leads to persistent skin infections or frequent illnesses.": "dynamic_275",
    "North Indian Example: Soft paratha (no chili) with a dollop of ghee and yogurt.": "dynamic_274",
    "Note: This app does not attempt to diagnose your child. This information is for educational purposes only. When in doubt, always consult a doctor immediately.": "dynamic_276",
    "OPTIONAL": "dynamic_278",
    "Nutrition & Diet": "dynamic_277",
    "Observe if the seizure lasts longer than 3-5 minutes or if breathing is affected.": "dynamic_280",
    "Observe sudden changes in skin color, breathing rate, and consciousness.": "dynamic_281",
    "OPV": "dynamic_279",
    "Observe the height of falls. Falls from >3 feet or onto hard surfaces carry higher risks for head injuries.": "dynamic_282",
    "Offer frequent, small sips of oral rehydration solution (ORS) or breastmilk.": "dynamic_283",
    "Offer smaller, more frequent feeds. Try a different feeding position or a calm environment.": "dynamic_284",
    "Oral Health": "dynamic_286",
    "Opens hands occasionally": "dynamic_285",
    "PCV Booster": "dynamic_288",
    "Pays attention to faces": "dynamic_291",
    "PCV": "dynamic_287",
    "Parallel play": "dynamic_289",
    "Passes objects hand to hand": "dynamic_290",
    "Pediatric Guidelines & Safety Advice": "dynamic_292",
    "Pinch the skin on the stomach; if it doesn't bounce back quickly, they are very dehydrated.": "dynamic_294",
    "Persistent High Fever": "dynamic_293",
    "Place child on their side in a safe, soft place. Do NOT put anything in their mouth. Time the seizure.": "dynamic_295",
    "Places objects into containers": "dynamic_296",
    "Plays interactive games": "dynamic_298",
    "Plays simple pretend games": "dynamic_299",
    "Plays cooperatively with peers": "dynamic_297",
    "Poison Prevention": "dynamic_302",
    "Pneumococcal Booster.": "dynamic_300",
    "Points with finger": "dynamic_301",
    "Press gently on the baby's forehead or nose; if the skin looks yellow where you pressed, it's jaundice. Observe if yellowing spreads downward.": "dynamic_303",
    "Pretend play becomes imaginative": "dynamic_304",
    "Prevents Diphtheria, Tetanus, and Pertussis (Whooping Cough).": "dynamic_306",
    "Prevents Chickenpox.": "dynamic_305",
    "Prevents Diphtheria, Tetanus, and Pertussis.": "dynamic_307",
    "Prevents Hepatitis A liver disease.": "dynamic_309",
    "Prevents Haemophilus influenzae type B.": "dynamic_308",
    "Prevents Hepatitis B infection.": "dynamic_310",
    "Prevents Measles, Mumps, and Rubella.": "dynamic_311",
    "Prevents Pneumococcal disease (Pneumonia, Meningitis).": "dynamic_312",
    "Prevents Pneumococcal disease.": "dynamic_313",
    "Prevents Typhoid fever.": "dynamic_315",
    "Prevents Polio (Oral Polio Vaccine 0 dose).": "dynamic_314",
    "Prevents choking, strangulation, and exposure to toxic materials (like lead or bad plastics).": "dynamic_316",
    "Prevents seasonal flu (Influenza). Recommended annually.": "dynamic_317",
    "Prevents tooth decay, establishes a healthy oral microbiome, and promotes proper jaw development.": "dynamic_320",
    "Prevents severe forms of Tuberculosis.": "dynamic_319",
    "Prevents severe diarrhea caused by Rotavirus.": "dynamic_318",
    "Protects children from abduction or abuse by establishing boundaries early on.": "dynamic_321",
    "Protein & Iron-rich Foods": "dynamic_322",
    "Protein builds tissues, and iron supports cognitive development and prevents anemia.": "dynamic_323",
    "Provide essential vitamins, minerals, and fiber to support immunity and healthy digestion.": "dynamic_324",
    "Pulls to stand": "dynamic_325",
    "Pushes up on forearms": "dynamic_326",
    "Puts objects into containers": "dynamic_327",
    "Recognizes caregivers": "dynamic_330",
    "Reaches accurately": "dynamic_329",
    "Rakes small objects": "dynamic_328",
    "Recognizes familiar people": "dynamic_332",
    "Recognizes familiar faces": "dynamic_331",
    "Recognizes many letters": "dynamic_333",
    "Red-Flag Warning Signs": "dynamic_335",
    "Recognizes mother's voice and smell": "dynamic_334",
    "Refusing breast, bottle, or all solid foods for multiple consecutive feeds.": "dynamic_336",
    "Regional Indian Food Ideas": "dynamic_337",
    "Responds to name": "dynamic_339",
    "Remove the allergen. If prescribed an EpiPen for a known allergy, use it immediately.": "dynamic_338",
    "Responds to tone": "dynamic_340",
    "Responds to voices": "dynamic_341",
    "Rides a tricycle": "dynamic_342",
    "Rotavirus": "dynamic_346",
    "Rolls easily": "dynamic_344",
    "Rolls both directions": "dynamic_343",
    "Rolls tummy to back": "dynamic_345",
    "Runs well": "dynamic_347",
    "Says repetitive sounds (mamama, bababa)": "dynamic_350",
    "Safe Toys": "dynamic_348",
    "Saturday": "dynamic_349",
    "Searches for dropped toys": "dynamic_353",
    "Screentime": "dynamic_351",
    "Scribbles circles": "dynamic_352",
    "Second booster for Diphtheria, Tetanus, Pertussis.": "dynamic_355",
    "Second Polio booster.": "dynamic_354",
    "Second dose for Chickenpox.": "dynamic_356",
    "Seek IMMEDIATE emergency care the moment you notice bluish discoloration.": "dynamic_357",
    "Seek immediate medical care.": "dynamic_360",
    "Seek IMMEDIATE emergency care. Do not wait.": "dynamic_358",
    "Seizures": "dynamic_361",
    "Seek IMMEDIATE emergency care. This is a critical warning sign.": "dynamic_359",
    "Set water heater to 120°F (49°C) max. Never hold hot drinks while holding a baby. Turn pot handles inward on the stove.": "dynamic_362",
    "Severe Allergic Reaction (Anaphylaxis)": "dynamic_363",
    "Severe Dehydration": "dynamic_364",
    "Severe Jaundice": "dynamic_365",
    "Sits independently": "dynamic_369",
    "Shows affection": "dynamic_367",
    "Shares toys": "dynamic_366",
    "Shows increasing independence": "dynamic_368",
    "Skips": "dynamic_372",
    "Sits with little support": "dynamic_370",
    "Sits with support": "dynamic_371",
    "Smiles at familiar people": "dynamic_373",
    "Social & Emotional": "dynamic_375",
    "Solves simple puzzles independently": "dynamic_376",
    "Smiles responsively": "dynamic_374",
    "South Indian Example: Soft idli with mild sambar, or Pongal.": "dynamic_378",
    "Sorts objects by color and shape": "dynamic_377",
    "Speaks in complete sentences": "dynamic_381",
    "Speaks clearly": "dynamic_379",
    "Speaks in 3–4 word sentences": "dynamic_380",
    "Speech mostly understandable": "dynamic_382",
    "Squeals": "dynamic_384",
    "Squats and stands": "dynamic_383",
    "Stands briefly": "dynamic_387",
    "Start offering solid foods while continuing breast milk or formula. This helps develop chewing skills and introduces new tastes.": "dynamic_388",
    "Stacks 6–8 blocks": "dynamic_385",
    "Stacks two blocks": "dynamic_386",
    "Startles to loud sounds": "dynamic_389",
    "Stranger Safety": "dynamic_390",
    "Stranger anxiety increases": "dynamic_391",
    "Strong primitive reflexes (Moro, rooting, sucking, grasp)": "dynamic_393",
    "Stranger awareness begins": "dynamic_392",
    "Sunday": "dynamic_396",
    "Sudden Deterioration": "dynamic_394",
    "Swaddle the baby, use white noise, gently rock or sway, offer a pacifier, or try skin-to-skin contact.": "dynamic_397",
    "Sun Protection": "dynamic_395",
    "Swipes at toys": "dynamic_398",
    "Tap to view guidelines...": "dynamic_401",
    "THIS IS AN EMERGENCY": "dynamic_399",
    "Teach body autonomy (nobody has to hug if they don't want to). For older toddlers, teach 'Tricky People' rather than 'Stranger Danger'.": "dynamic_402",
    "Tap to view details...": "dynamic_400",
    "Temperature >100.4°F (infants under 3 months) or >102.2°F (older children).": "dynamic_405",
    "The most effective way to prevent the spread of germs, viruses, and gastrointestinal infections.": "dynamic_408",
    "The child is unable to keep fluids down, is extremely lethargic, or has sunken eyes/fontanelle.": "dynamic_406",
    "The crying lasts for more than 2 hours non-stop, is accompanied by a fever, vomiting, or nothing soothes them.": "dynamic_407",
    "Tells detailed stories": "dynamic_403",
    "Tells simple stories": "dynamic_404",
    "The yellowing spreads below the belly button, the baby is hard to wake up, or is feeding poorly.": "dynamic_409",
    "They cannot be woken up, or are completely unresponsive to their environment.": "dynamic_410",
    "They refuse all liquids for more than 6-8 hours or show signs of dehydration.": "dynamic_411",
    "Third dose for Measles, Mumps, Rubella.": "dynamic_412",
    "Track wet diapers. Less than 3-4 wet diapers in 24 hours indicates they are not getting enough.": "dynamic_415",
    "Toilet Training": "dynamic_414",
    "Thursday": "dynamic_413",
    "Tracks objects about 20–30 cm": "dynamic_416",
    "Transfers objects hand to hand": "dynamic_417",
    "Transfers toys between hands (beginning)": "dynamic_418",
    "Tuesday": "dynamic_419",
    "Turns book pages": "dynamic_420",
    "Turns head from side to side": "dynamic_421",
    "Turns head to one side when lying on tummy": "dynamic_422",
    "Typhoid Conjugate": "dynamic_424",
    "Understands rules and fairness": "dynamic_427",
    "Understands object permanence": "dynamic_426",
    "Two-word sentences": "dynamic_423",
    "Understands complex instructions": "dynamic_425",
    "Understands simple commands": "dynamic_428",
    "Understands simple instructions": "dynamic_429",
    "Understands simple sequencing": "dynamic_430",
    "Understands size differences": "dynamic_431",
    "Understands time concepts like morning and night": "dynamic_433",
    "Understands taking turns": "dynamic_432",
    "Use a reliable digital thermometer. Monitor if the fever persists despite fever-reducing medicine.": "dynamic_435",
    "Unresponsiveness / Lethargy": "dynamic_434",
    "Uses child-safe scissors": "dynamic_436",
    "Uses immature pincer grasp": "dynamic_439",
    "Uses fork, spoon, and knife appropriately": "dynamic_437",
    "Uses gestures": "dynamic_438",
    "Uses spoon independently": "dynamic_440",
    "Varicella": "dynamic_441",
    "Walks independently or with minimal support": "dynamic_444",
    "Walks downstairs alternating feet": "dynamic_442",
    "Walks holding furniture": "dynamic_443",
    "Walks with one-hand support": "dynamic_445",
    "Warning": "dynamic_446",
    "Watch closely around any water source. Drowning does not look like the movies; it is often silent without splashing.": "dynamic_448",
    "Wash hands with soap and water for 20 seconds before feeding, after diaper changes, and after coming home.": "dynamic_447",
    "Watch for behavioral issues, sleep disturbances, or delayed speech resulting from high screen exposure.": "dynamic_450",
    "Watch for sudden coughing, gagging, or inability to make sounds (which indicates a full blockage).": "dynamic_453",
    "Watch for adults crossing boundaries with your child, or your child acting overly fearful around a specific person.": "dynamic_449",
    "Watch for chronic constipation or withholding, which makes potty training extremely difficult and painful.": "dynamic_451",
    "Watch for redness, blistering, or the baby acting unusually fussy or lethargic after sun exposure.": "dynamic_452",
    "Watches faces briefly": "dynamic_455",
    "Watch for sudden swelling of the tongue or throat, or sudden severe difficulty breathing.": "dynamic_454",
    "Waves bye-bye": "dynamic_458",
    "Watches own hands": "dynamic_456",
    "Waves, claps": "dynamic_459",
    "Water Safety": "dynamic_457",
    "Wednesday": "dynamic_460",
    "Weekly Meal Plan": "dynamic_461",
    "Weekly Meal Plan for $ageRange": "dynamic_462",
    "West/East Example: Mild khichdi (rice and lentil porridge) with vegetables.": "dynamic_463",
    "What to Look Out For:": "dynamic_465",
    "Wet Diaper Tracking": "dynamic_464",
    "Wipe infant gums with a damp cloth. Once teeth erupt, brush twice a day with a smear of fluoride toothpaste.": "dynamic_466",
    "Writes some letters and numbers": "dynamic_467",
    "Yellowing of the skin and whites of the eyes.": "dynamic_468",
    "You suspect poisoning. Call Poison Control immediately before inducing vomiting or giving fluids.": "dynamic_471",
    "You spot cavities, signs of decay, or the child injures their mouth/teeth in a fall. First dental visit by age 1.": "dynamic_469",
    "You suspect any form of abuse, physical or emotional, which requires professional psychological and medical help.": "dynamic_470",
    "You suspect your child has a speech delay or behavioral issue that requires professional evaluation.": "dynamic_472",
    "Zero screentime under 18 months (except video chatting). 1 hour max of high-quality programming for ages 2-5.": "dynamic_473",
}

files = [
    r"c:\Users\ABDUL MANNAN\Downloads\CubyCare-main\CubyCare-main\app\src\main\java\co\csedge\cubycare\ui\dashboard\CubyAlertScreen.kt",
    r"c:\Users\ABDUL MANNAN\Downloads\CubyCare-main\CubyCare-main\app\src\main\java\co\csedge\cubycare\ui\dashboard\CubyParentingScreen.kt",
    r"c:\Users\ABDUL MANNAN\Downloads\CubyCare-main\CubyCare-main\app\src\main\java\co\csedge\cubycare\data\repository\VaccineScheduleProvider.kt",
    r"c:\Users\ABDUL MANNAN\Downloads\CubyCare-main\CubyCare-main\app\src\main\java\co\csedge\cubycare\data\repository\MilestoneProvider.kt",
    r"c:\Users\ABDUL MANNAN\Downloads\CubyCare-main\CubyCare-main\app\src\main\java\co\csedge\cubycare\ui\dashboard\NutritionHomeScreen.kt",
    r"c:\Users\ABDUL MANNAN\Downloads\CubyCare-main\CubyCare-main\app\src\main\java\co\csedge\cubycare\ui\dashboard\NutritionAgeDetailScreen.kt",
]


for filepath in files:
    with open(filepath, 'r', encoding='utf-8') as f:
        content = f.read()
    for eng, key in replacements.items():
        search_str = '"' + eng + '"'
        replace_str = 'tr("' + key + '")'
        content = content.replace(search_str, replace_str)
        
    if "import co.csedge.cubycare.utils.tr" not in content:
        last_import = content.rfind("import ")
        if last_import != -1:
            eol = content.find("\n", last_import)
            content = content[:eol+1] + "import co.csedge.cubycare.utils.tr\n" + content[eol+1:]
            
    with open(filepath, 'w', encoding='utf-8') as f:
        f.write(content)
print("Applied dynamic keys.")
