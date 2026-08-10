import os
import re

files = [
    r"c:\Users\ABDUL MANNAN\Downloads\CubyCare-main\CubyCare-main\app\src\main\java\co\csedge\cubycare\ui\dashboard\VaccineDetailScreen.kt",
    r"c:\Users\ABDUL MANNAN\Downloads\CubyCare-main\CubyCare-main\app\src\main\java\co\csedge\cubycare\ui\dashboard\VaccinationScreen.kt",
    r"c:\Users\ABDUL MANNAN\Downloads\CubyCare-main\CubyCare-main\app\src\main\java\co\csedge\cubycare\ui\dashboard\MilestoneDetailScreen.kt",
    r"c:\Users\ABDUL MANNAN\Downloads\CubyCare-main\CubyCare-main\app\src\main\java\co\csedge\cubycare\ui\dashboard\MilestoneScreen.kt"
]

replacements = {
    "vaccine.name": "tr(vaccine.name)",
    "vaccine.reason": "tr(vaccine.reason)",
    "vaccine.type": "tr(vaccine.type)",
    "vaccine.recommendedAge": "tr(vaccine.recommendedAge)",
    "milestone.title": "tr(milestone.title)",
    "milestone.domain": "tr(milestone.domain)",
    "milestone.ageRange": "tr(milestone.ageRange)"
}

for filepath in files:
    with open(filepath, 'r', encoding='utf-8') as f:
        content = f.read()
    
    # We use regex to replace whole words only to avoid replacing things like `vaccine.name.hashCode()`
    for old, new in replacements.items():
        # Match only if not followed by a dot (e.g. avoid vaccine.name.hashCode())
        # and not already wrapped in tr(
        pattern = r'(?<!tr\()' + re.escape(old) + r'(?!\.)'
        content = re.sub(pattern, new, content)
        
    if "import co.csedge.cubycare.utils.tr" not in content:
        last_import = content.rfind("import ")
        if last_import != -1:
            eol = content.find("\n", last_import)
            content = content[:eol+1] + "import co.csedge.cubycare.utils.tr\n" + content[eol+1:]

    with open(filepath, 'w', encoding='utf-8') as f:
        f.write(content)

print("Wrapped fields with tr() successfully.")
