@echo off
echo ========================================
echo TestSprite Test Runner
echo ========================================
echo.

echo [1/2] Checking if application is running on port 8080...
netstat -an | findstr ":8080" >nul
if %errorlevel% neq 0 (
    echo [ERROR] Application is not running on port 8080!
    echo Please start the application first:
    echo   gradlew.bat bootRun --args=--spring.profiles.active=test
    echo.
    pause
    exit /b 1
)

echo [OK] Application is running on port 8080
echo.

echo [2/2] Running TestSprite tests...
echo.
node "C:\Users\user\AppData\Local\npm-cache\_npx\8ddf6bea01b2519d\node_modules\@testsprite\testsprite-mcp\dist\index.js" generateCodeAndExecute

echo.
echo ========================================
echo Test execution completed!
echo Check results in:
echo   - testsprite_tests\tmp\raw_report.md
echo   - testsprite_tests\testsprite-mcp-test-report.md
echo ========================================
pause

