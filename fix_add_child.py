import os

file_path = r"c:\Users\ABDUL MANNAN\Downloads\CubyCare-main\CubyCare-main\app\src\main\java\co\csedge\cubycare\ui\profile\AddChildScreen.kt"

with open(file_path, 'r', encoding='utf-8') as f:
    content = f.read()

replacements = {
    'question = "When was $name born?"': 'question = tr("addchild_q_dob").replace("$name", name)',
    'description = "Enter the Date of Birth."': 'description = tr("addchild_desc_dob")',
    'placeholder = "DD/MM/YYYY"': 'placeholder = tr("addchild_ph_dob")',
    
    'question = "What is $name\'s gender?"': 'question = tr("addchild_q_gender").replace("$name", name)',
    'description = "Optional."': 'description = tr("addchild_desc_optional")',
    'placeholder = "Boy / Girl / Other"': 'placeholder = tr("addchild_ph_gender")',
    
    'question = "What was $name\'s birth weight?"': 'question = tr("addchild_q_weight").replace("$name", name)',
    'description = "This helps us track growth accurately."': 'description = tr("addchild_desc_weight")',
    'placeholder = "e.g., 3.2 kg"': 'placeholder = tr("addchild_ph_weight")',
    
    'question = "What was the birth length?"': 'question = tr("addchild_q_length")',
    'description = "Length or height at birth."': 'description = tr("addchild_desc_length")',
    'placeholder = "e.g., 50 cm"': 'placeholder = tr("addchild_ph_length")',
    
    'question = "Head circumference at birth?"': 'question = tr("addchild_q_head")',
    'description = "Important for early development tracking."': 'description = tr("addchild_desc_head")',
    'placeholder = "e.g., 35 cm"': 'placeholder = tr("addchild_ph_head")',
    
    'question = "What is $name\'s blood group?"': 'question = tr("addchild_q_blood").replace("$name", name)',
    'description = "Good to have on record."': 'description = tr("addchild_desc_blood")',
    'placeholder = "e.g., O+, A-"': 'placeholder = tr("addchild_ph_blood")',
    
    'question = "Any genetic issues?"': 'question = tr("addchild_q_genetic")',
    'description = "Leave blank if none."': 'description = tr("addchild_desc_blank")',
    'placeholder = "Type here..."': 'placeholder = tr("addchild_ph_type")',
    
    'question = "Does $name have any allergies?"': 'question = tr("addchild_q_allergies").replace("$name", name)',
    
    'question = "Any medical conditions?"': 'question = tr("addchild_q_medical")',
    
    'Text(\n                                                if (profileImageUri.isNotBlank()) "Change Photo" else "Upload Photo (Optional)",': 'Text(\n                                                if (profileImageUri.isNotBlank()) tr("change_photo") else tr("upload_photo"),'
}

for old, new in replacements.items():
    content = content.replace(old, new)

if "import co.csedge.cubycare.utils.tr" not in content:
    last_import_idx = content.rfind("import ")
    end_of_line = content.find("\n", last_import_idx)
    content = content[:end_of_line+1] + "import co.csedge.cubycare.utils.tr\n" + content[end_of_line+1:]

with open(file_path, 'w', encoding='utf-8') as f:
    f.write(content)

print("AddChildScreen updated.")
