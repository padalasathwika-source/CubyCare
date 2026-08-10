import os
import re

ui_dir = r"c:\Users\ABDUL MANNAN\Downloads\CubyCare-main\CubyCare-main\app\src\main\java\co\csedge\cubycare\ui"

strings = set()

for root, _, files in os.walk(ui_dir):
    for file in files:
        if file.endswith(".kt"):
            filepath = os.path.join(root, file)
            with open(filepath, 'r', encoding='utf-8') as f:
                content = f.read()
                
                # Match Text(text = "...")
                matches = re.findall(r'text\s*=\s*"([^"]+)"', content)
                for m in matches: strings.add(m)
                
                # Match title = "..."
                matches2 = re.findall(r'title\s*=\s*"([^"]+)"', content)
                for m in matches2: strings.add(m)
                
                # Match subtitle = "..."
                matches3 = re.findall(r'subtitle\s*=\s*"([^"]+)"', content)
                for m in matches3: strings.add(m)
                
                # Match description = "..."
                matches4 = re.findall(r'description\s*=\s*"([^"]+)"', content)
                for m in matches4: strings.add(m)

with open(r"c:\Users\ABDUL MANNAN\Downloads\CubyCare-main\CubyCare-main\missed_strings.txt", 'w', encoding='utf-8') as out:
    for s in sorted(list(strings)):
        if "{" not in s and "$" not in s and "App Color Theme" not in s: # skip already processed or complex strings
            out.write(s + "\n")
