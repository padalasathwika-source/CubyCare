package co.csedge.cubycare.ui.dashboard

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import co.csedge.cubycare.data.model.Child
import co.csedge.cubycare.data.repository.HealthAdviceProvider

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HealthTrackerDetailScreen(
    child: Child,
    ageRange: String,
    onBack: () -> Unit,
    onOpenNearby: () -> Unit = {},
    bottomBar: @Composable () -> Unit = {}
) {
    val context = LocalContext.current
    val symptoms = HealthAdviceProvider.symptomsList
    var selectedSymptomIndex by remember { mutableIntStateOf(0) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Image(
                                painter = painterResource(id = co.csedge.cubycare.R.drawable.vital),
                                contentDescription = null,
                                modifier = Modifier.size(24.dp).padding(end = 6.dp)
                            )
                            Text("Cuby Vitals • Symptoms & Advice", fontWeight = FontWeight.Bold)
                        }
                        Text("${child.name} • $ageRange", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White,
                    titleContentColor = Color.DarkGray
                )
            )
        },
        bottomBar = bottomBar,
        containerColor = Color(0xFFF9F9F9)
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Symptom Sub-tabs (Fever, Cough & Cold, Vomiting, Diarrhea, Rashes, Constipation)
            ScrollableTabRow(
                selectedTabIndex = selectedSymptomIndex,
                containerColor = Color.White,
                edgePadding = 8.dp
            ) {
                symptoms.forEachIndexed { index, symptom ->
                    Tab(
                        selected = selectedSymptomIndex == index,
                        onClick = { selectedSymptomIndex = index },
                        text = { Text(symptom, fontWeight = FontWeight.Bold) }
                    )
                }
            }

            val currentSymptom = symptoms[selectedSymptomIndex]
            val advice = HealthAdviceProvider.getSymptomAdvice(ageRange, currentSymptom)

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Description Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "About $currentSymptom",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = advice.description,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }

                // Home Remedies Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Home Remedies (Mild Severity)",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF2E7D32)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        advice.homeRemedies.forEach { remedy ->
                            Row(modifier = Modifier.padding(vertical = 4.dp)) {
                                Text("•", modifier = Modifier.padding(end = 8.dp), color = Color(0xFF2E7D32))
                                Text(remedy, style = MaterialTheme.typography.bodyMedium)
                            }
                        }
                    }
                }

                // Consult Doctor Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFFEBEE))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "When to Consult a Doctor",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.error
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        advice.doctorFactors.forEach { factor ->
                            Row(modifier = Modifier.padding(vertical = 4.dp)) {
                                Text("•", modifier = Modifier.padding(end = 8.dp), color = MaterialTheme.colorScheme.error)
                                Text(factor, style = MaterialTheme.typography.bodyMedium)
                            }
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        Button(
                            onClick = {
                                co.csedge.cubycare.utils.HospitalUtils.openPediatricsSpecialty(context)
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Consult Malla Reddy Narayana Pediatrics", fontWeight = FontWeight.Bold, color = Color.White)
                        }
                    }
                }
            }
        }
    }
}
