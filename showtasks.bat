call runcrud.bat

if "%ERRORLEVEL%" == "0" goto openbrowser
echo.
echo runcrud has errors, breaking work
goto fail

:openbrowser
start firefox
if "%ERRORLEVEL%" == "0" goto opentasks
echo.
echo opening firefox has errors, breaking work
goto fail

:opentasks
start http://localhost:8080/crud/v1/tasks
if "%ERRORLEVEL%" == "0" goto end
echo.
echo opening opening this URL has errors, breaking work
goto fail

goto end

:fail
echo.
echo errors occurred

:end
echo.
echo Work of showtask is finished.