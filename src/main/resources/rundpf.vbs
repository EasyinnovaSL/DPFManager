Dim command
Dim argument
command = "dpf-manager-console.exe"
For i = 0 To WScript.Arguments.Count-1
	argument = WScript.Arguments(i)
	If InStr(argument, " ") > 0 Then
		argument = """" & argument & """"
	End If
	command = command & " " & argument
Next

Dim WinScriptHost
Set WinScriptHost = WScript.CreateObject("WScript.Shell")
Return = WinScriptHost.Run(command, 0, false)
Set WinScriptHost = Nothing