package co.csedge.cubycare.ui.dashboard

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import co.csedge.cubycare.data.model.Child

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CubyHealthSelectionScreen(
    child: Child,
    onBack: () -> Unit,
    onNapsClick: () -> Unit,
    onVitalsClick: () -> Unit,
    onSmileClick: () -> Unit,
    bottomBar: @Composable () -> Unit = {}
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Cuby Health") },
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

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "What would you like to track?",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(bottom = 32.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    // Cuby Naps Block
                    Card(
                        modifier = Modifier
                            .size(140.dp)
                            .clickable { onNapsClick() },
                        shape = RoundedCornerShape(24.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFE8EAF6).copy(alpha = 0.9f)),
                        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                    ) {
                        Column(
                            modifier = Modifier.fillMaxSize().padding(8.dp),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Image(
                                painter = painterResource(id = co.csedge.cubycare.R.drawable.cuby_naps_icon),
                                contentDescription = "Cuby Naps",
                                modifier = Modifier.size(60.dp).padding(bottom = 6.dp),
                                contentScale = ContentScale.Fit
                            )
                            Text(
                                text = "Cuby\nNaps",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color.DarkGray,
                                textAlign = TextAlign.Center
                            )
                        }
                    }

                    // Cuby Vitals Block
                    Card(
                        modifier = Modifier
                            .size(140.dp)
                            .clickable { onVitalsClick() },
                        shape = RoundedCornerShape(24.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFFCE4EC).copy(alpha = 0.9f)),
                        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                    ) {
                        Column(
                            modifier = Modifier.fillMaxSize().padding(8.dp),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Image(
                                painter = painterResource(id = co.csedge.cubycare.R.drawable.vital),
                                contentDescription = "Cuby Vitals",
                                modifier = Modifier.size(60.dp).padding(bottom = 6.dp),
                                contentScale = ContentScale.Fit
                            )
                            Text(
                                text = "Cuby\nVitals",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color.DarkGray,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    // Cuby Smile Block
                    Card(
                        modifier = Modifier
                            .size(140.dp)
                            .clickable { onSmileClick() },
                        shape = RoundedCornerShape(24.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFE0F7FA).copy(alpha = 0.9f)),
                        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                    ) {
                        Column(
                            modifier = Modifier.fillMaxSize().padding(8.dp),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Image(
                                painter = painterResource(id = co.csedge.cubycare.R.drawable.dental),
                                contentDescription = "Cuby Smile",
                                modifier = Modifier.size(60.dp).padding(bottom = 6.dp),
                                contentScale = ContentScale.Fit
                            )
                            Text(
                                text = "Cuby\nSmile",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color.DarkGray,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        }
    }
}
