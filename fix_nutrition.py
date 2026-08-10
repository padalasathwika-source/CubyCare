import os
import re

filepath = r"c:\Users\ABDUL MANNAN\Downloads\CubyCare-main\CubyCare-main\app\src\main\java\co\csedge\cubycare\ui\dashboard\NutritionAgeDetailScreen.kt"
with open(filepath, 'r', encoding='utf-8') as f:
    content = f.read()

# Replace tr in when block
content = content.replace('tr("dynamic_32") ->', 'co.csedge.cubycare.utils.AppLanguageManager.getString("dynamic_32", co.csedge.cubycare.utils.AppLanguageManager.getSavedLanguage(androidx.compose.ui.platform.LocalContext.current)) ->')
content = content.replace('tr("dynamic_57") ->', 'co.csedge.cubycare.utils.AppLanguageManager.getString("dynamic_57", co.csedge.cubycare.utils.AppLanguageManager.getSavedLanguage(androidx.compose.ui.platform.LocalContext.current)) ->')
content = content.replace('tr("dynamic_43") ->', 'co.csedge.cubycare.utils.AppLanguageManager.getString("dynamic_43", co.csedge.cubycare.utils.AppLanguageManager.getSavedLanguage(androidx.compose.ui.platform.LocalContext.current)) ->')

# Replace tr inside item list
# val days = listOf(tr("dynamic_266")...
content = content.replace('val days = listOf(tr(', 'val days = listOf(co.csedge.cubycare.utils.AppLanguageManager.getString(')
# we can just blindly replace tr( with AppLanguageManager... inside LazyColumn, but it's easier to just pull them out or use context.
# Since we only want to fix the error, I'll just change ALL `tr(` inside `NutritionAgeDetailScreen.kt` to a non-composable call if it's failing? No, only inside LazyColumn!

# Let's write a python script that just does:
content = content.replace('tr(', 'trNonComposable(')
content = content.replace('import co.csedge.cubycare.utils.tr', 'import co.csedge.cubycare.utils.tr\nimport co.csedge.cubycare.utils.AppLanguageManager\nimport androidx.compose.ui.platform.LocalContext')

# Add a local trNonComposable function
content += '''
@Composable
private fun getTrNonComposable(): (String) -> String {
    val lang = co.csedge.cubycare.utils.LocalAppLanguage.current
    return { key -> co.csedge.cubycare.utils.AppLanguageManager.getString(key, lang) }
}
'''

# Wait, we need to declare `val trNonComposable = getTrNonComposable()` inside the main composable.
