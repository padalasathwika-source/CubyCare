package co.csedge.cubycare.ui.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import co.csedge.cubycare.data.repository.VaccineKnowledgeProvider
import co.csedge.cubycare.data.repository.VaccineScheduleProvider

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VaccineFlowchartScreen(vaccineName: String, onBack: () -> Unit) {
    val sanitizedName = remember(vaccineName) { VaccineScheduleProvider.sanitizeVaccineName(vaccineName) }
    val info = remember(sanitizedName) { VaccineKnowledgeProvider.getInfoForVaccine(sanitizedName) }

    val bgColors = listOf(
        Color(0xFFE3F2FD), // Light blue
        Color(0xFFFFF9C4), // Light yellow
        Color(0xFFFCE4EC), // Light pink
        Color(0xFFE8F5E9)  // Light green
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("How ${info.vaccineName} Works") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFFFC0CB), // Baby Pink
                    titleContentColor = Color.DarkGray
                )
            )
        },
        containerColor = Color(0xFFFFC0CB) // Baby Pink
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header Info Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = info.fullMedicalName,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Surface(
                        color = MaterialTheme.colorScheme.primaryContainer,
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = "Shields against: ${info.diseasePrevented}",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "📍 Administration: ${info.administrationRoute}",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.DarkGray
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Why Needed Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF8E1)), // Soft warm yellow
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Filled.Info, contentDescription = null, tint = Color(0xFFF57F17))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Why Is It Taken?",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFE65100)
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = info.whyNeeded,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Black.copy(alpha = 0.8f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // How It Works Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFE1F5FE)), // Soft sky blue
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Filled.Star, contentDescription = null, tint = Color(0xFF0288D1))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "How Does It Work In The Body?",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF01579B)
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = info.howItWorks,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Black.copy(alpha = 0.8f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Flowchart Heading
            Text(
                "Step-by-Step Biological Journey to Immunity",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = Color.DarkGray,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Render 4 Flowchart Steps
            info.flowchartSteps.forEachIndexed { index, step ->
                FlowchartStep(
                    stepNumber = step.stepNumber,
                    title = step.title,
                    description = step.description,
                    color = bgColors[index % bgColors.size]
                )
                if (index < info.flowchartSteps.size - 1) {
                    FlowchartArrow()
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Normal Reactions Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF3E5F5)), // Soft lavender
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Filled.CheckCircle, contentDescription = null, tint = Color(0xFF7B1FA2))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Normal Mild Reactions & Care",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF4A148C)
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = info.normalReactions,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Black.copy(alpha = 0.8f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun FlowchartStep(stepNumber: String, title: String, description: String, color: Color) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = color),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.8f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stepNumber,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.DarkGray
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.DarkGray
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.DarkGray.copy(alpha = 0.8f)
                )
            }
        }
    }
}

@Composable
fun FlowchartArrow() {
    Icon(
        imageVector = Icons.Filled.KeyboardArrowDown,
        contentDescription = null,
        tint = Color.DarkGray,
        modifier = Modifier
            .size(36.dp)
            .padding(vertical = 4.dp)
    )
}
