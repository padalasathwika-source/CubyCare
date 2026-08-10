import os
import re

files_to_fix = [
    r"c:\Users\ABDUL MANNAN\Downloads\CubyCare-main\CubyCare-main\app\src\main\java\co\csedge\cubycare\ui\dashboard\CubyAlertScreen.kt",
    r"c:\Users\ABDUL MANNAN\Downloads\CubyCare-main\CubyCare-main\app\src\main\java\co\csedge\cubycare\ui\dashboard\CubyParentingScreen.kt"
]

for filepath in files_to_fix:
    with open(filepath, 'r', encoding='utf-8') as f:
        content = f.read()
    
    # We find the main composable function and insert `val conditionsList = emergencyConditions`
    if "CubyAlertScreen.kt" in filepath:
        func_start = content.find("fun CubyAlertScreen(")
        func_body_start = content.find("{", func_start) + 1
        content = content[:func_body_start] + "\n    val conditionsList = emergencyConditions\n" + content[func_body_start:]
        content = content.replace("items(emergencyConditions)", "items(conditionsList)")
    elif "CubyParentingScreen.kt" in filepath:
        func_start = content.find("fun CubyParentingScreen(")
        func_body_start = content.find("{", func_start) + 1
        content = content[:func_body_start] + "\n    val topicsList = parentingTopics\n" + content[func_body_start:]
        content = content.replace("items(parentingTopics)", "items(topicsList)")

    with open(filepath, 'w', encoding='utf-8') as f:
        f.write(content)

print("Fixed LazyColumn items calls.")
