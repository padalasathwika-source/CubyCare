package co.csedge.cubycare.ui.dashboard

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import co.csedge.cubycare.utils.tr

data class EmergencyCondition(
    val name: String,
    val symptoms: String,
    val homeRemedies: String,
    val detectSeverity: String,
    val consultDoctorWhen: String
)

val emergencyConditions: List<EmergencyCondition>
    @Composable get() = listOf(
    EmergencyCondition(
        name = "High Fever (Above 100.4°F / 38°C)",
        symptoms = "Hot forehead, flushed cheeks, sweating or shivering, unusual crying.",
        homeRemedies = "Dress baby in light cotton clothes, give a lukewarm water sponge bath, ensure adequate hydration with breastmilk/water, administer prescribed pediatric paracetamol.",
        detectSeverity = "Temperature > 102°F (38.9°C), or any fever in infants under 3 months old.",
        consultDoctorWhen = "Fever lasts > 24 hours, infant is under 3 months, or child becomes unusually unresponsive or lethargic."
    ),
    EmergencyCondition(
        name = "Difficulty Breathing / Wheezing",
        symptoms = "Rapid shallow breathing, chest sucking in deeply under ribs (retractions), nostril flaring, grunting sounds.",
        homeRemedies = "Keep baby upright, run a cool-mist humidifier, sit in a steamy bathroom for 10 minutes.",
        detectSeverity = "Chest wall collapsing inward during inhalation, bluish skin around lips or nail beds.",
        consultDoctorWhen = "SEEK IMMEDIATE EMERGENCY MEDICAL CARE if child is struggling to breathe or turning pale/blue."
    ),
    EmergencyCondition(
        name = "Dehydration (Diarrhea & Vomiting)",
        symptoms = "Frequent watery stools, repeated vomiting, dry mouth, no wet diaper in 6 hours, crying without tears.",
        homeRemedies = "Offer Oral Rehydration Solution (ORS) in small frequent sips, continue frequent breastfeeding.",
        detectSeverity = "Sunken eyes, extreme drowsiness, cold hands/feet, dry tongue.",
        consultDoctorWhen = "Inability to keep liquids down for 8 hours, blood in stool, or extreme lethargy."
    ),
    EmergencyCondition(
        name = "Severe Allergic Reaction (Hives / Swelling)",
        symptoms = "Sudden raised red itchy skin patches, swollen eyelids/lips, sneezing, or facial rash after new food or sting.",
        homeRemedies = "Apply a cool damp cloth to itchy areas, keep child calm, remove any suspected allergen.",
        detectSeverity = "Swelling of tongue, throat hoarseness, or sudden breathing difficulty.",
        consultDoctorWhen = "SEEK IMMEDIATE EMERGENCY CARE if facial swelling or breathing difficulty develops (Anaphylaxis risk)."
    ),
    EmergencyCondition(
        name = "Accidental Fall & Head Injury",
        symptoms = "Crying immediately after fall, minor localized bump or small bruise on forehead.",
        homeRemedies = "Apply ice pack wrapped in a washcloth to bump for 10 minutes, comfort and observe child closely.",
        detectSeverity = "Vomiting after fall, loss of consciousness, unequal pupil sizes, fluid leaking from nose/ears.",
        consultDoctorWhen = "Any loss of consciousness, repeated vomiting, or if baby fell from higher than 3 feet."
    ),
    EmergencyCondition(
        name = "Croup (Barking Cough & Stridor)",
        symptoms = "Harsh seal-like barking cough, noisy breathing when inhaling (stridor), raspy voice.",
        homeRemedies = "Sit in steamy bathroom for 10-15 minutes, expose to cool night air for 5 minutes, keep child calm.",
        detectSeverity = "High-pitched whistling sound when breathing quietly at rest, blue skin tone.",
        consultDoctorWhen = "Struggle to breathe, stridor heard while resting, or skin pulling tight around ribs."
    ),
    EmergencyCondition(
        name = "Constipation & Abdominal Pain",
        symptoms = "Hard pellet-like stools, crying or straining during bowel movements, firm bloated tummy.",
        homeRemedies = "Gentle clockwise tummy massage, leg bicycle exercises, warm bath, prune/pear puree for older infants.",
        detectSeverity = "Vomiting green/yellow bile, severe tummy swelling, intense abdominal tenderness.",
        consultDoctorWhen = "Blood in stool, persistent vomiting, or unsoothable abdominal pain."
    ),
    EmergencyCondition(
        name = "Ear Infection (Otitis Media)",
        symptoms = "Tugging or pulling at ears, unexplainable crying when lying flat, low fever, fluid draining from ear.",
        homeRemedies = "Hold warm washcloth over ear, elevate head slightly during sleep, keep ear dry.",
        detectSeverity = "Pus or bloody discharge draining from ear canal, high fever > 102°F.",
        consultDoctorWhen = "Fluid draining from ear canal, severe ear pain lasting > 24 hours, or high fever."
    ),
    EmergencyCondition(
        name = "Unsoothable Crying & Colic",
        symptoms = "Predictable late afternoon/evening crying episodes lasting > 3 hours, clenching fists, pulling legs up.",
        homeRemedies = "Swaddling, gentle rhythmic rocking, white noise machine, thorough burping after feeds.",
        detectSeverity = "Crying accompanied by fever, vomiting, or abnormal abdominal firmness.",
        consultDoctorWhen = "Crying pitch changes drastically, baby refuses feeding, or exhibits fever/lethargy."
    ),
    EmergencyCondition(
        name = "Febrile Seizure (Convulsions)",
        symptoms = "Sudden muscle twitching, rolling eyes, body stiffening, loss of consciousness during rapid fever spike.",
        homeRemedies = "Place baby on side on soft floor, clear surrounding objects, do NOT place anything in mouth.",
        detectSeverity = "Seizure lasting longer than 5 minutes, or difficulty breathing after seizure ends.",
        consultDoctorWhen = "SEEK IMMEDIATE EMERGENCY MEDICAL EVALUATION after any seizure episode."
    )
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CubyAlertScreen(
    onBack: () -> Unit,
    onOpenNearby: () -> Unit = {}
) {
    val conditionsList = emergencyConditions

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Emergency Symptoms (CubyAlert)") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFFFEBEE),
                    titleContentColor = Color(0xFFC62828),
                    navigationIconContentColor = Color(0xFFC62828)
                )
            )
        },
        containerColor = Color.Transparent
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize()) {
            Image(
                painter = painterResource(id = co.csedge.cubycare.R.drawable.mother_baby_health_bg),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize(),
                alpha = 0.20f
            )

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentPadding = PaddingValues(bottom = 32.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // High Priority Disclaimer Block
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFCDD2)),
                        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Warning,
                                contentDescription = "Warning",
                                tint = Color(0xFFD32F2F),
                                modifier = Modifier.size(48.dp)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "CubyAlert Emergency Symptoms",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFB71C1C)
                            )
                            Text(
                                text = "Immediate Pediatric Guidance",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFFC62828)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Recognize critical red-flag symptoms in infants and young children, apply immediate safe first-aid remedies, and find nearby emergency care.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color(0xFF5D4037),
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(14.dp))
                            
                            val context = androidx.compose.ui.platform.LocalContext.current
                            Column(verticalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
                                // Option 1: Find Nearby Children's Hospital / Pediatrician (Google Maps)
                                Button(
                                    onClick = { co.csedge.cubycare.utils.HospitalUtils.openNearbyHospitals(context) },
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F)),
                                    shape = RoundedCornerShape(12.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Icon(Icons.Filled.Place, contentDescription = null, tint = Color.White)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("1️⃣ Find Nearby Children's Hospitals in Google Maps 🗺️", fontWeight = FontWeight.Bold, color = Color.White)
                                }

                                // Option 2: Second Option - Malla Reddy Narayana Hospital Pediatrics & Neonatology
                                OutlinedButton(
                                    onClick = {
                                        co.csedge.cubycare.utils.HospitalUtils.openPediatricsSpecialty(context)
                                    },
                                    shape = RoundedCornerShape(12.dp),
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFB71C1C))
                                ) {
                                    Text("2️⃣ Visit Malla Reddy Narayana Pediatrics & Neonatology", fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }

                item {
                    Text(
                        text = "Common Infant & Pediatric Emergency Symptoms",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.DarkGray,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                    )
                }

                items(conditionsList) { condition ->
                    Box(modifier = Modifier.padding(horizontal = 16.dp)) {
                        ConditionCard(condition)
                    }
                }
            }
        }
    }
}

