[byte[]]$header = new-object byte[] 44
[byte[]]$data = new-object byte[] 88200
for ($i = 0; $i -lt 44100; $i++) {
    $val = [int16]([math]::Sin(2 * [math]::PI * 440 * $i / 44100) * 16383)
    $data[$i*2] = [byte]($val -band 0xFF)
    $data[$i*2+1] = [byte](($val -shr 8) -band 0xFF)
}
$len = $data.Length
$riff = [System.Text.Encoding]::ASCII.GetBytes("RIFF")
$wave = [System.Text.Encoding]::ASCII.GetBytes("WAVE")
$fmt = [System.Text.Encoding]::ASCII.GetBytes("fmt ")
$dataStr = [System.Text.Encoding]::ASCII.GetBytes("data")
[System.Array]::Copy($riff, 0, $header, 0, 4)
$filelen = [BitConverter]::GetBytes($len + 36)
[System.Array]::Copy($filelen, 0, $header, 4, 4)
[System.Array]::Copy($wave, 0, $header, 8, 4)
[System.Array]::Copy($fmt, 0, $header, 12, 4)
[System.Array]::Copy([BitConverter]::GetBytes(16), 0, $header, 16, 4)
[System.Array]::Copy([BitConverter]::GetBytes([int16]1), 0, $header, 20, 2)
[System.Array]::Copy([BitConverter]::GetBytes([int16]1), 0, $header, 22, 2)
[System.Array]::Copy([BitConverter]::GetBytes(44100), 0, $header, 24, 4)
[System.Array]::Copy([BitConverter]::GetBytes(88200), 0, $header, 28, 4)
[System.Array]::Copy([BitConverter]::GetBytes([int16]2), 0, $header, 32, 2)
[System.Array]::Copy([BitConverter]::GetBytes([int16]16), 0, $header, 34, 2)
[System.Array]::Copy($dataStr, 0, $header, 36, 4)
[System.Array]::Copy([BitConverter]::GetBytes($len), 0, $header, 40, 4)

$file = [System.IO.File]::Create("c:\Users\bhask\AndroidStudioProjects\CubyCare\app\src\main\res\raw\baby_laugh.wav")
$file.Write($header, 0, 44)
$file.Write($data, 0, $len)
$file.Close()
