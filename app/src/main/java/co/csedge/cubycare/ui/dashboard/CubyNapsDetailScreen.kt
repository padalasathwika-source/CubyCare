package co.csedge.cubycare.ui.dashboard

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import co.csedge.cubycare.data.model.Child
import co.csedge.cubycare.data.repository.SleepCycleProvider

import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CubyNapsDetailScreen(
    child: Child,
    ageRange: String,
    onBack: () -> Unit
) {
    val sleepInfo = remember(ageRange) {
        SleepCycleProvider.getSleepInfoForAge(ageRange)
    }
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("$ageRange Sleep Info") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFE8EAF6), // Soft Twilight Blue
                    titleContentColor = Color.DarkGray
                )
            )
        },
        containerColor = Color.Transparent
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize()) {
            androidx.compose.foundation.Image(
                painter = androidx.compose.ui.res.painterResource(id = co.csedge.cubycare.R.drawable.cuby_naps_bg),
                contentDescription = "Background",
                contentScale = androidx.compose.ui.layout.ContentScale.Crop,
                modifier = Modifier.fillMaxSize(),
                alpha = 0.20f
            )
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .verticalScroll(scrollState)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFAFAFA)),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    // Sleep Duration
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(12.dp)
                                .background(Color(0xFF5C6BC0), CircleShape)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = "Total Sleep Duration",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF5C6BC0)
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = sleepInfo.sleepDuration,
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.DarkGray,
                        modifier = Modifier.padding(start = 24.dp)
                    )
                    
                    Spacer(modifier = Modifier.height(24.dp))

                    // Number of Naps
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(12.dp)
                                .background(Color(0xFF42A5F5), CircleShape)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = "Number of Naps",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF42A5F5)
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = sleepInfo.numberOfNaps,
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.DarkGray,
                        modifier = Modifier.padding(start = 24.dp)
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Night Awakenings
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(12.dp)
                                .background(Color(0xFF7E57C2), CircleShape)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = "Night Awakenings",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF7E57C2)
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = sleepInfo.nightAwakenings,
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.DarkGray,
                        modifier = Modifier.padding(start = 24.dp)
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // When Disturbed
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(12.dp)
                                .background(Color(0xFFE53935), CircleShape) // Red for disturbance
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = "When Sleep Cycle is Disturbed",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFE53935)
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = sleepInfo.whenDisturbed,
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.DarkGray,
                        modifier = Modifier.padding(start = 24.dp)
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Mild Remedies
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(12.dp)
                                .background(Color(0xFF4CAF50), CircleShape) // Green for remedies
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = "Home Remedies for Mild Disturbances",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF2E7D32) // Dark Green
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = sleepInfo.mildRemedies,
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.DarkGray,
                        modifier = Modifier.padding(start = 24.dp)
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Severe Symptoms / Consult Doctor
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(12.dp)
                                .background(Color(0xFFC62828), CircleShape) // Dark Red for severe
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = "When Severity Increases (Consult Doctor)",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFC62828)
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = sleepInfo.severeSymptoms,
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.DarkGray,
                        modifier = Modifier.padding(start = 24.dp)
                    )
                }
            }
            
            // Info Note
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF9C4)) // Light Yellow
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Icon(
                        imageVector = Icons.Filled.Info,
                        contentDescription = "Info",
                        tint = Color(0xFFF57F17),
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "Every child is unique. These are general guidelines. If you have concerns about your child's sleep patterns, please consult your pediatrician.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.DarkGray
                    )
                }
            }
        }
        }
    }
}
