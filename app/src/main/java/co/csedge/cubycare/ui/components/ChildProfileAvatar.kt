package co.csedge.cubycare.ui.components

import android.graphics.BitmapFactory
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

import androidx.compose.ui.res.painterResource

@Composable
fun ChildProfileAvatar(
    profileImageUri: String?,
    name: String,
    modifier: Modifier = Modifier,
    size: Dp = 64.dp
) {
    val context = LocalContext.current
    var bitmap by remember(profileImageUri) { mutableStateOf<ImageBitmap?>(null) }

    val isGeneral = name.contains("General Baby") || profileImageUri == "general_baby_avatar"

    if (isGeneral) {
        Image(
            painter = painterResource(id = co.csedge.cubycare.R.drawable.general_baby_avatar),
            contentDescription = name,
            contentScale = ContentScale.Crop,
            modifier = modifier
                .size(size)
                .clip(CircleShape)
        )
    } else {
        LaunchedEffect(profileImageUri) {
            if (!profileImageUri.isNullOrBlank()) {
                try {
                    val uri = Uri.parse(profileImageUri)
                    val inputStream = context.contentResolver.openInputStream(uri)
                    val loadedBitmap = BitmapFactory.decodeStream(inputStream)
                    inputStream?.close()
                    if (loadedBitmap != null) {
                        bitmap = loadedBitmap.asImageBitmap()
                    }
                } catch (e: Exception) {
                    bitmap = null
                }
            } else {
                bitmap = null
            }
        }

        if (bitmap != null) {
            Image(
                bitmap = bitmap!!,
                contentDescription = name,
                contentScale = ContentScale.Crop,
                modifier = modifier
                    .size(size)
                    .clip(CircleShape)
            )
        } else {
            // Name Only Initial Avatar
            val initial = if (name.isNotBlank()) name.first().uppercaseChar().toString() else "C"
            Surface(
                shape = CircleShape,
                color = MaterialTheme.colorScheme.primaryContainer,
                modifier = modifier.size(size)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = initial,
                        style = if (size > 50.dp) MaterialTheme.typography.headlineMedium else MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }
        }
    }
}
