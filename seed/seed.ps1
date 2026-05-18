# Seed script — popula o banco com dados fictícios para desenvolvimento.
# Uso: .\seed\seed.ps1
# Requer o servidor rodando em http://localhost:8080

$ErrorActionPreference = "Stop"
$base = "http://localhost:8080/api"
$seed = "$PSScriptRoot"
$utf8NoBom = New-Object System.Text.UTF8Encoding $false
$tmpFile = "$seed\plantao_tmp.json"

function Post-Json($url, $jsonFile) {
    $resp = curl.exe -s -o - -w "`n%{http_code}" -X POST $url -H "Content-Type: application/json" --data-binary "@$jsonFile"
    $lines = $resp -split "`n"
    $body = $lines[0]
    $code = $lines[1]
    if ($code -ne "200") {
        Write-Host "  ERRO $code: $body" -ForegroundColor Red
        return $null
    }
    return $body
}

# ── Profissionais ──────────────────────────────────────────────────────────────
Write-Host ""
Write-Host "Cadastrando profissionais..." -ForegroundColor Cyan

$profFiles = @("p1.json","p2.json","p3.json","p4.json","p5.json","p6.json","p7.json")
$ids = @()

foreach ($file in $profFiles) {
    $resp = Post-Json "$base/professionals" "$seed\$file"
    if ($null -ne $resp) {
        $ids += [long]$resp
        $name = (Get-Content "$seed\$file" | ConvertFrom-Json).name
        Write-Host "  OK  $name (ID $resp)"
    } else {
        $ids += 0
    }
}

# ── Plantões ───────────────────────────────────────────────────────────────────
Write-Host ""
Write-Host "Cadastrando plantoes para os proximos 7 dias..." -ForegroundColor Cyan

$dates = 0..6 | ForEach-Object { (Get-Date).AddDays($_).ToString("yyyy-MM-dd") }

$plantoes = @(
    # Dia +0: plantao isolado
    @{ i = 2; data = $dates[0]; turno = "MANHA" }   # Dr. Ricardo Lima

    # Dia +1: plantoes isolados em turnos distintos
    @{ i = 4; data = $dates[1]; turno = "TARDE" }   # Enf. Marcos Oliveira
    @{ i = 6; data = $dates[1]; turno = "NOITE" }   # Tec. Bruno Almeida

    # Dia +2: MANHA + TARDE no mesmo dia (2 medicos)
    @{ i = 0; data = $dates[2]; turno = "MANHA" }   # Dr. Carlos Mendes
    @{ i = 1; data = $dates[2]; turno = "TARDE" }   # Dra. Ana Paula Ribeiro

    # Dia +3: plantao isolado
    @{ i = 2; data = $dates[3]; turno = "NOITE" }   # Dr. Ricardo Lima

    # Dia +4: mesmo medico com plantao MANHA + TARDE + NOITE no mesmo dia
    @{ i = 0; data = $dates[4]; turno = "MANHA" }   # Dr. Carlos Mendes
    @{ i = 0; data = $dates[4]; turno = "TARDE" }   # Dr. Carlos Mendes
    @{ i = 0; data = $dates[4]; turno = "NOITE" }   # Dr. Carlos Mendes

    # Dia +5: plantoes isolados em turnos distintos
    @{ i = 6; data = $dates[5]; turno = "MANHA" }   # Tec. Bruno Almeida
    @{ i = 1; data = $dates[5]; turno = "TARDE" }   # Dra. Ana Paula Ribeiro

    # Dia +6: plantao isolado
    @{ i = 4; data = $dates[6]; turno = "MANHA" }   # Enf. Marcos Oliveira
)

foreach ($p in $plantoes) {
    $pid = $ids[$p.i]
    if ($pid -le 0) {
        Write-Host "  SKIP $($p.data) $($p.turno) (profissional nao cadastrado)" -ForegroundColor Yellow
        continue
    }
    $json = '{"professionalId":' + $pid + ',"data":"' + $p.data + '","turno":"' + $p.turno + '"}'
    [System.IO.File]::WriteAllText($tmpFile, $json, $utf8NoBom)
    $resp = Post-Json "$base/plantoes" $tmpFile
    if ($null -ne $resp) {
        $r = $resp | ConvertFrom-Json
        Write-Host "  OK  $($r.data) $($r.turno) — $($r.professionalName)"
    }
}

Remove-Item $tmpFile -ErrorAction SilentlyContinue

# ── Resumo ─────────────────────────────────────────────────────────────────────
Write-Host ""
Write-Host "Escala cadastrada (proximos 7 dias a partir de hoje):" -ForegroundColor Cyan
$escala = curl.exe -s "$base/escala?data=$($dates[0])" | ConvertFrom-Json
foreach ($prof in $escala) {
    Write-Host "  $($prof.professionalName) [$($prof.professionalCategory)]"
    foreach ($pl in $prof.plantoes) {
        Write-Host "    $($pl.data) - $($pl.turno)"
    }
}
Write-Host ""