@Composable
fun ConditionCard(condition: EmergencyCondition) {
    var isExpanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { isExpanded = !isExpanded },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = condition.name,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFC62828)
            )
            
            androidx.compose.animation.AnimatedVisibility(
                visible = isExpanded,
                enter = androidx.compose.animation.fadeIn(animationSpec = androidx.compose.animation.core.tween(200, easing = androidx.compose.animation.core.FastOutSlowInEasing)) + androidx.compose.animation.expandVertically(animationSpec = androidx.compose.animation.core.tween(200, easing = androidx.compose.animation.core.FastOutSlowInEasing)),
                exit = androidx.compose.animation.fadeOut(animationSpec = androidx.compose.animation.core.tween(180, easing = androidx.compose.animation.core.FastOutLinearInEasing)) + androidx.compose.animation.shrinkVertically(animationSpec = androidx.compose.animation.core.tween(180, easing = androidx.compose.animation.core.FastOutLinearInEasing))
            ) {
                Column(modifier = Modifier.padding(top = 12.dp)) {
                    Text(
                        text = "Symptoms:",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = condition.symptoms,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    Text(
                        text = "First Aid & Home Care:",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = condition.homeRemedies,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    Text(
                        text = "Detecting Severity:",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = condition.detectSeverity,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    Text(
                        text = "Consult Doctor / Hospital When:",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFD32F2F)
                    )
                    Text(
                        text = condition.consultDoctorWhen,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            if (!isExpanded) {
                Text(
                    text = "Tap card to view symptoms & remedies ↓",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
    }
}
