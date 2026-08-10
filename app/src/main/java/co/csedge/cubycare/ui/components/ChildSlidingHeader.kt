package co.csedge.cubycare.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import co.csedge.cubycare.data.model.Child

@Composable
fun ChildSlidingHeader(
    children: List<Child>,
    currentChildId: String,
    onChildSelected: (Child) -> Unit,
    modifier: Modifier = Modifier
) {
    if (children.size <= 1) return
    val currentIndex = children.indexOfFirst { it.id == currentChildId }.coerceAtLeast(0)

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(
                onClick = {
                    val prevIndex = if (currentIndex > 0) currentIndex - 1 else children.size - 1
                    onChildSelected(children[prevIndex])
                }
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Previous Child",
                    tint = MaterialTheme.colorScheme.primary
                )
            }

            val currentChild = children.getOrElse(currentIndex) { children.first() }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.weight(1f)
            ) {
                ChildProfileAvatar(
                    profileImageUri = currentChild.profileImageUri,
                    name = currentChild.name,
                    size = 40.dp
                )
                Spacer(modifier = Modifier.width(10.dp))
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = currentChild.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = if (currentChild.dateOfBirthMillis == 0L) "All Ages (General)" else "Age: ${currentChild.ageFormatted}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            IconButton(
                onClick = {
                    val nextIndex = (currentIndex + 1) % children.size
                    onChildSelected(children[nextIndex])
                }
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = "Next Child",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}
