package co.csedge.cubycare.ui.dashboard

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import co.csedge.cubycare.data.model.Child
import co.csedge.cubycare.data.model.GrowthRecord
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

import androidx.compose.foundation.Image
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import co.csedge.cubycare.utils.tr

import co.csedge.cubycare.utils.currentAgeRange

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GrowthScreen(
    child: Child, 
    onBack: () -> Unit, 
    onUpdateChild: (Child) -> Unit,
    bottomBar: @Composable () -> Unit = {}
) {
    var showAddDialog by remember { mutableStateOf(false) }
    var selectedMonthToLog by remember { mutableStateOf<Int?>(null) }
    val isGeneralProfile = child.id == "default_general_baby" || child.name.contains("General Baby") || child.dateOfBirthMillis == 0L
    var selectedMilestoneAge by remember { mutableStateOf<String?>(if (isGeneralProfile) null else child.currentAgeRange) }

    // Top Section Tab: 0 -> Growth Tracker, 1 -> Milestones
    var mainSectionTab by remember { mutableIntStateOf(0) }
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("Text Logs", "Block Chart", "Line Graph", "Pie Chart", "WHO Boys vs Girls 👦👧")

    // Generate estimated logs for missing months
    val allLogs = remember(child.growthLogs, child.ageInMonths, child.birthWeight) {
        generateEstimatedLogs(child)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "${child.name}'s Growth & Milestones",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Surface(
                            shape = CircleShape,
                            color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f),
                            modifier = Modifier.size(36.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "Back",
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        },
        bottomBar = bottomBar,
        containerColor = Color.Transparent,
        floatingActionButton = {
            if (mainSectionTab == 0) {
                FloatingActionButton(onClick = { 
                    selectedMonthToLog = null
                    showAddDialog = true 
                }) {
                    Icon(Icons.Filled.Add, contentDescription = "Add Log")
                }
            }
        }
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize()) {
            Image(
                painter = painterResource(id = co.csedge.cubycare.R.drawable.mother_baby_growth_bg),
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
                // Top Merged Section Tabs
                TabRow(
                    selectedTabIndex = mainSectionTab,
                    containerColor = Color.White
                ) {
                    Tab(
                        selected = mainSectionTab == 0,
                        onClick = { mainSectionTab = 0 },
                        text = { Text("📈 Growth Tracker", fontWeight = FontWeight.Bold) }
                    )
                    Tab(
                        selected = mainSectionTab == 1,
                        onClick = { mainSectionTab = 1 },
                        text = { Text("🏆 Milestones", fontWeight = FontWeight.Bold) }
                    )
                }

                if (mainSectionTab == 0) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                    ) {
                        DisclaimerBanner()
                        
                        ScrollableTabRow(
                            selectedTabIndex = selectedTab,
                            edgePadding = 8.dp
                        ) {
                            tabs.forEachIndexed { index, title ->
                                Tab(
                                    selected = selectedTab == index,
                                    onClick = { selectedTab = index },
                                    text = { Text(title) }
                                )
                            }
                        }

                        val realLogs = child.growthLogs.sortedBy { it.dateMillis }
                        val alerts = analyzeGrowth(realLogs)
                        if (alerts.isNotEmpty() && selectedTab == 0) {
                            AlertsSection(alerts)
                        }

                        Box(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                            when (selectedTab) {
                                0 -> TextLogsView(child, allLogs, onLogMonth = { month ->
                                    selectedMonthToLog = month
                                    showAddDialog = true
                                })
                                1 -> BlockChartGraph(allLogs)
                                2 -> LineChartGraph(allLogs)
                                3 -> GrowthPieChart(child, realLogs)
                                4 -> WHOGenderGrowthStandardsView(child)
                            }
                        }
                    }
                } else {
                    // Milestones Content
                    Box(modifier = Modifier.fillMaxSize()) {
                        if (selectedMilestoneAge != null) {
                            MilestoneDetailScreen(
                                child = child,
                                ageRange = selectedMilestoneAge!!,
                                onBack = { selectedMilestoneAge = null },
                                onUpdateChild = onUpdateChild
                            )
                        } else {
                            MilestoneScreen(
                                child = child,
                                onBack = { mainSectionTab = 0 },
                                onAgeBlockClick = { ageRange ->
                                    selectedMilestoneAge = ageRange
                                },
                                onUpdateChild = onUpdateChild
                            )
                        }
                    }
                }
            }
        }

        if (showAddDialog) {
            AddGrowthLogDialog(
                prefillMonth = selectedMonthToLog,
                onDismiss = { showAddDialog = false },
                onSave = { record ->
                    val updatedLogs = child.growthLogs + record
                    onUpdateChild(child.copy(growthLogs = updatedLogs))
                    showAddDialog = false
                }
            )
        }
    }
}

