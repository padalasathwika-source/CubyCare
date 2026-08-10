package co.csedge.cubycare.ui.dashboard

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import co.csedge.cubycare.data.model.Child
import co.csedge.cubycare.data.model.DevelopmentalMilestone
import kotlinx.coroutines.delay
import co.csedge.cubycare.utils.tr

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MilestoneDetailScreen(
    child: Child,
    ageRange: String,
    onBack: () -> Unit,
    onUpdateChild: (Child) -> Unit
) {
    val filteredMilestones = remember(child.milestones, ageRange) {
        val list = if (child.milestones.isEmpty()) {
            co.csedge.cubycare.data.repository.MilestoneProvider.generateDefaultMilestones()
        } else {
            child.milestones
        }
        val target = list.filter { 
            it.ageRange == ageRange || 
            it.ageRange.equals(ageRange, ignoreCase = true) ||
            ageRange.contains(it.ageRange, ignoreCase = true) ||
            it.ageRange.contains(ageRange.take(4), ignoreCase = true)
        }
        if (target.isEmpty()) list.take(6) else target
    }
    val groupedByDomain = filteredMilestones.groupBy { it.domain }

    var showFullScreenCelebration by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("$ageRange Milestones") },
                    navigationIcon = {
                        IconButton(onClick = onBack) {
                            Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.White,
                        titleContentColor = Color.DarkGray
                    )
                )
            },
            containerColor = Color(0xFFF9F9F9)
        ) { paddingValues ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(top = 16.dp, bottom = 32.dp)
            ) {
                groupedByDomain.forEach { (domain, domainMilestones) ->
                    item {
                        Text(
                            text = domain,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(bottom = 8.dp, top = 8.dp)
                        )
                    }

                    items(domainMilestones) { milestone ->
                        MilestoneItem(
                            milestone = milestone,
                            onToggle = { isChecked ->
                                if (isChecked) {
                                    showFullScreenCelebration = true
                                }
                                val updatedList = child.milestones.map {
                                    if (it.id == milestone.id) {
                                        it.copy(
                                            isCompleted = isChecked,
                                            completedDateMillis = if (isChecked) System.currentTimeMillis() else null
                                        )
                                    } else it
                                }
                                onUpdateChild(child.copy(milestones = updatedList))
                            }
                        )
                    }
                }
            }
        }

        if (showFullScreenCelebration) {
            FullScreenCelebration(
                onAnimationFinished = { showFullScreenCelebration = false }
            )
        }
    }
}

