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
                matches = re.findall(r'Text\(\s*"([^"]+)"', content)
                for m in matches:
                    strings.add(m)
                
                # Also find placehoders and labels
                matches2 = re.findall(r'placeholder\s*=\s*\{\s*Text\(\s*"([^"]+)"', content)
                for m in matches2:
                    strings.add(m)
                
                matches3 = re.findall(r'label\s*=\s*\{\s*Text\(\s*"([^"]+)"', content)
                for m in matches3:
                    strings.add(m)

with open(r"c:\Users\ABDUL MANNAN\Downloads\CubyCare-main\CubyCare-main\extracted_strings.txt", 'w', encoding='utf-8') as out:
    for s in sorted(list(strings)):
        out.write(s + "\n")
