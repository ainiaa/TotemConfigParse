#Region ;**** 参数创建于 ACNWrapper_GUI ****
#PRE_Icon=C:\Users\Administrator\Desktop\bitbug_favicon.ico
#PRE_Compile_Both=y
#PRE_Res_requestedExecutionLevel=None
#EndRegion ;**** 参数创建于 ACNWrapper_GUI ****
#include <MsgBoxConstants.au3>
#include <FileConstants.au3>


Func getJarFilePath()
	Local $jarFilePath = "./DessertShopConfigParse.jar"
	If Not FileExists($jarFilePath) Then
		; Create a constant variable in Local scope of the message to display in FileOpenDialog.
		Local Const $sMessage = "Hold down Ctrl or Shift to choose multiple files."

		; Display an open dialog to select a list of file(s).
		Local $jarFilePath = FileOpenDialog($sMessage, @WindowsDir & "\", "Jar (*.jar)", $FD_FILEMUSTEXIST + $FD_MULTISELECT)
		If @error Then
			FileChangeDir(@ScriptDir)
			$jarFilePath = False
			MsgBox($MB_SYSTEMMODAL, "", "No file(s) were selected.")
			
		Else
			FileChangeDir(@ScriptDir)
		EndIf
	EndIf
	
	Return $jarFilePath
EndFunc

Local $jarFilePath = getJarFilePath()

If $jarFilePath Then
	Local $finalCmd = "javaw.exe" & " -jar """ & $jarFilePath & """"
	;MsgBox($MB_SYSTEMMODAL, "", "final cmd:" & @CRLF & $finalCmd)
	Run($finalCmd, "", @SW_HIDE)
EndIf