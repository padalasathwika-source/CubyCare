package co.csedge.cubycare.ui.dashboard

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import co.csedge.cubycare.utils.tr

data class ParentingTopic(
    val name: String,
    val reason: String,
    val actionableSteps: String,
    val severityOrLookout: String,
    val consultDoctorWhen: String
)

val parentingTopics: List<ParentingTopic>
    @Composable get() = listOf(
    ParentingTopic(
        name = "Establishing Infant Sleep Routines",
        reason = "Consistent bedtime routines help babies distinguish day from night and release melatonin for sound, uninterrupted sleep.",
        actionableSteps = "Maintain a 20-minute wind-down routine (dim lights, warm bath, gentle massage, lullaby). Put baby down drowsy but awake.",
        severityOrLookout = "Look out for over-tiredness signs like eye-rubbing, ear-tugging, or yawning.",
        consultDoctorWhen = "Consult pediatrician if baby wakes up gasping, snorts loudly, or exhibits severe sleep apnea."
    ),
    ParentingTopic(
        name = "Soothing Colic & Evening Fussiness",
        reason = "Immature digestive systems and sensory overload can cause predictable 3-hour crying spells in early months.",
        actionableSteps = "Use the 5 S's: Swaddle comfortably, Side/Stomach position while holding, Shush white noise, Swing gently, offer Pacifier to Suck.",
        severityOrLookout = "Ensure crying isn't caused by a fever, hair tourniquet on toes/fingers, or tight diaper.",
        consultDoctorWhen = "Consult doctor if crying is high-pitched, incessant past 4 hours, or accompanied by vomiting/fever."
    ),
    ParentingTopic(
        name = "Breastfeeding & Latch Technique",
        reason = "A deep latch ensures effective milk transfer, prevents nipple soreness, and boosts maternal milk supply.",
        actionableSteps = "Bring baby's chin to breast first with mouth wide open like a yawn. Ensure both nipple and lower areola are in baby's mouth.",
        severityOrLookout = "Track 6+ wet diapers daily and steady weight gain as positive indicators of sufficient milk transfer.",
        consultDoctorWhen = "Consult a lactation consultant or pediatrician if baby shows yellow skin (jaundice) or fails to gain weight."
    ),
    ParentingTopic(
        name = "Introducing Solid Foods & Weaning",
        reason = "Around 6 months, infants require iron-rich solid foods to complement breastmilk or formula as nutritional needs expand.",
        actionableSteps = "Start with single-ingredient purees (mashed avocado, sweet potato, iron-fortified rice cereal). Wait 3 days between new foods to check for allergies.",
        severityOrLookout = "Watch for signs of readiness: sitting unsupported, good head control, and loss of tongue-thrust reflex.",
        consultDoctorWhen = "Consult pediatrician if baby develops hives, facial swelling, vomiting, or breathing difficulty after eating."
    ),
    ParentingTopic(
        name = "Daily Tummy Time & Motor Skill Development",
        reason = "Tummy time strengthens neck, shoulder, and core muscles required for rolling, sitting, and crawling.",
        actionableSteps = "Start with 2-3 minute sessions 3 times daily on a clean play mat. Place colorful contrast toys or a baby mirror in front.",
        severityOrLookout = "Place baby on chest or across lap if baby resists lying flat on the floor.",
        consultDoctorWhen = "Consult doctor if baby cannot hold head up at 4 months or shows persistent muscle stiffness/floppiness."
    ),
    ParentingTopic(
        name = "Teething Care & Dental Hygiene",
        reason = "Teething begins around 4-7 months as first teeth break through gums, causing drooling and mild gum discomfort.",
        actionableSteps = "Provide a clean refrigerated (not frozen) rubber teething ring. Wipe baby's gums twice daily with a clean damp cloth.",
        severityOrLookout = "Teething causes mild drooling and gum rubbing, NOT high fever (>101°F) or diarrhea.",
        consultDoctorWhen = "Consult dentist or pediatrician if no teeth appear by 15 months or if gum bleeding occurs."
    ),
    ParentingTopic(
        name = "Baby Boy Anatomical & Foreskin Care 👦",
        reason = "Proper infant foreskin and genital care prevents infection without causing painful tissue trauma.",
        actionableSteps = "Do NOT forcibly retract baby boy's foreskin. The foreskin is naturally fused to the glans at birth and separates gradually between 2 to 5+ years. Wash only the outside gently with warm water.",
        severityOrLookout = "Check that both testicles are descended into the scrotum. Never pull or stretch infantile foreskin.",
        consultDoctorWhen = "Consult pediatrician if there is severe redness, swelling, pus discharge, or if testicles remain undescended past 6 months."
    ),
    ParentingTopic(
        name = "Baby Girl Hygiene & UTI Prevention 👧",
        reason = "Female infant anatomy requires specific wiping habits to prevent intestinal bacteria from entering the short urethra.",
        actionableSteps = "ALWAYS wipe front-to-back (vagina toward anus). Mild white discharge or slight blood-tinged spotting in first 2 weeks is normal maternal hormone withdrawal; clean gently with warm water.",
        severityOrLookout = "Watch for thin labial fusion (labial adhesions) or foul-smelling urine.",
        consultDoctorWhen = "Consult pediatrician if urine is cloudy, foul-smelling, causes crying during urination (UTI sign), or if labial fusion obstructs urine flow."
    ),
    ParentingTopic(
        name = "Toddler Tantrum Co-Regulation",
        reason = "Toddlers experience intense emotions before the prefrontal cortex develops the capacity for self-regulation.",
        actionableSteps = "Stay calm, lower your voice, offer a warm hug or quiet space. Name their emotion ('I see you are frustrated').",
        severityOrLookout = "Ensure safety during a meltdown by removing hard objects nearby.",
        consultDoctorWhen = "Consult pediatrician if tantrums involve self-harm, breath-holding spells > 1 minute, or extreme aggression."
    ),
    ParentingTopic(
        name = "Speech & Language Stimulation",
        reason = "Early conversational turn-taking accelerates vocabulary growth and cognitive neural connections.",
        actionableSteps = "Narrate daily activities out loud. Read picture books daily. Respond eagerly when baby babbles or points.",
        severityOrLookout = "Engage in eye contact and imitate baby's vocal sounds.",
        consultDoctorWhen = "Consult doctor if child does not babble by 12 months, or has no single words by 15-18 months."
    )
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CubyParentingScreen(
    onBack: () -> Unit
) {
    val topicsList = parentingTopics

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(tr("parenting_guide")) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = tr("dynamic_79"))
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface,
                    navigationIconContentColor = MaterialTheme.colorScheme.onSurface
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

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                // Header Info Card
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFB2DFDB)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Info,
                            contentDescription = "Expand Details",
                            tint = Color(0xFF004D40),
                            modifier = Modifier.size(36.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Parenting & Child Care Guide",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF004D40)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Evidence-based advice & practical tips for raising a healthy child",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color(0xFF004D40).copy(alpha = 0.8f),
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                    }
                }

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    contentPadding = PaddingValues(bottom = 24.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(topicsList) { topic ->
                        ParentingTopicCard(topic)
                    }
                }
            }
        }
    }
}