/**
 * Calculates missing months up to the child's current age, and creates
 * "Estimated" records using linear interpolation from Birth Weight to Latest Weight.
 */
fun generateEstimatedLogs(child: Child): List<GrowthRecord> {
    val realLogs = child.growthLogs
    val age = child.ageInMonths
    if (age <= 0) return realLogs
    
    val birthWeight = child.birthWeight.replace(Regex("[^0-9.]"), "").toDoubleOrNull() ?: 3.0
    val birthHeight = child.birthLength.replace(Regex("[^0-9.]"), "").toDoubleOrNull() ?: 50.0
    val birthHeadCirc = child.headCircumference.replace(Regex("[^0-9.]"), "").toDoubleOrNull() ?: 35.0
    
    val latestRealLog = realLogs.maxByOrNull { it.dateMillis }
    val latestWeight = latestRealLog?.weightKg ?: birthWeight
    val latestHeight = latestRealLog?.lengthCm?.takeIf { it > 0.0 } ?: birthHeight
    val latestHeadCirc = latestRealLog?.headCircumferenceCm?.takeIf { it > 0.0 } ?: birthHeadCirc
    
    val completeList = realLogs.toMutableList()
    
    for (m in 1..age) {
        val hasRealLog = realLogs.any { it.monthIndex == m }
        if (!hasRealLog) {
            // Linear interpolation estimates
            val fraction = m.toDouble() / age.coerceAtLeast(1)
            val estimatedWeight = birthWeight + ((latestWeight - birthWeight) * fraction)
            val estimatedHeight = birthHeight + ((latestHeight - birthHeight) * fraction)
            val estimatedHeadCirc = birthHeadCirc + ((latestHeadCirc - birthHeadCirc) * fraction)
            
            val estimatedTime = child.dateOfBirthMillis + (m * 30.44 * 24 * 60 * 60 * 1000).toLong()
            
            completeList.add(
                GrowthRecord(
                    id = "est_$m",
                    dateMillis = estimatedTime,
                    weightKg = estimatedWeight,
                    lengthCm = estimatedHeight,
                    headCircumferenceCm = estimatedHeadCirc,
                    isEstimated = true,
                    monthIndex = m
                )
            )
        }
    }
    return completeList.sortedByDescending { it.dateMillis }
}

