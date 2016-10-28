@echo off
where /Q iscc.exe
IF ERRORLEVEL 1 goto notfoundexe
goto foundexe

:notfoundexe
SET location="C:\Program Files (x86)\Inno Setup 5\iscc.exe"
IF EXIST %location% goto foundpath
goto notfoundpath

:notfoundpath
echo Skipping building .exe installer. Inno Setup 5 (iscc.exe) not found path.
goto commonexit

:foundexe
iscc.exe build-windows.iss
goto commonexit

:founpath
"C:\Program Files (x86)\Inno Setup 5\iscc.exe" build-windows.iss
goto commonexit

:commonexit