@Composable
fun FullScreenCelebration(onAnimationFinished: () -> Unit) {
    var isPlaying by remember { mutableStateOf(false) }
    val context = androidx.compose.ui.platform.LocalContext.current
    
    LaunchedEffect(Unit) {
        isPlaying = true
        var mediaPlayer: android.media.MediaPlayer? = null
        try {
            mediaPlayer = android.media.MediaPlayer.create(context, co.csedge.cubycare.R.raw.baby_laughing_sound)
            mediaPlayer?.start()
            delay(2000)
            if (mediaPlayer?.isPlaying == true) {
                mediaPlayer.stop()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            mediaPlayer?.release()
        }
    }

    val progress by animateFloatAsState(
        targetValue = if (isPlaying) 1f else 0f,
        animationSpec = tween(durationMillis = 2000, easing = FastOutSlowInEasing),
        finishedListener = { onAnimationFinished() },
        label = "fullscreen_star"
    )

    val configuration = androidx.compose.ui.platform.LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
        val offsetY = screenHeight * 1.2f * progress

        // Confetti!
        Canvas(modifier = Modifier.fillMaxSize()) {
            val colors = listOf(Color.Red, Color.Blue, Color.Green, Color(0xFFFF4500), Color(0xFFFF1493), Color.Cyan, Color.Magenta, Color.Yellow)
            val maxRadius = size.height * 0.8f
            val currentRadius = progress * maxRadius
            val alphaVal = (1f - progress * 1.2f).coerceIn(0f, 1f)

            // Draw confetti burst from bottom center
            for (i in 0 until 40) {
                val angle = (i * 9) * (Math.PI / 180)
                val cx = center.x + (currentRadius * kotlin.math.cos(angle)).toFloat()
                val cy = size.height - (currentRadius * kotlin.math.sin(angle)).toFloat() + (progress * size.height * 0.2f)

                drawCircle(
                    color = colors[i % colors.size].copy(alpha = alphaVal),
                    radius = 6.dp.toPx(),
                    center = Offset(cx, cy)
                )

                val endX = cx + (20.dp.toPx() * kotlin.math.cos(angle - 0.5)).toFloat()
                val endY = cy - (20.dp.toPx() * kotlin.math.sin(angle - 0.5)).toFloat()
                drawLine(
                    color = colors[(i + 3) % colors.size].copy(alpha = alphaVal),
                    start = Offset(cx, cy),
                    end = Offset(endX, endY),
                    strokeWidth = 6.dp.toPx(),
                    cap = StrokeCap.Square
                )
            }
        }

        // The Big Star
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .offset(y = -offsetY)
                .scale(2.5f + kotlin.math.sin(progress * Math.PI * 6).toFloat() * 0.3f)
        ) {
            Icon(
                imageVector = Icons.Filled.Star,
                contentDescription = "Completed Star",
                tint = Color(0xFFFFD700), // Gold/Yellow star
                modifier = Modifier.size(48.dp)
            )
            Canvas(modifier = Modifier.size(20.dp)) {
                // Cheeks
                drawCircle(color = Color(0xFFFFB6C1), radius = 2.dp.toPx(), center = Offset(size.width * 0.25f, size.height * 0.55f))
                drawCircle(color = Color(0xFFFFB6C1), radius = 2.dp.toPx(), center = Offset(size.width * 0.75f, size.height * 0.55f))
                // Eyes
                drawCircle(color = Color(0xFF5C4033), radius = 1.5.dp.toPx(), center = Offset(size.width * 0.35f, size.height * 0.45f))
                drawCircle(color = Color(0xFF5C4033), radius = 1.5.dp.toPx(), center = Offset(size.width * 0.65f, size.height * 0.45f))
                // Calm Smile
                drawPath(
                    path = Path().apply {
                        moveTo(size.width * 0.4f, size.height * 0.6f)
                        quadraticBezierTo(
                            size.width * 0.5f, size.height * 0.7f,
                            size.width * 0.6f, size.height * 0.6f
                        )
                    },
                    color = Color(0xFF5C4033),
                    style = Stroke(width = 1.5.dp.toPx(), cap = StrokeCap.Round)
                )
            }
        }
    }
}

@Composable
fun MilestoneItem(milestone: DevelopmentalMilestone, onToggle: (Boolean) -> Unit) {
    // Local state for instant visual feedback before network sync
    var isCompleted by remember(milestone.id, milestone.isCompleted) { mutableStateOf(milestone.isCompleted) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = isCompleted,
                onCheckedChange = { checked ->
                    isCompleted = checked
                    onToggle(checked)
                },
                colors = CheckboxDefaults.colors(checkedColor = MaterialTheme.colorScheme.primary)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = tr(milestone.title),
                style = MaterialTheme.typography.bodyLarge,
                color = Color.DarkGray,
                modifier = Modifier.weight(1f)
            )
            // Static star to indicate completion
            if (isCompleted) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.padding(start = 8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Star,
                        contentDescription = "Completed Star",
                        tint = Color(0xFFFFD700), // Gold/Yellow star
                        modifier = Modifier.size(28.dp)
                    )
                    // Draw a cute, calm smiling face directly on the star
                    Canvas(modifier = Modifier.size(12.dp)) {
                        // Cheeks
                        drawCircle(color = Color(0xFFFFB6C1), radius = 1.5.dp.toPx(), center = Offset(size.width * 0.25f, size.height * 0.55f))
                        drawCircle(color = Color(0xFFFFB6C1), radius = 1.5.dp.toPx(), center = Offset(size.width * 0.75f, size.height * 0.55f))
                        // Eyes
                        drawCircle(color = Color(0xFF5C4033), radius = 1.dp.toPx(), center = Offset(size.width * 0.35f, size.height * 0.45f))
                        drawCircle(color = Color(0xFF5C4033), radius = 1.dp.toPx(), center = Offset(size.width * 0.65f, size.height * 0.45f))
                        // Calm Smile
                        drawPath(
                            path = Path().apply {
                                moveTo(size.width * 0.4f, size.height * 0.6f)
                                quadraticBezierTo(
                                    size.width * 0.5f, size.height * 0.7f,
                                    size.width * 0.6f, size.height * 0.6f
                                )
                            },
                            color = Color(0xFF5C4033),
                            style = Stroke(width = 1.dp.toPx(), cap = StrokeCap.Round)
                        )
                    }
                }
            }
        }
    }
}
