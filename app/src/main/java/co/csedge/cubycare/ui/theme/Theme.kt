package co.csedge.cubycare.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

data class AppThemeOption(
    val id: String,
    val name: String,
    val primaryColor: Color,
    val containerColor: Color,
    val isDark: Boolean = false
)

val AvailableAppThemes = listOf(
    AppThemeOption("Ocean Blue", "Ocean Blue", Color(0xFF4A90E2), Color(0xFFD6E8FA)),
    AppThemeOption("Rose Blossom", "Rose Blossom", Color(0xFFE91E63), Color(0xFFFCE4EC)),
    AppThemeOption("Emerald Mint", "Emerald Mint", Color(0xFF10B981), Color(0xFFD1FAE5)),
    AppThemeOption("Lavender Sunset", "Lavender Sunset", Color(0xFF8B5CF6), Color(0xFFEDE9FE)),
    AppThemeOption("Warm Gold", "Warm Gold", Color(0xFFF59E0B), Color(0xFFFEF3C7)),
    AppThemeOption("Dark Mode", "Dark Mode", Color(0xFF38BDF8), Color(0xFF1E293B), isDark = true),
    AppThemeOption("System", "System Default", Color(0xFF0277BD), Color(0xFFE1F5FE))
)

private val LightColorScheme = lightColorScheme(
    primary = PrimaryBlue,
    onPrimary = Color.White,
    primaryContainer = PrimaryBlueLight,
    onPrimaryContainer = PrimaryBlueDark,
    
    secondary = SecondaryGreen,
    onSecondary = Color.White,
    secondaryContainer = SecondaryGreenLight,
    onSecondaryContainer = SecondaryGreenDark,
    
    tertiary = PrimaryBlue,
    onTertiary = Color.White,
    tertiaryContainer = PrimaryBlueLight,
    onTertiaryContainer = PrimaryBlueDark,
    
    error = ErrorRed,
    errorContainer = ErrorRedLight,
    onError = Color.White,
    onErrorContainer = ErrorRed,
    
    background = SurfaceWarm,
    onBackground = TextPrimary,
    
    surface = Color.White,
    onSurface = TextPrimary,
    surfaceVariant = SurfaceVariantWarm,
    onSurfaceVariant = TextSecondary
)

private val RoseColorScheme = lightColorScheme(
    primary = Color(0xFFE91E63),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFFCE4EC),
    onPrimaryContainer = Color(0xFF880E4F),
    secondary = Color(0xFFEC407A),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFF8BBD0),
    onSecondaryContainer = Color(0xFFC2185B),
    background = Color(0xFFFFF5F7),
    onBackground = Color(0xFF0F172A),
    surface = Color.White,
    onSurface = Color(0xFF0F172A),
    onSurfaceVariant = Color(0xFF334155)
)

private val MintColorScheme = lightColorScheme(
    primary = Color(0xFF10B981),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFD1FAE5),
    onPrimaryContainer = Color(0xFF064E3B),
    secondary = Color(0xFF059669),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFA7F3D0),
    onSecondaryContainer = Color(0xFF047857),
    background = Color(0xFFF0FDF4),
    onBackground = Color(0xFF0F172A),
    surface = Color.White,
    onSurface = Color(0xFF0F172A),
    onSurfaceVariant = Color(0xFF334155)
)

private val LavenderColorScheme = lightColorScheme(
    primary = Color(0xFF8B5CF6),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFEDE9FE),
    onPrimaryContainer = Color(0xFF4C1D95),
    secondary = Color(0xFF7C3AED),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFDDD6FE),
    onSecondaryContainer = Color(0xFF5B21B6),
    background = Color(0xFFFAF5FF),
    onBackground = Color(0xFF0F172A),
    surface = Color.White,
    onSurface = Color(0xFF0F172A),
    onSurfaceVariant = Color(0xFF334155)
)

private val GoldColorScheme = lightColorScheme(
    primary = Color(0xFFD97706),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFFEF3C7),
    onPrimaryContainer = Color(0xFF78350F),
    secondary = Color(0xFFF59E0B),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFFDE68A),
    onSecondaryContainer = Color(0xFF92400E),
    background = Color(0xFFFFFBEB),
    onBackground = Color(0xFF0F172A),
    surface = Color.White,
    onSurface = Color(0xFF0F172A),
    onSurfaceVariant = Color(0xFF334155)
)

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF38BDF8),
    onPrimary = Color(0xFF0F172A),
    primaryContainer = Color(0xFF0369A1),
    onPrimaryContainer = Color(0xFFE0F2FE),
    
    secondary = Color(0xFF34D399),
    onSecondary = Color(0xFF064E3B),
    secondaryContainer = Color(0xFF047857),
    onSecondaryContainer = Color(0xFFD1FAE5),
    
    tertiary = Color(0xFF818CF8),
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFF4338CA),
    onTertiaryContainer = Color(0xFFE0E7FF),
    
    error = Color(0xFFF87171),
    errorContainer = Color(0xFF7F1D1D),
    onError = Color.White,
    onErrorContainer = Color(0xFFFEE2E2),
    
    background = Color(0xFF0F172A),
    onBackground = Color(0xFFF8FAFC),
    
    surface = Color(0xFF1E293B),
    onSurface = Color(0xFFF8FAFC),
    surfaceVariant = Color(0xFF334155),
    onSurfaceVariant = Color(0xFFCBD5E1)
)

@Composable
fun CubyCareTheme(
    themeMode: String = "Ocean Blue",
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val isSystemDark = isSystemInDarkTheme()
    val colorScheme = when (themeMode) {
        "Rose Blossom" -> RoseColorScheme
        "Emerald Mint" -> MintColorScheme
        "Lavender Sunset" -> LavenderColorScheme
        "Warm Gold" -> GoldColorScheme
        "Dark Mode", "Midnight Dark", "Dark" -> DarkColorScheme
        "Light" -> LightColorScheme
        "System" -> if (isSystemDark) DarkColorScheme else LightColorScheme
        else -> LightColorScheme
    }

    val isDarkBar = themeMode == "Dark Mode" || themeMode == "Midnight Dark" || themeMode == "Dark" || (themeMode == "System" && isSystemDark)

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !isDarkBar
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