@Composable
fun TextLogsView(child: Child, allLogs: List<GrowthRecord>, onLogMonth: (Int) -> Unit) {
    Column {
        // Missing prompts
        val missingMonths = (1..child.ageInMonths).filter { m -> 
            !child.growthLogs.any { it.monthIndex == m }
        }
        
        if (missingMonths.isNotEmpty()) {
            Text("Missing Real Logs:", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.error)
            Spacer(modifier = Modifier.height(8.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                missingMonths.take(3).forEach { m ->
                    Button(onClick = { onLogMonth(m) }, modifier = Modifier.weight(1f)) {
                        Text("Month $m")
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        if (allLogs.isEmpty()) {
            Box(modifier = Modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) {
                Text("No growth logs yet. Click + to add one.")
            }
        } else {
            Column(modifier = Modifier.fillMaxWidth()) {
                allLogs.forEach { record ->
                    GrowthRecordCard(record)
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

@Composable
fun BlockChartGraph(logs: List<GrowthRecord>) {
    val sortedLogs = logs.sortedBy { it.monthIndex ?: 0 }
    if (sortedLogs.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Not enough data to draw a chart.")
        }
        return
    }

    var selectedMetric by remember { mutableStateOf("Weight (kg)") }
    val metrics = listOf("Weight (kg)", "Height (cm)", "Head Circ (cm)", "BMI")

    Column(modifier = Modifier.fillMaxSize()) {
        Text("Growth Trend vs Age (Months)", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            metrics.forEach { metric ->
                FilterChip(
                    selected = selectedMetric == metric,
                    onClick = { selectedMetric = metric },
                    label = { Text(metric) }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        val maxValue = when (selectedMetric) {
            "Weight (kg)" -> sortedLogs.maxOfOrNull { it.weightKg }?.coerceAtLeast(10.0) ?: 10.0
            "Height (cm)" -> sortedLogs.maxOfOrNull { it.lengthCm }?.coerceAtLeast(100.0) ?: 100.0
            "Head Circ (cm)" -> sortedLogs.maxOfOrNull { it.headCircumferenceCm }?.coerceAtLeast(50.0) ?: 50.0
            else -> sortedLogs.maxOfOrNull { it.bmi }?.coerceAtLeast(20.0) ?: 20.0
        }

        val maxMonths = sortedLogs.size

        Canvas(modifier = Modifier.fillMaxWidth().height(300.dp).padding(16.dp)) {
            val width = size.width
            val height = size.height
            val barWidth = (width / maxMonths) * 0.6f
            val spacing = (width / maxMonths) * 0.4f

            // Draw Axes
            drawLine(Color.Gray, start = Offset(0f, height), end = Offset(width, height), strokeWidth = 4f)
            drawLine(Color.Gray, start = Offset(0f, 0f), end = Offset(0f, height), strokeWidth = 4f)

            sortedLogs.forEachIndexed { index, record ->
                val x = (index * (barWidth + spacing)) + (spacing / 2)
                
                val value = when (selectedMetric) {
                    "Weight (kg)" -> record.weightKg
                    "Height (cm)" -> record.lengthCm
                    "Head Circ (cm)" -> record.headCircumferenceCm
                    else -> record.bmi
                }

                val barHeight = ((value / maxValue) * height).toFloat()
                val y = height - barHeight

                val color = if (record.isEstimated) Color(0xFFFFB74D) else Color(0xFF64B5F6)

                drawRoundRect(
                    color = color,
                    topLeft = Offset(x, y),
                    size = Size(barWidth, barHeight),
                    cornerRadius = CornerRadius(8f, 8f)
                )
            }
        }

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            Text("Blue Blocks = Real Data, Orange Blocks = Estimated", color = MaterialTheme.colorScheme.onSurfaceVariant, style = MaterialTheme.typography.bodySmall)
        }
    }
}

@Composable
fun LineChartGraph(logs: List<GrowthRecord>) {
    val sortedLogs = logs.sortedBy { it.monthIndex ?: 0 }
    if (sortedLogs.size < 2) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Not enough data to draw a chart.")
        }
        return
    }
    
    var selectedMetric by remember { mutableStateOf("Weight (kg)") }
    val metrics = listOf("Weight (kg)", "Height (cm)", "Head Circ (cm)", "BMI")

    val maxMonths = sortedLogs.size

    Column(modifier = Modifier.fillMaxSize()) {
        Text("Growth Trend vs Age (Months)", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            metrics.forEach { metric ->
                FilterChip(
                    selected = selectedMetric == metric,
                    onClick = { selectedMetric = metric },
                    label = { Text(metric) }
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        
        val maxValue = when (selectedMetric) {
            "Weight (kg)" -> sortedLogs.maxOf { it.weightKg }.coerceAtLeast(10.0)
            "Height (cm)" -> sortedLogs.maxOf { it.lengthCm }.coerceAtLeast(100.0)
            "Head Circ (cm)" -> sortedLogs.maxOf { it.headCircumferenceCm }.coerceAtLeast(50.0)
            else -> sortedLogs.maxOf { it.bmi }.coerceAtLeast(20.0)
        }

        Canvas(modifier = Modifier.fillMaxWidth().height(300.dp).padding(16.dp)) {
            val width = size.width
            val height = size.height
            val xStep = width / (maxMonths - 1).coerceAtLeast(1)
            
            // Draw Axes
            drawLine(Color.Gray, start = Offset(0f, height), end = Offset(width, height), strokeWidth = 4f)
            drawLine(Color.Gray, start = Offset(0f, 0f), end = Offset(0f, height), strokeWidth = 4f)

            val path = Path()
            sortedLogs.forEachIndexed { index, record ->
                val x = index * xStep
                
                val value = when (selectedMetric) {
                    "Weight (kg)" -> record.weightKg
                    "Height (cm)" -> record.lengthCm
                    "Head Circ (cm)" -> record.headCircumferenceCm
                    else -> record.bmi
                }
                
                // Normalize Y
                val y = height - ((value / maxValue) * height).toFloat()
                
                if (index == 0) path.moveTo(x, y) else path.lineTo(x, y)
                
                // Draw a dot, color it red if it's an estimate
                drawCircle(
                    color = if (record.isEstimated) Color.Red else Color.Blue,
                    radius = 8f,
                    center = Offset(x, y)
                )
            }
            
            drawPath(path, color = Color.Blue, style = Stroke(width = 4f))
        }
        
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            Text("Blue = Real Data, Red = Estimated", color = MaterialTheme.colorScheme.onSurfaceVariant, style = MaterialTheme.typography.bodySmall)
        }
    }
}

@Composable
fun GrowthPieChart(child: Child, realLogs: List<GrowthRecord>) {
    var selectedMetric by remember { mutableStateOf("Weight (kg)") }
    val metrics = listOf("Weight (kg)", "Height (cm)", "Head Circ (cm)", "BMI")

    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        Text("Growth Composition", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            metrics.forEach { metric ->
                FilterChip(
                    selected = selectedMetric == metric,
                    onClick = { selectedMetric = metric },
                    label = { Text(metric) }
                )
            }
        }
        Spacer(modifier = Modifier.height(32.dp))

        val birthValueStr = when (selectedMetric) {
            "Weight (kg)" -> child.birthWeight
            "Height (cm)" -> child.birthLength
            "Head Circ (cm)" -> child.headCircumference
            else -> {
                val bw = child.birthWeight.replace(Regex("[^0-9.]"), "").toDoubleOrNull() ?: 3.0
                val bl = child.birthLength.replace(Regex("[^0-9.]"), "").toDoubleOrNull() ?: 50.0
                val hm = bl / 100.0
                if (hm > 0) (bw / (hm * hm)).toString() else "12.0"
            }
        }.replace(Regex("[^0-9.]"), "")
        
        val birthValue = birthValueStr.toFloatOrNull() ?: if (selectedMetric == "Height (cm)") 50f else if (selectedMetric == "Head Circ (cm)") 35f else if (selectedMetric == "BMI") 12f else 3.0f
        
        val latestLog = realLogs.maxByOrNull { it.dateMillis }
        val latestValue = when (selectedMetric) {
            "Weight (kg)" -> latestLog?.weightKg?.toFloat()
            "Height (cm)" -> latestLog?.lengthCm?.toFloat()
            "Head Circ (cm)" -> latestLog?.headCircumferenceCm?.toFloat()
            else -> latestLog?.bmi?.toFloat()
        } ?: birthValue
        
        val valueGained = (latestValue - birthValue).coerceAtLeast(0f)
        val total = birthValue + valueGained
        val birthSweep = if (total > 0f) (birthValue / total) * 360f else 0f
        val gainSweep = if (total > 0f) (valueGained / total) * 360f else 0f

        Canvas(modifier = Modifier.size(200.dp)) {
            drawArc(
                color = Color(0xFF4CAF50), // Green for Birth
                startAngle = -90f,
                sweepAngle = birthSweep,
                useCenter = true,
                size = size
            )
            drawArc(
                color = Color(0xFF2196F3), // Blue for Gained
                startAngle = -90f + birthSweep,
                sweepAngle = gainSweep,
                useCenter = true,
                size = size
            )
        }
        
        Spacer(modifier = Modifier.height(32.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.size(16.dp).background(Color(0xFF4CAF50)))
            Spacer(modifier = Modifier.width(8.dp))
            Text("Birth: ${String.format("%.1f", birthValue)}")
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.size(16.dp).background(Color(0xFF2196F3)))
            Spacer(modifier = Modifier.width(8.dp))
            Text("Gained: ${String.format("%.1f", valueGained)}")
        }
    }
}

@Composable
fun DisclaimerBanner() {
    Surface(
        color = MaterialTheme.colorScheme.errorContainer,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "Important: Growth interpretation should be based on validated child growth standards and should not replace pediatric assessment.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onErrorContainer,
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Composable
fun AlertsSection(alerts: List<String>) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Filled.Warning, contentDescription = "Alert", tint = MaterialTheme.colorScheme.onTertiaryContainer)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Growth Alerts", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onTertiaryContainer)
            }
            Spacer(modifier = Modifier.height(8.dp))
            alerts.forEach { alert ->
                Text("• $alert", style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}

@Composable
fun GrowthRecordCard(record: GrowthRecord) {
    val formatter = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
    val dateStr = formatter.format(Date(record.dateMillis))

    val bgColor = if (record.isEstimated) MaterialTheme.colorScheme.surfaceVariant else MaterialTheme.colorScheme.surface

    Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = bgColor)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Month ${record.monthIndex ?: "?"} - $dateStr", 
                    fontWeight = FontWeight.Bold
                )
                if (record.isEstimated) {
                    Text("(Estimated)", color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.labelSmall)
                }
            }
            Spacer(modifier = Modifier.height(4.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Weight: ${String.format("%.2f", record.weightKg)} kg")
                if (!record.isEstimated) Text("Height: ${record.lengthCm} cm")
            }
            if (!record.isEstimated) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Head Circ: ${record.headCircumferenceCm} cm")
                    Text("BMI: ${String.format("%.1f", record.bmi)}")
                }
            }
        }
    }
}

@Composable
fun AddGrowthLogDialog(prefillMonth: Int?, onDismiss: () -> Unit, onSave: (GrowthRecord) -> Unit) {
    var weight by remember { mutableStateOf("") }
    var height by remember { mutableStateOf("") }
    var headCircumference by remember { mutableStateOf("") }

    val calculatedBmi = remember(weight, height) {
        val w = weight.toDoubleOrNull() ?: 0.0
        val h = height.toDoubleOrNull() ?: 0.0
        if (h > 0.0 && w > 0.0) {
            val heightM = h / 100.0
            w / (heightM * heightM)
        } else {
            0.0
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (prefillMonth != null) "Log Month $prefillMonth" else "Add Growth Log") },
        text = {
            Column {
                OutlinedTextField(
                    value = weight,
                    onValueChange = { weight = it },
                    label = { Text("Weight (kg)") },
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = height,
                    onValueChange = { height = it },
                    label = { Text("Height/Length (cm)") },
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = headCircumference,
                    onValueChange = { headCircumference = it },
                    label = { Text("Head Circ. (cm)") },
                    singleLine = true
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                if (calculatedBmi > 0.0) {
                    Surface(
                        color = MaterialTheme.colorScheme.secondaryContainer,
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Estimated BMI: ${String.format("%.1f", calculatedBmi)}",
                            modifier = Modifier.padding(12.dp),
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = {
                val record = GrowthRecord(
                    id = System.currentTimeMillis().toString(),
                    dateMillis = System.currentTimeMillis(),
                    weightKg = weight.toDoubleOrNull() ?: 0.0,
                    lengthCm = height.toDoubleOrNull() ?: 0.0,
                    headCircumferenceCm = headCircumference.toDoubleOrNull() ?: 0.0,
                    monthIndex = prefillMonth,
                    isEstimated = false
                )
                onSave(record)
            }) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text(tr("cancel")) }
        }
    )
}

fun analyzeGrowth(logs: List<GrowthRecord>): List<String> {
    if (logs.size < 2) return emptyList()
    
    val sortedLogs = logs.sortedBy { it.dateMillis }
    val latest = sortedLogs.last()
    val previous = sortedLogs[sortedLogs.size - 2]
    val alerts = mutableListOf<String>()

    // Weight checks
    if (latest.weightKg < previous.weightKg) {
        val dropPercent = ((previous.weightKg - latest.weightKg) / previous.weightKg) * 100
        if (dropPercent > 5.0) {
            alerts.add("Sudden weight loss detected (dropped ${String.format("%.1f", dropPercent)}%). Consult a pediatrician.")
        } else {
            alerts.add("Minor weight loss detected since last reading.")
        }
    } else if (latest.weightKg > previous.weightKg) {
        val gainPercent = ((latest.weightKg - previous.weightKg) / previous.weightKg) * 100
        if (gainPercent > 15.0) {
            alerts.add("Excessive weight gain detected (increased ${String.format("%.1f", gainPercent)}%).")
        }
    }

    // Height / Length check
    if (latest.weightKg == previous.weightKg && latest.lengthCm == previous.lengthCm) {
        alerts.add("Growth plateau detected. No change in weight or height since last reading.")
    }
    
    // Head Circumference check
    if (previous.headCircumferenceCm > 0.0 && latest.headCircumferenceCm > 0.0) {
        if (latest.headCircumferenceCm < previous.headCircumferenceCm) {
            alerts.add("Warning: Head circumference reading is lower than the previous reading. Please double check the measurement.")
        } else if (latest.headCircumferenceCm == previous.headCircumferenceCm) {
            alerts.add("Head circumference plateau detected.")
        } else {
            val gain = latest.headCircumferenceCm - previous.headCircumferenceCm
            if (gain > 2.0) { // arbitrary fast growth threshold for a short period
                alerts.add("Rapid head circumference growth detected (+${String.format("%.1f", gain)} cm). Ensure measurements are accurate.")
            }
        }
    }

    // BMI Check
    if (previous.bmi > 0.0 && latest.bmi > 0.0) {
        if (latest.bmi < previous.bmi) {
            val bmiDrop = ((previous.bmi - latest.bmi) / previous.bmi) * 100
            if (bmiDrop > 5.0) {
                alerts.add("Significant BMI drop detected (dropped ${String.format("%.1f", bmiDrop)}%).")
            }
        }
    }

    // Poor weight check (simple heuristic for demonstration: if weight is barely increasing)
    val ageInMonths = latest.monthIndex ?: 0
    if (ageInMonths > 0) {
        val expectedMinWeight = 2.5 + (ageInMonths * 0.4) // Very conservative baseline
        if (latest.weightKg < expectedMinWeight) {
            alerts.add("Poor weight detected for age. Please consult a pediatrician.")
        }
    }

    return alerts
}

@Composable
fun WHOGenderGrowthStandardsView(child: Child) {
    val isBoy = child.gender.equals("Boy", ignoreCase = true) || child.gender.equals("Male", ignoreCase = true)
    val isGirl = child.gender.equals("Girl", ignoreCase = true) || child.gender.equals("Female", ignoreCase = true)

    Card(
        modifier = Modifier.fillMaxWidth().padding(4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = if (isBoy) "👦 WHO Growth Standards (Boys Target)" else if (isGirl) "👧 WHO Growth Standards (Girls Target)" else "👦👧 WHO Growth Standards (Boys vs Girls)",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Official World Health Organization (WHO) median weight, length, and head circumference benchmarks for boys and girls from birth to 24 months:",
                style = MaterialTheme.typography.bodySmall,
                color = Color.DarkGray
            )
            Spacer(modifier = Modifier.height(14.dp))

            // Boys Table Card
            Surface(
                color = Color(0xFFE3F2FD),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text("👦 Boys WHO Benchmarks:", fontWeight = FontWeight.Bold, color = Color(0xFF1565C0))
                    Spacer(modifier = Modifier.height(6.dp))
                    Text("• At Birth: Weight 3.3 kg | Length 49.9 cm | Head 34.5 cm", style = MaterialTheme.typography.bodySmall)
                    Text("• 6 Months: Weight 7.9 kg | Length 67.6 cm | Head 43.3 cm", style = MaterialTheme.typography.bodySmall)
                    Text("• 12 Months (1 yr): Weight 9.6 kg | Length 75.7 cm | Head 46.1 cm", style = MaterialTheme.typography.bodySmall)
                    Text("• 24 Months (2 yrs): Weight 12.2 kg | Length 87.1 cm | Head 48.3 cm", style = MaterialTheme.typography.bodySmall)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Girls Table Card
            Surface(
                color = Color(0xFFFCE4EC),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text("👧 Girls WHO Benchmarks:", fontWeight = FontWeight.Bold, color = Color(0xFFC2185B))
                    Spacer(modifier = Modifier.height(6.dp))
                    Text("• At Birth: Weight 3.2 kg | Length 49.1 cm | Head 33.9 cm", style = MaterialTheme.typography.bodySmall)
                    Text("• 6 Months: Weight 7.3 kg | Length 65.7 cm | Head 42.2 cm", style = MaterialTheme.typography.bodySmall)
                    Text("• 12 Months (1 yr): Weight 8.9 kg | Length 74.0 cm | Head 44.9 cm", style = MaterialTheme.typography.bodySmall)
                    Text("• 24 Months (2 yrs): Weight 11.5 kg | Length 85.7 cm | Head 47.2 cm", style = MaterialTheme.typography.bodySmall)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "💡 Clinical Note: Healthy children grow along their individual percentile curve. A steady upward trend is more important than matching an exact median number.",
                style = MaterialTheme.typography.labelSmall,
                color = Color.Gray
            )
        }
    }
}
