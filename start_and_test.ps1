# PowerShell script to start application and run tests

$ErrorActionPreference = "Continue"

Write-Host "========================================" -ForegroundColor Cyan
Write-Host "TestSprite Test Runner" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

# Step 1: Start application
Write-Host "[1/3] Starting Spring Boot application..." -ForegroundColor Yellow
$appProcess = Start-Process -FilePath ".\gradlew.bat" -ArgumentList "bootRun","--args=--spring.profiles.active=test" -PassThru -WindowStyle Hidden

Write-Host "Application process started (PID: $($appProcess.Id))" -ForegroundColor Gray
Write-Host "Waiting for application to start (this may take 30-60 seconds)..." -ForegroundColor Gray

# Step 2: Wait for port 8080
$maxWait = 90
$waited = 0
$portOpen = $false

while ($waited -lt $maxWait -and -not $portOpen) {
    Start-Sleep -Seconds 3
    $waited += 3
    try {
        $portOpen = Test-NetConnection -ComputerName localhost -Port 8080 -InformationLevel Quiet -WarningAction SilentlyContinue
        if ($portOpen) {
            Write-Host "[2/3] ✓ Application is running on port 8080!" -ForegroundColor Green
            break
        } else {
            Write-Host "  Still waiting... ($waited/$maxWait seconds)" -ForegroundColor Gray
        }
    } catch {
        Write-Host "  Checking port... ($waited/$maxWait seconds)" -ForegroundColor Gray
    }
}

if (-not $portOpen) {
    Write-Host "[2/3] ✗ Application failed to start within $maxWait seconds" -ForegroundColor Red
    Write-Host "Please check the application logs manually." -ForegroundColor Yellow
    Stop-Process -Id $appProcess.Id -Force -ErrorAction SilentlyContinue
    exit 1
}

# Step 3: Run tests
Write-Host "[3/3] Running TestSprite tests..." -ForegroundColor Yellow
$testScript = "C:\Users\user\AppData\Local\npm-cache\_npx\8ddf6bea01b2519d\node_modules\@testsprite\testsprite-mcp\dist\index.js"

if (-not (Test-Path $testScript)) {
    Write-Host "✗ TestSprite script not found at: $testScript" -ForegroundColor Red
    Write-Host "Please check the path or install TestSprite." -ForegroundColor Yellow
    exit 1
}

Write-Host "Executing: node $testScript generateCodeAndExecute" -ForegroundColor Gray
Write-Host ""

try {
    & node $testScript generateCodeAndExecute
    $testResult = $LASTEXITCODE
    
    if ($testResult -eq 0) {
        Write-Host ""
        Write-Host "========================================" -ForegroundColor Green
        Write-Host "✓ Tests completed successfully!" -ForegroundColor Green
        Write-Host "========================================" -ForegroundColor Green
        Write-Host ""
        Write-Host "Check results in:" -ForegroundColor Cyan
        Write-Host "  - testsprite_tests\tmp\raw_report.md" -ForegroundColor White
        Write-Host "  - testsprite_tests\testsprite-mcp-test-report.md" -ForegroundColor White
    } else {
        Write-Host ""
        Write-Host "✗ Tests completed with exit code: $testResult" -ForegroundColor Yellow
    }
} catch {
    Write-Host "✗ Error running tests: $_" -ForegroundColor Red
    $testResult = 1
}

Write-Host ""
Write-Host "Stopping application..." -ForegroundColor Gray
Stop-Process -Id $appProcess.Id -Force -ErrorAction SilentlyContinue

exit $testResult

