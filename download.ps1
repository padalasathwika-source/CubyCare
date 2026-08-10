$client = New-Object System.Net.WebClient
$client.Headers.Add("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64)")
$client.DownloadFile("https://upload.wikimedia.org/wikipedia/commons/d/df/Baby_Laugh.ogg", "c:\Users\bhask\AndroidStudioProjects\CubyCare\app\src\main\res\raw\baby_laugh.ogg")
