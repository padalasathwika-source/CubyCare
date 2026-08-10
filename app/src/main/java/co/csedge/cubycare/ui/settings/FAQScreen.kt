package co.csedge.cubycare.ui.settings

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import co.csedge.cubycare.utils.tr

data class FAQItem(
    val category: String,
    val question: String,
    val answer: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FAQScreen(
    onBack: () -> Unit
) {
    val faqList = remember {
        listOf(
            FAQItem(
                category = "Vaccination Schedule",
                question = "How does CubyCare calculate mandatory vaccine dates?",
                answer = "CubyCare calculates vaccination schedules based on World Health Organization (WHO) and National Immunization Guidelines. Vaccine due dates are computed automatically from your baby's Date of Birth (DOB)."
            ),
            FAQItem(
                category = "Vaccination Schedule",
                question = "What should I do if a vaccine is delayed or missed?",
                answer = "If a dose is missed, mark it as 'Delayed' in the Vaccines tab. Consult your pediatrician to arrange a catch-up vaccination schedule. CubyCare will keep reminding you until marked as completed."
            ),
            FAQItem(
                category = "Growth Charts & WHO Percentiles",
                question = "How are growth percentiles calculated?",
                answer = "Your baby's weight, height, and head circumference entries are compared against standard WHO Growth Standards (0-5 years) for boys and girls to give exact percentile curves."
            ),
            FAQItem(
                category = "Growth Charts & WHO Percentiles",
                question = "How often should I log growth measurements?",
                answer = "For newborns (0-6 months), log measurements monthly during pediatric checkups. For older infants (6-24 months), log every 2-3 months."
            ),
            FAQItem(
                category = "Medicine Tracker & Alarms",
                question = "Do medicine alarm notifications work when the app is closed?",
                answer = "Yes! CubyCare uses Android AlarmManager to trigger exact medicine reminder alerts even if your phone is locked or the app is closed."
            ),
            FAQItem(
                category = "Multi-Account & Data Sharing",
                question = "Can both parents track the same baby using different phones?",
                answer = "Yes! You can log in using the same Google or Email account on both parents' phones, or use the 'Switch Account' feature in the Profile section to switch between multiple family accounts."
            ),
            FAQItem(
                category = "Data Export & Privacy",
                question = "How can I export my baby's medical records for doctor visits?",
                answer = "Go to Settings > Data Export & Cloud Sync > Export Medical Report Summary or Export Child Data Sheet (CSV File). You can export a PDF summary or a CSV file to print, email, or share via WhatsApp directly with your pediatrician."
            )
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(tr("faq_title")) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Filled.Info,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(32.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                "Frequently Asked Questions",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                            Text(
                                "Everything you need to know about tracking your baby's health.",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                            )
                        }
                    }
                }
            }

            items(faqList) { faq ->
                FAQCard(faq = faq)
            }

            item {
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
fun FAQCard(faq: FAQItem) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { expanded = !expanded },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = faq.category.uppercase(),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = faq.question,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                }
                Icon(
                    imageVector = if (expanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            }

            androidx.compose.animation.AnimatedVisibility(
                visible = expanded,
                enter = androidx.compose.animation.fadeIn(animationSpec = androidx.compose.animation.core.tween(200, easing = androidx.compose.animation.core.FastOutSlowInEasing)) + androidx.compose.animation.expandVertically(animationSpec = androidx.compose.animation.core.tween(200, easing = androidx.compose.animation.core.FastOutSlowInEasing)),
                exit = androidx.compose.animation.fadeOut(animationSpec = androidx.compose.animation.core.tween(180, easing = androidx.compose.animation.core.FastOutLinearInEasing)) + androidx.compose.animation.shrinkVertically(animationSpec = androidx.compose.animation.core.tween(180, easing = androidx.compose.animation.core.FastOutLinearInEasing))
            ) {
                Column {
                    Spacer(modifier = Modifier.height(12.dp))
                    HorizontalDivider(color = Color.LightGray.copy(alpha = 0.4f), thickness = 0.5.dp)
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = faq.answer,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.DarkGray
                    )
                }
            }
        }
    }
}
