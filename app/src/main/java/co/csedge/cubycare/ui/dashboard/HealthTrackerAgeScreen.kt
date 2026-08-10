package co.csedge.cubycare.ui.dashboard

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import co.csedge.cubycare.data.model.Child
import co.csedge.cubycare.utils.currentVitalsAgeRange

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HealthTrackerAgeScreen(
    child: Child,
    onBack: () -> Unit,
    onAgeBlockClick: (String) -> Unit,
    bottomBar: @Composable () -> Unit = {}
) {
    val isGeneralProfile = child.id == "default_general_baby" || child.name.contains("General Baby") || child.dateOfBirthMillis == 0L
    var selectedAgeRange by remember { mutableStateOf<String?>(if (isGeneralProfile) null else child.currentVitalsAgeRange) }

    if (selectedAgeRange != null) {
        HealthTrackerDetailScreen(
            child = child,
            ageRange = selectedAgeRange!!,
            onBack = { selectedAgeRange = null }
        )
    } else {
        val ageRanges = listOf("0-6 Months", "6-12 Months", "1-5 Years")

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Cuby Vitals") },
                    navigationIcon = {
                        IconButton(onClick = onBack) {
                            Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surface,
                        titleContentColor = MaterialTheme.colorScheme.onSurface
                    )
                )
            },
            bottomBar = bottomBar,
            containerColor = Color.Transparent
        ) { paddingValues ->
            Box(modifier = Modifier.fillMaxSize()) {
                Image(
                    painter = painterResource(id = co.csedge.cubycare.R.drawable.mother_baby_vitals_bg),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize(),
                    alpha = 0.20f
                )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(ageRanges) { ageRange ->
                        val hue = Math.abs(ageRange.hashCode()) % 360f
                        val blockColor = Color.hsl(hue = hue, saturation = 0.4f, lightness = 0.90f)

                        Card(
                            modifier = Modifier
                                .size(140.dp)
                                .clickable { onAgeBlockClick(ageRange) },
                            shape = RoundedCornerShape(24.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                        ) {
                            Column(
                                modifier = Modifier.fillMaxSize().padding(12.dp),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Favorite,
                                    contentDescription = ageRange,
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(44.dp).padding(bottom = 6.dp)
                                )
                                Text(
                                    text = ageRange.replace(" ", "\n"),
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
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
