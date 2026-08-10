Add-Type -AssemblyName System.Drawing
$names = @(
    'mother_baby_growth_bg', 'mother_baby_vaccine_bg', 'mother_baby_nutrition_bg', 
    'mother_baby_joy_bg', 'mother_baby_health_bg', 'mother_baby_smile_bg', 'mother_baby_vitals_bg',
    'cuby_alert_icon', 'cuby_health_icon', 'disorders_care_icon', 'allergy_care_icon',
    'cuby_vitals_icon', 'cuby_smile_icon', 'cuby_ai_robot_mother_baby'
)
foreach ($name in $names) {
    $path = "c:\Users\ABDUL MANNAN\Downloads\CubyCare-main\CubyCare-main\app\src\main\res\drawable\$name.png"
    if (Test-Path $path) {
        $img = [System.Drawing.Image]::FromFile($path)
        $tmp = "$path.tmp.png"
        $img.Save($tmp, [System.Drawing.Imaging.ImageFormat]::Png)
        $img.Dispose()
        Move-Item -Path $tmp -Destination $path -Force
        Write-Host "Converted $name.png to valid PNG"
    }
}
