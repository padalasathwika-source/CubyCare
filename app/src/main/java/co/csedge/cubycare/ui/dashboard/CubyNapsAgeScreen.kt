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
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import co.csedge.cubycare.data.model.Child
import co.csedge.cubycare.data.repository.SleepCycleProvider

import androidx.compose.foundation.lazy.grid.GridItemSpan

import co.csedge.cubycare.utils.currentVitalsAgeRange

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CubyNapsAgeScreen(
    child: Child,
    onBack: () -> Unit,
    onAgeBlockClick: (String) -> Unit
) {
    val isGeneralProfile = child.id == "default_general_baby" || child.name.contains("General Baby") || child.dateOfBirthMillis == 0L
    var selectedAgeRange by remember { mutableStateOf<String?>(if (isGeneralProfile) null else child.currentVitalsAgeRange) }

    if (selectedAgeRange != null) {
        CubyNapsDetailScreen(
            child = child,
            ageRange = selectedAgeRange!!,
            onBack = { selectedAgeRange = null }
        )
    } else {
        val ageRanges = SleepCycleProvider.ageRanges
        val colors = listOf(
            Color(0xFFE3F2FD),
            Color(0xFFF3E5F5),
            Color(0xFFE8EAF6)
        )

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Cuby Naps") },
                    navigationIcon = {
                        IconButton(onClick = onBack) {
                            Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color(0xFFE8EAF6),
                        titleContentColor = Color.DarkGray
                    )
                )
            },
            containerColor = Color.Transparent
        ) { paddingValues ->
            Box(modifier = Modifier.fillMaxSize()) {
                Image(
                    painter = painterResource(id = co.csedge.cubycare.R.drawable.cuby_naps_bg),
                    contentDescription = "Background",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize(),
                    alpha = 0.20f
                )

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp),
                contentPadding = PaddingValues(top = 16.dp, bottom = 80.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item(span = { GridItemSpan(2) }) {
                    Column {
                        Text(
                            text = "Sleep Cycle",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        Text(
                            text = "Select an age range to view recommended sleep duration, naps, and night awakenings.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )
                    }
                }

                items(ageRanges) { ageRange ->
                        val colorIndex = ageRanges.indexOf(ageRange) % colors.size
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(1f)
                                .clickable { onAgeBlockClick(ageRange) },
                            shape = RoundedCornerShape(24.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                        ) {
                            Column(
                                modifier = Modifier.fillMaxSize().padding(12.dp),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Star,
                                    contentDescription = ageRange,
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(44.dp).padding(bottom = 6.dp)
                                )
                                Text(
                                    text = ageRange,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
