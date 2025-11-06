# Script to check application and run TestSprite tests

Write-Host "Checking if application is running on port 8080..."
$portCheck = Test-NetConnection -ComputerName localhost -Port 8080 -InformationLevel Quiet -WarningAction SilentlyContinue

if ($portCheck) {
    Write-Host "✓ Application is running on port 8080" -ForegroundColor Green
    Write-Host "Starting TestSprite tests..." -ForegroundColor Yellow
    
    $testScript = "C:\Users\user\AppData\Local\npm-cache\_npx\8ddf6bea01b2519d\node_modules\@testsprite\testsprite-mcp\dist\index.js"
    
    if (Test-Path $testScript) {
        node $testScript generateCodeAndExecute
    } else {
        Write-Host "✗ TestSprite script not found at: $testScript" -ForegroundColor Red
    }
} else {
    Write-Host "✗ Application is NOT running on port 8080" -ForegroundColor Red
    Write-Host ""
    Write-Host "Please start the application first with:" -ForegroundColor Yellow
    Write-Host "  .\gradlew.bat bootRun --args='--spring.profiles.active=test'" -ForegroundColor Cyan
    Write-Host ""
    Write-Host "Or if you have PostgreSQL available:" -ForegroundColor Yellow
    Write-Host "  `$env:dbUsername='your_username'; `$env:dbPassword='your_password'; .\gradlew.bat bootRun" -ForegroundColor Cyan
}