@Composable
fun ParentingTopicCard(topic: ParentingTopic) {
    var isExpanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { isExpanded = !isExpanded },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = topic.name,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = topic.reason,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.DarkGray
            )

            androidx.compose.animation.AnimatedVisibility(
                visible = isExpanded,
                enter = androidx.compose.animation.fadeIn(animationSpec = androidx.compose.animation.core.tween(200, easing = androidx.compose.animation.core.FastOutSlowInEasing)) + androidx.compose.animation.expandVertically(animationSpec = androidx.compose.animation.core.tween(200, easing = androidx.compose.animation.core.FastOutSlowInEasing)),
                exit = androidx.compose.animation.fadeOut(animationSpec = androidx.compose.animation.core.tween(180, easing = androidx.compose.animation.core.FastOutLinearInEasing)) + androidx.compose.animation.shrinkVertically(animationSpec = androidx.compose.animation.core.tween(180, easing = androidx.compose.animation.core.FastOutLinearInEasing))
            ) {
                Column(
                    modifier = Modifier.padding(top = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    HorizontalDivider(color = Color.LightGray.copy(alpha = 0.4f), thickness = 0.5.dp)
                    
                    Text(
                        text = "💡 Actionable Steps & Guidance",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = topic.actionableSteps,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "👀 What to Look Out For",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = topic.severityOrLookout,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "🩺 Consult Pediatrician When",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFD32F2F)
                    )
                    Text(
                        text = topic.consultDoctorWhen,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            if (!isExpanded) {
                Text(
                    text = "Tap card to expand details ↓",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
    }
}
