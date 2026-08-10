import os

filepath = r"c:\Users\ABDUL MANNAN\Downloads\CubyCare-main\CubyCare-main\app\src\main\java\co\csedge\cubycare\ui\dashboard\NutritionAgeDetailScreen.kt"
with open(filepath, 'r', encoding='utf-8') as f:
    content = f.read()

# Replace all `tr(` inside `Scaffold` body (which includes LazyColumn) with `trNonComposable(`
# Since `tr` is used in top app bar too which is composable, it's safe to just replace ALL `tr(` inside the file with `trNonComposable(`!
content = content.replace('tr(', 'trNonComposable(')

# But we must define `val trNonComposable = ...` at the top of NutritionAgeDetailScreen composable!
insertion_point = content.find('fun NutritionAgeDetailScreen(')
insertion_point = content.find('{', insertion_point) + 1

lambda_def = '''
    val currentLang = co.csedge.cubycare.utils.LocalAppLanguage.current
    val trNonComposable: (String) -> String = { key ->
        co.csedge.cubycare.utils.AppLanguageManager.getString(key, currentLang)
    }
'''
content = content[:insertion_point] + lambda_def + content[insertion_point:]

with open(filepath, 'w', encoding='utf-8') as f:
    f.write(content)

print("Fixed NutritionAgeDetailScreen.")
