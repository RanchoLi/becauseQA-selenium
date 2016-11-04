;------------------------------------------------------------------------------------
;Change Logs
;2016.09.09 V 1.0.2
;     1. Modify the java installation exception error for errorcode :1618
;2016.07.26 V 1.0.1
;     1. Project build from maven script ,invoke project properties in maven;
;2016.06.28 V 1.0.0
;     1. install jre in specified folder as user choosed
;     2. Modify jre8 silent install command as refer:
;     http://docs.oracle.com/javase/8/docs/technotes/guides/install/config.html#table_config_file_options
;     Static install,remove the previous old jre,no prompt for applet or java web start, no update ,no reboot needed
;     Also install the jre as you put directory
;     INSTALL_SILENT=1 STATIC=1 REMOVEOUTOFDATEJRES=1 EULA=1 AUTO_UPDATE=0 REBOOT=0 INSTALLDIR="$INSTDIR\jre" /L "$TEMP\jre_setup.log"
;     Add the JRE_HOME,CLASS_PATH,PATH ins Environment path
;     
;2016.06.27 V 1.0.0
;     1. Modify the app name ;
;     2. Add the file version info,refer page: 
;       https://sourceforge.net/p/portableapps/launcher/ci/2.2.dev/tree/Other/Source/GeneratorWizard.nsi
;     3. Re-install use the previous install path from registery value;
;     4. Add detail jdk check log message also only delete the jre file if install it successfully.
;2016.06.26 V 1.0.0   
;     1. Add close the app warning message when unintall it
;     2. Add the multiply language support ,special for SimpleChinese with encode UTF-8
;     3. Add the flash image before install and uninstall 
;
;------------------------------------------------------------------------------------

; defines Variables need to be changed for each installer
!define PRODUCT_NAME "${project.name}"
!define PRDOUCT_NAME_EXECUABLE "${project.artifactId}.exe"
!define PRODUCT_VERSION "${project.version}.${timestamp}"
!define PRODUCT_INSTALLERNAME "${application.installername}"
!define PRODUCT_SIZE "${application.size}"

!define PRODUCT_PUBLISHER "${project.organization.name}"
!define PRODUCT_WEB_SITE "${project.organization.url}"
!define PRODUCT_ROOT_KEY "HKLM"
!define PRODUCT_DIR_REGKEY "Software\Microsoft\Windows\CurrentVersion\${PRODUCT_PUBLISHER}\${PRODUCT_NAME}"
!define PRODUCT_UNINST_KEY "Software\Microsoft\Windows\CurrentVersion\Uninstall\${PRODUCT_PUBLISHER}-${PRODUCT_NAME}"

!define PRODUCT_STARTMENU_REGVALUE "${project.name}"
!define PRODUCT_ICON "logo.ico"
!define PRODUCT_LICENSEFILE "LICENSE.txt"
!define PRODUCT_README "README.md"
!define /date PRODUCT_LOCALDATE "%Y"
!define /date PRODUCT_LOCALTIMESTAMP "%Y%m%d%H%M%S"

; Latest jre install page: https://java.com/en/download/manual.jsp, Version 8 Update 91
Var FLAG_INSTALLJRE 
!define JRE_VERSION "1.8.0"
Var JRE_URL
!define JRE_64BitURL "http://javadl.sun.com/webapps/download/AutoDL?BundleId=210185" 
!define JRE_32BitURL "http://javadl.sun.com/webapps/download/AutoDL?BundleId=210183"
!define ALL_USERS  ;JAVA_HOME environment variable
!define NSISDL_TEXT `"$(NSISDL_DOWNLOADING)" "$(NSISDL_CONNECTING)" \
                   "$(NSISDL_SECOND)" "$(NSISDL_MINUTE)" \
                   "$(NSISDL_HOUR)" "$(NSISDL_PLURAL)" \
                   "$(NSISDL_PROGRESS)" "$(NSISDL_REMAINING)"`


; MUI end ------
Name "${PRODUCT_NAME} ${PRODUCT_VERSION}"
BrandingText $(DESC_BRANDINGTEXT)
;BrandingURL::Set /NOUNLOAD "200" "0" "0" "${PRODUCT_WEB_SITE}"
Caption "$(^Name)"
;InstallButtonText "Go >" 
;Icon "${PRODUCT_ICON}"
OutFile "${PRODUCT_INSTALLERNAME}"
InstallDir "$PROGRAMFILES\${PRODUCT_PUBLISHER}\${PRODUCT_NAME}"
;InstallDirRegKey "${PRODUCT_ROOT_KEY}" "${PRODUCT_DIR_REGKEY}" "InstallDir"
ShowInstDetails show
ShowUnInstDetails show
AutoCloseWindow false
;ShowInstDetails nevershow + SetDetailsPrint none

;=== Runtime Switches
RequestExecutionLevel user
;If /SOLID is used, all of the installer data is compressed in one block.
; This results in greater compression ratios.
; Best Compression
SetCompress Auto
SetCompressor /SOLID lzma
;SetCompressorDictSize 32
SetDatablockOptimize On


; MUI 1.67 compatible ------
!include "MUI2.nsh"
!include "LogicLib.nsh"
!include "WordFunc.nsh"
!include "x64.nsh"
!include "nsProcess.nsh"
!include "WinMessages.nsh"
!include "StrFunc.nsh"
;!include "UAC.nsh"

; MUI Settings
!define MUI_ABORTWARNING
!define MUI_HEADERIMAGE
!define MUI_HEADERIMAGE_BITMAP "${NSISDIR}\Contrib\Graphics\Header\orange.bmp" ; optional
!define MUI_HEADERIMAGE_UNBITMAP "${NSISDIR}\Contrib\Graphics\Header\orange-uninstall.bmp"

!define MUI_ICON "${PRODUCT_ICON}"
!define MUI_UNICON "${NSISDIR}\Contrib\Graphics\Icons\modern-uninstall.ico"

; Language Selection Dialog Settings
!define MUI_LANGDLL_REGISTRY_ROOT "${PRODUCT_ROOT_KEY}"
!define MUI_LANGDLL_REGISTRY_KEY "${PRODUCT_UNINST_KEY}"
!define MUI_LANGDLL_REGISTRY_VALUENAME "Language"

; The install file vesion info
VIProductVersion "${PRODUCT_VERSION}.0"
;VIFileVersion    "${PRODUCT_VERSION}.0"
VIAddVersionKey  "FileDescription"  "${PRODUCT_NAME}"
VIAddVersionKey  "FileVersion"      "${PRODUCT_VERSION}.0"
VIAddVersionKey  "ProductName"      "${PRODUCT_NAME}"
VIAddVersionKey  "ProductVersion"   "${PRODUCT_VERSION}.0"
VIAddVersionKey  "LegalCopyright"   "Copyright (C) ${PRODUCT_LOCALDATE} ${PRODUCT_PUBLISHER}"
VIAddVersionKey  "OriginalFilename" "${PRDOUCT_NAME_EXECUABLE}"
VIAddVersionKey  "Comments"         "${PRODUCT_WEB_SITE}"
VIAddVersionKey  "LegalTrademarks"  "${PRODUCT_WEB_SITE}"
VIAddVersionKey  "CompanyName"      "${PRODUCT_PUBLISHER}"
VIAddVersionKey  "InternalName"     "${PRDOUCT_NAME}"
VIAddVersionKey  "PrivateBuild"     "${PRODUCT_VERSION}.0"
VIAddVersionKey  "SpecialBuild"     "${PRODUCT_VERSION}.0"
;------------------------------------------------------------------
;------------------------------------------------------------------
; Welcome page
!insertmacro MUI_PAGE_WELCOME
;Detect JRE page
Page custom CUSTOM_PAGE_JREINFO

; Directory page for jre
; Insertmacro MUI_PAGE_COMPONENTS
!define MUI_PAGE_CUSTOMFUNCTION_PRE SectionPreJREInstallPath
!define MUI_PAGE_HEADER_TEXT $(DESC_JRE_DIRECTORYPAGE_HEADER_TEXT)
!define MUI_PAGE_HEADER_SUBTEXT $(DESC_JRE_DIRECTORYPAGE_HEADER_SUBTEXT)
!define MUI_DIRECTORYPAGE_TEXT_TOP $(DESC_JRE_DIRECTORYPAGE_TEXT_TOP)
!define MUI_DIRECTORYPAGE_VARIABLE $INSTDIR
!define MUI_DIRECTORYPAGE_VERIFYONLEAVE $(DESC_JRE_DIRECTORYPAGE_VERIFYONLEAVE)
;!define MUI_DIRECTORYPAGE_TEXT_DESTINATION $(DESC_JRE_DIRECTORYPAGE_TEXT_DESTINATION)
!insertmacro MUI_PAGE_DIRECTORY

; Define headers for the 'Java installation successfully' page
!define MUI_PAGE_HEADER_TEXT $(DESC_JRE_INSTALL_HEADER)
!define MUI_PAGE_HEADER_SUBTEXT $(DESC_JRE_INSTALL_SUBHEADER)
!define MUI_INSTFILESPAGE_ABORTHEADER_SUBTEXT $(DESC_JRE_INSTALL_ABORTHEADER_SUBHEADER)
!define MUI_INSTFILESPAGE_ABORTHEADER_TEXT $(DESC_JRE_INSTALL_ABORTHEADER_TEXT)
!define MUI_INSTFILESPAGE_FINISHHEADER_TEXT $(DESC_JRE_INSTALL_HEADER)
!define MUI_INSTFILESPAGE_FINISHHEADER_SUBTEXT $(DESC_JRE_INSTALL_FINISHED_SUBHEADER)
!insertmacro MUI_PAGE_INSTFILES  ;install JRE progress


; Uncomment the next line if you want optional components to be selectable
; Insertmacro MUI_PAGE_COMPONENTS
!define MUI_PAGE_CUSTOMFUNCTION_PRE SectionPreInstallFiles
!define MUI_PAGE_CUSTOMFUNCTION_LEAVE SectionRestoreInstallFiles

!define MUI_FINISHPAGE_TITLE $(DESC_JRE_FINISHPAGE_TITLE)
!define MUI_FINISHPAGE_TEXT $(DESC_JRE_FINISHPAGE_TEXT)
!insertmacro MUI_PAGE_FINISH




; License page
!define MUI_LICENSEPAGE_CHECKBOX
!insertmacro MUI_PAGE_LICENSE "${PRODUCT_LICENSEFILE}"
; Components page
!insertmacro MUI_PAGE_COMPONENTS
; Directory page
!insertmacro MUI_PAGE_DIRECTORY
; Start menu page
var ICONS_GROUP
!define MUI_STARTMENUPAGE_NODISABLE
!define MUI_STARTMENUPAGE_DEFAULTFOLDER "${PRODUCT_STARTMENU_REGVALUE}"
!define MUI_STARTMENUPAGE_REGISTRY_ROOT "${PRODUCT_ROOT_KEY}"
!define MUI_STARTMENUPAGE_REGISTRY_KEY "${PRODUCT_UNINST_KEY}"
!define MUI_STARTMENUPAGE_REGISTRY_VALUENAME "${PRODUCT_STARTMENU_REGVALUE}"
!insertmacro MUI_PAGE_STARTMENU STARTMENU_FOLDER_ID $ICONS_GROUP
; Instfiles page
!insertmacro MUI_PAGE_INSTFILES
; Finish page
!define MUI_FINISHPAGE_RUN "$INSTDIR\${PRDOUCT_NAME_EXECUABLE}"
!define MUI_FINISHPAGE_SHOWREADME_NOTCHECKED
!define MUI_FINISHPAGE_SHOWREADME "$INSTDIR\${PRODUCT_README}"
!define MUI_FINISHPAGE_LINK "${PRODUCT_NAME}"
!define MUI_FINISHPAGE_LINK_LOCATION "${PRODUCT_WEB_SITE}"
!insertmacro MUI_PAGE_FINISH

; Uninstaller pages

;!insertmacro MUI_UNPAGE_WELCOME
!insertmacro MUI_UNPAGE_CONFIRM
!insertmacro MUI_UNPAGE_INSTFILES
!insertmacro MUI_UNPAGE_FINISH


; Language files

  !insertmacro MUI_LANGUAGE "English" ;first language is the default language
  !insertmacro MUI_LANGUAGE "SimpChinese"
  ;!insertmacro MUI_LANGUAGE "French"
  


; Reserve files
;!insertmacro MUI_RESERVEFILE_INSTALLOPTIONS
!insertmacro MUI_RESERVEFILE_LANGDLL ;Language selection dialog


;==============================
; The full install section
;
;==============================
Section "${PRODUCT_NAME} ${PRODUCT_VERSION}" SectionFullInstall

 ; The root installation folder
  SetOutPath "$INSTDIR"
  SetOverwrite ifnewer
  File "${PRDOUCT_NAME_EXECUABLE}"
  File "*.jar"
  File "${PRODUCT_LICENSEFILE}"
  File "${PRODUCT_README}"
  ;File "${PRODUCT_ICON}"

  ; The lib folder
  SetOutPath "$INSTDIR\lib"
  SetOverwrite ifnewer
  File /nonfatal /r "lib\*"

  ; The document folder
  SetOutPath "$INSTDIR\doc"
  SetOverwrite ifnewer
  File /nonfatal /r "doc\*"

  ; The application license files
  SetOutPath "$INSTDIR\licenses"
  SetOverwrite ifnewer
  File /nonfatal /r "licenses\*.txt"

; start menu Shortcuts for all users
  SetShellVarContext all
  !insertmacro MUI_STARTMENU_WRITE_BEGIN STARTMENU_FOLDER_ID
    CreateDirectory "$SMPROGRAMS\$ICONS_GROUP"
    CreateShortCut "$SMPROGRAMS\$ICONS_GROUP\${PRODUCT_NAME}.lnk" "$INSTDIR\${PRDOUCT_NAME_EXECUABLE}" "" "$INSTDIR\${PRDOUCT_NAME_EXECUABLE}"
    ;desktop shortcut
    CreateShortCut "$DESKTOP\${PRODUCT_NAME}.lnk" "$INSTDIR\${PRDOUCT_NAME_EXECUABLE}" "" "$INSTDIR\${PRDOUCT_NAME_EXECUABLE}"   
  !insertmacro MUI_STARTMENU_WRITE_END
SectionEnd


;==============================
; Before install ,display the language selection page
; Call the JRE check section
;
;==============================
Function .onInit
   ;default install path as previous installation 
   ${If} ${RunningX64}
     SetRegView 64
   ${EndIf}
   ReadRegStr $0 ${PRODUCT_ROOT_KEY} "${PRODUCT_DIR_REGKEY}" "InstallDir"
   
   StrCmp $0 "" freshInstall
   MessageBox MB_OKCANCEL|MB_ICONEXCLAMATION \
   "${PRODUCT_NAME} is already installed before. $\n$\nClick 'OK' to remove the \
   previous version and update as latest version  or 'Cancel' to cancel this upgrade." \
   IDOK uninstOld 
   Abort
 
 ;Run the uninstaller
  uninstOld:
    ;ClearErrors
    StrCpy $INSTDIR $0
    ;ExecWait $INSTDIR\uninst.exe ; instead of the ExecWait line
    ; Set the default install path as previous install path    
    Goto installSettings
  
  freshInstall:
    ;ClearErrors
    ${If} ${RunningX64}
      ${DisableX64FSRedirection} ;disables file system redirection.
      StrCpy $INSTDIR "$PROGRAMFILES64\${PRODUCT_PUBLISHER}\${PRODUCT_NAME}"
    ${Else}
      StrCpy $INSTDIR "$PROGRAMFILES32\${PRODUCT_PUBLISHER}\${PRODUCT_NAME}"
    ${EndIf}
    Goto installSettings

  installSettings:
   ;show a splash wizard bitmap  the plugins dir is automatically deleted when the installer exits
   SetOutPath $TEMP
   File /oname=splash.bmp "${NSISDIR}\Contrib\Graphics\Wizard\orange.bmp"
   advsplash::show 500 600 400 0x04025C $TEMP\splash
   Pop $0 
   Delete $TEMP\splash.bmp
  
   ; the language ui
   !insertmacro MUI_LANGDLL_DISPLAY
   Call initJRESetupWizard

FunctionEnd


;==============================
;
; Startup menu folder and internet shortcut
; Desktop shortcut ,quicklaunch shortcut
;==============================
Section "-CreateShortcut" SectionCreateShortCuts
  SetShellVarContext all
  !insertmacro MUI_STARTMENU_WRITE_BEGIN STARTMENU_FOLDER_ID

  ;Internet Shortcut file e$INSTDIR\${PRODUCT_NAME}.exextension MUST be capitalized, e.g:
  WriteINIStr  "$INSTDIR\${PRODUCT_NAME}.URL" "InternetShortcut" "URL" "${PRODUCT_WEB_SITE}"
  CreateShortCut "$SMPROGRAMS\$ICONS_GROUP\official Website.lnk" "$INSTDIR\${PRODUCT_NAME}.url" "" \
                "$INSTDIR\${PRDOUCT_NAME_EXECUABLE}" 0 "SW_SHOWNORMAL" "" "${PRODUCT_WEB_SITE}"
  CreateShortCut "$SMPROGRAMS\$ICONS_GROUP\License.lnk" ${PRODUCT_LICENSEFILE}
  CreateShortCut "$SMPROGRAMS\$ICONS_GROUP\README.lnk" ${PRODUCT_README}
  CreateShortCut "$SMPROGRAMS\$ICONS_GROUP\Uninstall.lnk" "$INSTDIR\uninst.exe" "" "${MUI_UNICON}"
  ;quicklauncher and start menu
  CreateShortCut "$QUICKLAUNCH\${PRODUCT_NAME}.lnk" "$INSTDIR\${PRDOUCT_NAME_EXECUABLE}" "" "$INSTDIR\${PRDOUCT_NAME_EXECUABLE}"
  CreateShortCut "$STARTMENU\${PRODUCT_NAME}.lnk" "$INSTDIR\${PRDOUCT_NAME_EXECUABLE}" "" "$INSTDIR\${PRDOUCT_NAME_EXECUABLE}"
  !insertmacro MUI_STARTMENU_WRITE_END
SectionEnd


;--------------------------------
;
;Installer JRE Sections,hide it run background
;==============================


;---------------------------------------------------------------------------------------------------------------------
;Section "Add to Right mouse button" SecRightMouse
; SectionIn 1 2
; WriteRegStr HKLM "SOFTWARE\Classes\*\shellex\ContextMenuHandlers\UltraEdit-32" "" "{b5eedee0-c06e-11cf-8c56-444553540000}"
; WriteRegStr HKLM "SOFTWARE\Classes\CLSID\{b5eedee0-c06e-11cf-8c56-444553540000}" "" "UltraEdit-32"
; WriteRegStr HKLM "SOFTWARE\Classes\CLSID\{b5eedee0-c06e-11cf-8c56-444553540000}\InProcServer32" "" "$INSTDIR\ue32ctmn.dll"
; WriteRegStr HKLM "SOFTWARE\Classes\CLSID\{b5eedee0-c06e-11cf-8c56-444553540000}\InProcServer32" "ThreadingModel" "Apartment"
;SectionEnd

;==============================
; Install page, add the uninstall infos
;If section_name is empty, omitted, or begins with a -, 
;then it is a hidden section and the user will not have the option of disabling it. 
; Write unintall information 
; Create the uninstall app in installation folder
;==============================
Section "-UninstallInfo" SectionUninstallInfo
  ; Install infos
  WriteRegStr ${PRODUCT_ROOT_KEY} "${PRODUCT_DIR_REGKEY}" "InstallDir" "$INSTDIR"  ; will use this registery value for re-install directory
  WriteRegStr ${PRODUCT_ROOT_KEY} "${PRODUCT_DIR_REGKEY}" "Version" "${PRODUCT_VERSION}"
  WriteRegStr ${PRODUCT_ROOT_KEY} "${PRODUCT_DIR_REGKEY}" "InstallDate" "${PRODUCT_LOCALTIMESTAMP}"
  ; Uninstall infos from control panel->Uninstall programs
  WriteUninstaller "$INSTDIR\uninst.exe"
  WriteRegStr ${PRODUCT_ROOT_KEY} "${PRODUCT_UNINST_KEY}" "DisplayName" "$(^Name)"
  WriteRegStr ${PRODUCT_ROOT_KEY} "${PRODUCT_UNINST_KEY}" "Publisher" "${PRODUCT_PUBLISHER}"
  WriteRegStr ${PRODUCT_ROOT_KEY} "${PRODUCT_UNINST_KEY}" "InstallLocation" "$INSTDIR"
  WriteRegStr ${PRODUCT_ROOT_KEY} "${PRODUCT_UNINST_KEY}" "UninstallString" "$INSTDIR\uninst.exe"
  WriteRegStr ${PRODUCT_ROOT_KEY} "${PRODUCT_UNINST_KEY}" "QuietUninstallString" "$INSTDIR\uninstall.exe /s"
  WriteRegStr ${PRODUCT_ROOT_KEY} "${PRODUCT_UNINST_KEY}" "DisplayIcon" "$INSTDIR\${PRDOUCT_NAME_EXECUABLE}"
  WriteRegStr ${PRODUCT_ROOT_KEY} "${PRODUCT_UNINST_KEY}" "DisplayVersion" "${PRODUCT_VERSION}"
  WriteRegStr ${PRODUCT_ROOT_KEY} "${PRODUCT_UNINST_KEY}" "URLInfoAbout" "${PRODUCT_WEB_SITE}"
  WriteRegStr ${PRODUCT_ROOT_KEY} "${PRODUCT_UNINST_KEY}" "HelpLink" "${PRODUCT_WEB_SITE}"
  WriteRegStr ${PRODUCT_ROOT_KEY} "${PRODUCT_UNINST_KEY}" "URLUpdateInfo" "${PRODUCT_WEB_SITE}"
  WriteRegDWORD ${PRODUCT_ROOT_KEY} "${PRODUCT_UNINST_KEY}" "NoModify" 1
  WriteRegDWORD ${PRODUCT_ROOT_KEY} "${PRODUCT_UNINST_KEY}" "NoRepair" 1
  WriteRegDWORD ${PRODUCT_ROOT_KEY} "${PRODUCT_UNINST_KEY}" "EstimatedSize" "${PRODUCT_SIZE}"
SectionEnd


Section -installjre SectionInstallJRE
   Call DownloadAndInstallJREIfNecessary
SectionEnd


;--------------------------------
;Descriptions
 
!insertmacro MUI_FUNCTION_DESCRIPTION_BEGIN
  !insertmacro MUI_DESCRIPTION_TEXT ${SectionFullInstall} $(DESC_FULLINSTALLSECTION)
  ;!insertmacro MUI_DESCRIPTION_TEXT ${SectionCreateShortCuts} $(DESC_CREATESHORTCUTSSECTION)
  ;!insertmacro MUI_DESCRIPTION_TEXT ${SectionUninstallInfo} $(DESC_UNINSTALLINFOSECTION)
!insertmacro MUI_FUNCTION_DESCRIPTION_END


;==============================
;;;;;;;;;;;;;;;;;;;;;
;  Custom panel section for install Jre
;;;;;;;;;;;;;;;;;;;;;
;==============================
Function SectionPreJREInstallPath
  # your code here
  
  ; if return is OK means installed jre before, no need to show the dialog
  StrCmp $FLAG_INSTALLJRE "OK" leaveDirectoryPage 
  Goto  exit

  leaveDirectoryPage:
    ;MessageBox MB_OK   "Install JRE Before,no need to show the directory dialog "
    Abort
  exit:
    ;MessageBox MB_OK "exit function" 
    DetailPrint "Not Install JRE Before,take to install int in specified destination "
FunctionEnd
Function SectionPreInstallFiles
  Call SectionRestoreInstallFiles
  SetAutoClose true
FunctionEnd

Function SectionRestoreInstallFiles
  !insertmacro UnselectSection ${SectionInstallJRE}
  ;!insertmacro UnselectSection ${SectionInstallJREPath}
  !insertmacro SelectSection ${SectionFullInstall}
  !insertmacro SelectSection ${SectionCreateShortCuts}
  !insertmacro SelectSection ${SectionUninstallInfo}
FunctionEnd

Function initJRESetupWizard
  !insertmacro SelectSection ${SectionInstallJRE}
  ;!insertmacro SelectSection ${SectionInstallJREPath}
  !insertmacro UnselectSection ${SectionFullInstall}
  !insertmacro UnselectSection ${SectionCreateShortCuts}
  !insertmacro UnselectSection ${SectionUninstallInfo}
FunctionEnd



;==============================
; Uninstall section
; If a section's name is 'Uninstall' or is prefixed with 'un.', it's an uninstaller section.
; Delete the registery info and desktop shortcut,also the installation directory
;
;==============================
Section Uninstall
  SetShellVarContext all

  Goto App_Running_Check

  App_Running_Check: 
   ; is app.exe process running? result is stored in $R0 
   ${nsProcess::FindProcess} "${PRDOUCT_NAME_EXECUABLE}" $R0
   ${If} $R0 == 0
      MessageBox MB_RETRYCANCEL|MB_ICONEXCLAMATION $(DESC_UNINSTALL_CLOSEAPP_MESSAGE) /SD IDCANCEL IDRETRY App_Running_Check
      Quit
   ${EndIf} 

  ; Check the stare menu folder
  SetOutPath $Temp ; Make sure $InstDir is not the current directory so we can remove it
  !insertmacro MUI_STARTMENU_GETFOLDER "STARTMENU_FOLDER_ID" $ICONS_GROUP
  RMDir /R "$SMPROGRAMS\$ICONS_GROUP"

  Delete "$DESKTOP\${PRODUCT_NAME}.lnk"
  Delete "$QUICKLAUNCH\${PRODUCT_NAME}.lnk"
  Delete "$STARTMENU\${PRODUCT_NAME}.lnk"
  ;Remove all the files in the installocation folder
  RMDir /R "$INSTDIR"
  RMDir "$INSTDIR"
  RMDir ""

  ; Remove from the path for jre intallation
  Call un.RemovePaths
  
  ;clear the registery
  ${If} ${RunningX64}
    SetRegView 64
  ${EndIf}
  DeleteRegKey  "${PRODUCT_ROOT_KEY}" "${PRODUCT_UNINST_KEY}"
  DeleteRegKey "${PRODUCT_ROOT_KEY}" "${PRODUCT_DIR_REGKEY}"


  SetAutoClose true
SectionEnd

;==============================
; Before uninstall the application ,display the dialog
;
;==============================
Function un.onInit
;!insertmacro MUI_UNGETLANGUAGE
  MessageBox MB_ICONQUESTION|MB_YESNO|MB_DEFBUTTON2 $(DESC_UNINSTALL_MESSAGE) IDYES +2
  Abort
  ;!insertmacro MUI_UNGETLANGUAGE
  ;show a splash wizard bitmap  the plugins dir is automatically deleted when the installer exits
  InitPluginsDir
  File /oname=$PLUGINSDIR\splash.bmp "${NSISDIR}\Contrib\Graphics\Wizard\orange-uninstall.bmp"
  advsplash::show 500 600 400 0x04025C $PLUGINSDIR\splash
  Pop $0 
  Delete $PLUGINSDIR\splash.bmp
FunctionEnd
;==============================
; After uninstall the application ,display the dialog
;
;==============================

Function un.onUninstSuccess
  HideWindow
  MessageBox MB_ICONINFORMATION|MB_OK $(DESC_UNINSTALL_FINISH_MESSAGE)
FunctionEnd

;------------------------------------------------------------------

;;;;;;;;;;;;;;;;;;;;;
;  Custom panel for install Jre
;;;;;;;;;;;;;;;;;;;;;

Function CUSTOM_PAGE_JREINFO

  push $0
  push $1
  push $2
  
  Push "${JRE_VERSION}"
  Call DetectJRE
  Pop $0
  Pop $1
  StrCpy $FLAG_INSTALLJRE $0
  StrCmp $0 "OK" exit

  nsDialogs::create /NOUNLOAD 1018
  pop $1

  StrCmp $0 "0" NoFound
  StrCmp $0 "-1" FoundOld

NoFound:
;!define MUI_DIRECTORYPAGE_TEXT_DESTINATION $(DESC_JRE_DIRECTORYPAGE_TEXT_DESTINATION)
  !insertmacro MUI_HEADER_TEXT $(DESC_JRE_INSTALL_HEADER) $(DESC_JRE_INSTALL_NOTFOUND_SUBHEADER)
  ${NSD_CreateLabel} 0 0 100% 100% $(DESC_JRE_INSTALL_NOTFOUND_TEXT)
  pop $1 
  goto showDialog

FoundOld:
  !insertmacro MUI_HEADER_TEXT $(DESC_JRE_INSTALL_HEADER) $(DESC_JRE_INSTALL_FOUNDOLD_SUBHEADER)
  ${NSD_CreateLabel} 0 0 100% 100% $(DESC_JRE_INSTALL_FOUNDOLD_TEXT)
  pop $1 
  goto showDialog

showDialog:
  nsDialogs::Show

exit:
  pop $2
  pop $1
  pop $0
 

FunctionEnd

; Checks to ensure that the installed version of the JRE (if any) is at least that of
; the JRE_VERSION variable.  The JRE will be downloaded and installed if necessary
; The full path of java.exe will be returned on the stack

Function DownloadAndInstallJREIfNecessary
  Push $0
  Push $1
  DetailPrint "Detecting JRE Version"

  Push "${JRE_VERSION}"
  Call DetectJRE
  Pop $0  ; Get return value from stack
  Pop $1  ; get JRE path (or error message)
  DetailPrint "JRE Version detection complete - result = $1"

  strcmp $0 "OK" End downloadJRE

downloadJRE:
  ;MessageBox MB_OKCANCEL|MB_ICONQUESTION $(DESC_JRE_INSTALL_MESSAGE) IDOK +2
  ;Abort 
  ;Call ElevateToAdmin
  ${If} ${RunningX64}
    DetailPrint "About to download 64 Bit JRE from ${JRE_64BitURL}"
    StrCpy $JRE_URL ${JRE_64BitURL}
  ${Else}
    DetailPrint "About to download 32 Bit JRE from ${JRE_32BitURL}"
    StrCpy $JRE_URL ${JRE_32BitURL}
  ${EndIf}  
  ; Timeout=20 minutes,1200000
   NSISdl::download /TRANSLATE ${NSISDL_TEXT} $JRE_URL "$TEMP\jre_Setup.exe" /END
  /*inetc::get /NOPROXY /WEAKSECURITY \
  /CAPTION "" /POPUP "" /BANNER "" \
  /TRANSLATE 
  /TOSTACKCONV $JRE_URL "$TEMP\jre_Setup.exe" /END*/
  Pop $0 # return value = exit code, "OK" if OK
  DetailPrint "Download result = $0"
  strcmp $0 "success" downloadsuccessful  ;For NSISdl::download return is success
  ;strcmp $0 "OK" downloadsuccessful
  DetailPrint "Network Exception: $0,please install it manually from this page: $JRE_URL"
  DetailPrint "Due to the proxy setting or network connection issue,cannot download JAVA Virtual Machine."
  MessageBox MB_ICONSTOP  $(DESC_JRE_DOWNLOAD_ERROR)
  Abort
downloadsuccessful:
  DetailPrint "Launching JRE setup silently..."  
  IfSilent doSilent
  ; for jdk 8 we need to create the install config file 
  ;!appendfile "$%temp%\compiletimefile.txt" ""
  ;!delfile "$%temp%\compiletimefile.txt"
  ;!appendfile "$%temp%\compiletimefile.txt" ""
  ExecWait '"$TEMP\jre_setup.exe" INSTALL_SILENT=1 STATIC=1 REMOVEOUTOFDATEJRES=1 EULA=1 AUTO_UPDATE=0 REBOOT=0 INSTALLDIR="$INSTDIR\jre" /L "$TEMP\jre_setup.log"' $0
  goto jreSetupfinished
doSilent:
  ExecWait '"$TEMP\jre_setup.exe" INSTALL_SILENT=1 STATIC=1 REMOVEOUTOFDATEJRES=1 EULA=1 AUTO_UPDATE=0 REBOOT=0 INSTALLDIR="$INSTDIR\jre" /L "$TEMP\jre_setup.log"' $0
  
jreSetupFinished:
  DetailPrint "JRE Setup finished,Check if JRE installed correctly in the machine."
  StrCmp $0 "0" InstallVerif 0
  Push "The JRE setup has been abnormally interrupted - return code $0"
  DetailPrint "If met error code 1618 ,please reboot machine then reinstall it later ,it caused by java open issue in windows."
  Goto ExitInstallJRE
 
InstallVerif:
  DetailPrint "Checking the Installed JRE Setup's is correct or not"
  Push "${JRE_VERSION}"
  Call DetectJRE  
  Pop $0    ; DetectJRE's return value
  Pop $1    ; JRE home (or error message if compatible JRE could not be found)
  StrCmp $0 "OK" 0 JavaVerStillWrong
  Goto JREPathStorage
JavaVerStillWrong:
  Push "Unable to find JRE with version above ${JRE_VERSION}, even though the JRE setup was successful$\n$\n,JRE return: $0,JRE Home return: $1"
  Goto ExitInstallJRE
 
JREPathStorage:
  push $0 ; => rv, r1, r0
  exch 2  ; => r0, r1, rv
  exch    ; => r1, r0, rv
  ; if install success ,then take to delete the jre file,otherwise not delete it
  Delete "$TEMP\jre_setup.exe"
  DetailPrint "JRE installed successfully,JRE into System Environment Path and new Environment variable JRE_HOME"
  ; Add into the path and JRE_HOME
 ; Var /GLOBAL EnvPath
 /* ReadEnvStr $EnvPath "JRE_HOME"
  MessageBox MB_OK "$EnvPath" 
  StrCpy $EnvPath "$INSTDIR\jre"
  MessageBox MB_OK "NEW PATH: $EnvPath" 
  System::Call 'Kernel32::kernel32::SetEnvironmentVariable(t "JRE_HOME", t "$EnvPath")i'*/
  ; Path variable
  Call addJAVAHOMECLASSPATH
 
  Goto End
 
ExitInstallJRE:
  Pop $1
  DetailPrint  "Install Java from command line met exception: $1"
  DetailPrint  "Unable to install Java - Setup will be aborted$\n$\n"
  Pop $1  ; Restore $1
  Pop $0  ; Restore $0
  Abort
End:
  Pop $1  ; Restore $1
  Pop $0  ; Restore $0


FunctionEnd


; DetectJRE
; Inputs:  Minimum JRE version requested on stack (this value will be overwritten)
; Outputs: Returns two values on the stack: 
;     First value (rv0):  0 - JRE not found. -1 - JRE found but too old. OK - JRE found and meets version criteria
;     Second value (rv1):  Problem description.  Otherwise - Path to the java runtime (javaw.exe will be at .\bin\java.exe relative to this path)
 
Function DetectJRE

  Exch $0 ; Get version requested  
    ; Now the previous value of $0 is on the stack, and the asked for version of JDK is in $0
  Push $1 ; $1 = Java version string (ie 1.5.0)
  Push $2 ; $2 = Javahome
  Push $3 ; $3 = holds the version comparison result

  ; stack is now:  r3, r2, r1, r0
  ; 2) Check for JAVA_HOME
   ; goto CheckJavaHome

   ; CheckJavaHome:
   ;   ClearErrors
   ;   ReadEnvStr $2 "JAVA_HOME"
   ;   DetailPrint "Try to found JAVA_HOME Path is: $2"
   ;   StrCmp $2 "" CheckRegistry
   ;   Goto GetJRE
    
   ; CheckRegistry:
    ; first, check for an installed JRE from 32bit
    DetailPrint "Try to validate if JRE installed in the machine"
    ${If} ${RunningX64}
      SetRegView 64
      ReadRegStr $1 HKLM "SOFTWARE\JavaSoft\Java Runtime Environment" "CurrentVersion"
      ${If} $1 == ""
         ReadRegStr $1 HKLM "SOFTWARE\Wow6432Node\JavaSoft\Java Runtime Environment" "CurrentVersion"
      ${EndIf}
      StrCmp $1 "" DetectJDKInfos
      ReadRegStr $2 HKLM "SOFTWARE\JavaSoft\Java Runtime Environment\$1" "JavaHome"
      ${If} $2 == ""
         ReadRegStr $2 HKLM "SOFTWARE\Wow6432Node\JavaSoft\Java Runtime Environment\$1" "JavaHome"
      ${EndIf}
      StrCmp $2 "" DetectJDKInfos
    ${Else}
      ReadRegStr $1 HKLM "SOFTWARE\JavaSoft\Java Runtime Environment" "CurrentVersion"
      StrCmp $1 "" DetectJDKInfos
      ReadRegStr $2 HKLM "SOFTWARE\JavaSoft\Java Runtime Environment\$1" "JavaHome"
      StrCmp $2 "" DetectJDKInfos
    ${EndIf}
    DetailPrint "JavaHome value for JRE found from registery is: $2"
    Goto GetJRE
 
DetectJDKInfos:
  ; next, check for an installed JDK
 ${If} ${RunningX64}
    SetRegView 64
    ReadRegStr $1 HKLM "SOFTWARE\JavaSoft\Java Development Kit" "CurrentVersion"
    ${If} $1 == ""
       ReadRegStr $1 HKLM "SOFTWARE\Wow6432Node\JavaSoft\Java Development Kit" "CurrentVersion"
    ${EndIf}
    StrCmp $1 "" NoFound
    ReadRegStr $2 HKLM "SOFTWARE\JavaSoft\Java Development Kit\$1" "JavaHome"
    ${If} $2 == ""
       ReadRegStr $2 HKLM "SOFTWARE\Wow6432Node\JavaSoft\Java Development Kit\$1" "JavaHome"
    ${EndIf}
    StrCmp $2 "" NoFound
 ${Else}
    ReadRegStr $1 HKLM "SOFTWARE\JavaSoft\Java Development Kit" "CurrentVersion" 
    StrCmp $1 "" NoFound
    ReadRegStr $2 HKLM "SOFTWARE\JavaSoft\Java Development Kit\$1" "JavaHome"
    StrCmp $2 "" NoFound
${EndIf}
DetailPrint "JavaHome value for JDK found from registery is: $2"

 
GetJRE:
  ; ok, we found a JRE, let's compare it's version and make sure it is new enough
; $0 = version requested. $1 = version found. $2 = javaHome
  DetailPrint "Check java.exe($2\bin\java.exe) exist or not??"
  IfFileExists "$2\bin\java.exe" 0 NoFound
  ${VersionCompare} $0 $1 $3 ; $3 now contains the result of the comparison
  DetailPrint "Comparing version $0 to $1 results in $3"
  intcmp $3 1 FoundOld
  goto FoundNew
 
NoFound:
  ; No JRE found
  strcpy $0 "0"
  strcpy $1 "No JRE Found"
  DetailPrint "$2\bin\java.exe Not found-$1"
  Goto DetectJREEnd
 
FoundOld:
  ; An old JRE was found
  strcpy $0 "-1"
  strcpy $1 "Old JRE found"
  DetailPrint "$2\bin\java.exe found but it's old-$1"
  Goto DetectJREEnd  
FoundNew:
  ; A suitable JRE was found 
  strcpy $0 "OK"
  strcpy $1 $2
  DetailPrint "$2\bin\java.exe found,and version is $0"
  Goto DetectJREEnd

DetectJREEnd:
  ; at this stage, $0 contains rv0, $1 contains rv1
  ; now, straighten the stack out and recover original values for r0, r1, r2 and r3
  ; there are two return values: rv0 = -1, 0, OK and rv1 = JRE path or problem description
  ; stack looks like this: 
                ;    r3,r2,r1,r0
  Pop $3  ; => r2,r1,r0
  Pop $2  ; => r1,r0
  Push $0 ; => rv0, r1, r0
  Exch 2  ; => r0, r1, rv0
  Push $1 ; => rv1, r0, r1, rv0
  Exch 2  ; => r1, r0, rv1, rv0
  Pop $1  ; => r0, rv1, rv0
  Pop $0  ; => rv1, rv0 
  Exch  ; => rv0, rv1
  DetailPrint "Detect JRE End,Check status: $0,Return message: $1"


FunctionEnd

; Attempt to give the UAC plug-in a user process and an admin process.
; Function ElevateToAdmin
;   UAC_Elevate:
;     !insertmacro UAC_RunElevated
;     StrCmp 1223 $0 UAC_ElevationAborted ; UAC dialog aborted by user?
;     StrCmp 0 $0 0 UAC_Err ; Error?
;     StrCmp 1 $1 0 UAC_Success ;Are we the real deal or just the wrapper?
;     Quit
 
;   UAC_ElevationAborted:
;     # elevation was aborted, run as normal?
;     MessageBox MB_ICONSTOP "This installer requires admin access, aborting!"
;     Abort
 
;   UAC_Err:
;     MessageBox MB_ICONSTOP "Unable to elevate, error $0"
;     Abort
 
;   UAC_Success:
;     StrCmp 1 $3 +4 ;Admin?
;     StrCmp 3 $1 0 UAC_ElevationAborted ;Try again?
;     MessageBox MB_ICONSTOP "This installer requires admin access, try again"
;     goto UAC_Elevate 
; FunctionEnd
;--------------------------------------------------------------------
;----------------------------------- EnvVarUpdate start----------------------------------------
;EXAMPLE:  
;  Push "JAVA-PATH"
;  Push "R"
;  Push "HKLM"
;  Push "C:\Program Files\MyApp-v1.0"
;  Call EnvVarUpdate
;----------------------------------- EnvVarUpdate start----------------------------------------
;----------------------------------- EnvVarUpdate start----------------------------------------
Function addJAVAHOMECLASSPATH
  ;JAVA_HOME PATH
  Push "JRE_HOME"
  Push "A"
  Push "HKLM"
  Push "$INSTDIR\jre"
  Call EnvVarUpdate
  ;JAVA_HOME PATH
  Push "CLASS_PATH"
  Push "A"
  Push "HKLM"
  Push ".;%JRE_HOME%\lib"
  Call EnvVarUpdate
  ;PATH
  ; Push "PATH"
  ; Push "R"
  ; Push "HKLM"
  ; Push "%JRE_HOME%\bin"
  ; Call EnvVarUpdate
  ; Push "PATH"
  ; Push "A"
  ; Push "HKLM"
  ; Push "%JRE_HOME%\bin"
  ; Call EnvVarUpdate

FunctionEnd

Function un.RemovePaths
  # your code here
  ;JAVA_HOME PATH
  Push "JRE_HOME"
  Push "R"
  Push "HKLM"
  Push "$INSTDIR\lib"
  Call un.EnvVarUpdate
  ;JAVA_HOME PATH
  Push "CLASS_PATH"
  Push "R"
  Push "HKLM"
  Push "%JRE_HOME%\lib"
  Call un.EnvVarUpdate
  ;PATH
/*  Push "PATH"
  Push "R"
  Push "HKLM"
  Push "%JRE_HOME%\bin"
  Call un.EnvVarUpdate*/
  
FunctionEnd

!macro _IncludeStrFunction StrFuncName
  !ifndef ${StrFuncName}_INCLUDED
    ${${StrFuncName}}
  !endif
  !ifndef Un${StrFuncName}_INCLUDED
    ${Un${StrFuncName}}
  !endif
  !define un.${StrFuncName} "${Un${StrFuncName}}"
!macroend
!insertmacro _IncludeStrFunction StrTok
!insertmacro _IncludeStrFunction StrStr
!insertmacro _IncludeStrFunction StrRep
!define hklm_all_users     'HKLM "SYSTEM\CurrentControlSet\Control\Session Manager\Environment"'
!define hkcu_current_user  'HKCU "Environment"'
 
!macro EnvVarUpdate UN
 
Function ${UN}EnvVarUpdate
 
  Push $0
  Exch 4
  Exch $1
  Exch 3
  Exch $2
  Exch 2
  Exch $3
  Exch
  Exch $4
  Push $5
  Push $6
  Push $7
  Push $8
  Push $9
  Push $R0
 
  /* After this point:
  -------------------------
     $0 = ResultVar     (returned)
     $1 = EnvVarName    (input)
     $2 = Action        (input)
     $3 = RegLoc        (input)
     $4 = PathString    (input)
     $5 = Orig EnvVar   (read from registry)
     $6 = Len of $0     (temp)
     $7 = tempstr1      (temp)
     $8 = Entry counter (temp)
     $9 = tempstr2      (temp)
     $R0 = tempChar     (temp)  */
 
  ; Step 1:  Read contents of EnvVarName from RegLoc
  ;
  ; Check for empty EnvVarName
  ${If} $1 == ""
    SetErrors
    DetailPrint "ERROR: EnvVarName is blank"
    Goto EnvVarUpdate_Restore_Vars
  ${EndIf}
 
  ; Check for valid Action
  ${If}    $2 != "A"
  ${AndIf} $2 != "P"
  ${AndIf} $2 != "R"
    SetErrors
    DetailPrint "ERROR: Invalid Action - must be A, P, or R"
    Goto EnvVarUpdate_Restore_Vars
  ${EndIf}
 
  ${If} $3 == HKLM
    ReadRegStr $5 ${hklm_all_users} $1     ; Get EnvVarName from all users into $5
  ${ElseIf} $3 == HKCU
    ReadRegStr $5 ${hkcu_current_user} $1  ; Read EnvVarName from current user into $5
  ${Else}
    SetErrors
    DetailPrint 'ERROR: Action is [$3] but must be "HKLM" or HKCU"'
    Goto EnvVarUpdate_Restore_Vars
  ${EndIf}
 
  ; Check for empty PathString
  ${If} $4 == ""
    SetErrors
    DetailPrint "ERROR: PathString is blank"
    Goto EnvVarUpdate_Restore_Vars
  ${EndIf}
 
  ; Make sure we've got some work to do
  ${If} $5 == ""
  ${AndIf} $2 == "R"
    SetErrors
    DetailPrint "$1 is empty - Nothing to remove"
    Goto EnvVarUpdate_Restore_Vars
  ${EndIf}
 
  ; Step 2: Scrub EnvVar
  ;
  StrCpy $0 $5                             ; Copy the contents to $0
  ; Remove spaces around semicolons (NOTE: spaces before the 1st entry or
  ; after the last one are not removed here but instead in Step 3)
  ${If} $0 != ""                           ; If EnvVar is not empty ...
    ${Do}
      ${${UN}StrStr} $7 $0 " ;"
      ${If} $7 == ""
        ${ExitDo}
      ${EndIf}
      ${${UN}StrRep} $0  $0 " ;" ";"         ; Remove '<space>;'
    ${Loop}
    ${Do}
      ${${UN}StrStr} $7 $0 "; "
      ${If} $7 == ""
        ${ExitDo}
      ${EndIf}
      ${${UN}StrRep} $0  $0 "; " ";"         ; Remove ';<space>'
    ${Loop}
    ${Do}
      ${${UN}StrStr} $7 $0 ";;" 
      ${If} $7 == ""
        ${ExitDo}
      ${EndIf}
      ${${UN}StrRep} $0  $0 ";;" ";"
    ${Loop}
 
    ; Remove a leading or trailing semicolon from EnvVar
    StrCpy  $7  $0 1 0
    ${If} $7 == ";"
      StrCpy $0  $0 "" 1                   ; Change ';<EnvVar>' to '<EnvVar>'
    ${EndIf}
    StrLen $6 $0
    IntOp $6 $6 - 1
    StrCpy $7  $0 1 $6
    ${If} $7 == ";"
     StrCpy $0  $0 $6                      ; Change ';<EnvVar>' to '<EnvVar>'
    ${EndIf}
    ; DetailPrint "Scrubbed $1: [$0]"      ; Uncomment to debug
  ${EndIf}
 
  /* Step 3. Remove all instances of the target path/string (even if "A" or "P")
     $6 = bool flag (1 = found and removed PathString)
     $7 = a string (e.g. path) delimited by semicolon(s)
     $8 = entry counter starting at 0
     $9 = copy of $0
     $R0 = tempChar      */
 
  ${If} $5 != ""                           ; If EnvVar is not empty ...
    StrCpy $9 $0
    StrCpy $0 ""
    StrCpy $8 0
    StrCpy $6 0
 
    ${Do}
      ${${UN}StrTok} $7 $9 ";" $8 "0"      ; $7 = next entry, $8 = entry counter
 
      ${If} $7 == ""                       ; If we've run out of entries,
        ${ExitDo}                          ;    were done
      ${EndIf}                             ;
 
      ; Remove leading and trailing spaces from this entry (critical step for Action=Remove)
      ${Do}
        StrCpy $R0  $7 1
        ${If} $R0 != " "
          ${ExitDo}
        ${EndIf}
        StrCpy $7   $7 "" 1                ;  Remove leading space
      ${Loop}
      ${Do}
        StrCpy $R0  $7 1 -1
        ${If} $R0 != " "
          ${ExitDo}
        ${EndIf}
        StrCpy $7   $7 -1                  ;  Remove trailing space
      ${Loop}
      ${If} $7 == $4                       ; If string matches, remove it by not appending it
        StrCpy $6 1                        ; Set 'found' flag
      ${ElseIf} $7 != $4                   ; If string does NOT match
      ${AndIf}  $0 == ""                   ;    and the 1st string being added to $0,
        StrCpy $0 $7                       ;    copy it to $0 without a prepended semicolon
      ${ElseIf} $7 != $4                   ; If string does NOT match
      ${AndIf}  $0 != ""                   ;    and this is NOT the 1st string to be added to $0,
        StrCpy $0 $0;$7                    ;    append path to $0 with a prepended semicolon
      ${EndIf}                             ;
 
      IntOp $8 $8 + 1                      ; Bump counter
    ${Loop}                                ; Check for duplicates until we run out of paths
  ${EndIf}
 
  ; Step 4:  Perform the requested Action
  ;
  ${If} $2 != "R"                          ; If Append or Prepend
    ${If} $6 == 1                          ; And if we found the target
      DetailPrint "Target is already present in $1. It will be removed and"
    ${EndIf}
    ${If} $0 == ""                         ; If EnvVar is (now) empty
      StrCpy $0 $4                         ;   just copy PathString to EnvVar
      ${If} $6 == 0                        ; If found flag is either 0
      ${OrIf} $6 == ""                     ; or blank (if EnvVarName is empty)
        DetailPrint "$1 was empty and has been updated with the target"
      ${EndIf}
    ${ElseIf} $2 == "A"                    ;  If Append (and EnvVar is not empty),
      StrCpy $0 $0;$4                      ;     append PathString
      ${If} $6 == 1
        DetailPrint "appended to $1"
      ${Else}
        DetailPrint "Target was appended to $1"
      ${EndIf}
    ${Else}                                ;  If Prepend (and EnvVar is not empty),
      StrCpy $0 $4;$0                      ;     prepend PathString
      ${If} $6 == 1
        DetailPrint "prepended to $1"
      ${Else}
        DetailPrint "Target was prepended to $1"
      ${EndIf}
    ${EndIf}
  ${Else}                                  ; If Action = Remove
    ${If} $6 == 1                          ;   and we found the target
      DetailPrint "Target was found and removed from $1"
    ${Else}
      DetailPrint "Target was NOT found in $1 (nothing to remove)"
    ${EndIf}
    ${If} $0 == ""
      DetailPrint "$1 is now empty"
    ${EndIf}
  ${EndIf}
 
  ; Step 5:  Update the registry at RegLoc with the updated EnvVar and announce the change
  ;
  ClearErrors
  ${If} $3  == HKLM
    WriteRegExpandStr ${hklm_all_users} $1 $0     ; Write it in all users section
  ${ElseIf} $3 == HKCU
    WriteRegExpandStr ${hkcu_current_user} $1 $0  ; Write it to current user section
  ${EndIf}
 
  IfErrors 0 +4
    MessageBox MB_OK|MB_ICONEXCLAMATION "Could not write updated $1 to $3"
    DetailPrint "Could not write updated $1 to $3"
    Goto EnvVarUpdate_Restore_Vars
 
  ; "Export" our change
  SendMessage ${HWND_BROADCAST} ${WM_WININICHANGE} 0 "STR:Environment" /TIMEOUT=5000
 
  EnvVarUpdate_Restore_Vars:
  ;
  ; Restore the user's variables and return ResultVar
  Pop $R0
  Pop $9
  Pop $8
  Pop $7
  Pop $6
  Pop $5
  Pop $4
  Pop $3
  Pop $2
  Pop $1
  Push $0  ; Push my $0 (ResultVar)
  Exch
  Pop $0   ; Restore his $0
 
FunctionEnd
 
!macroend   ; EnvVarUpdate UN
!insertmacro EnvVarUpdate ""
!insertmacro EnvVarUpdate "un."
;----------------------------------- EnvVarUpdate end----------------------------------------

;-----------------------------------------------------------------------------
;
;Display language, First descrition name


;----------------------------------------

; English Display
!ifdef LANG_ENGLISH
    ; The nsdl:download plugin translate  
    LangString NSISDL_DOWNLOADING ${LANG_ENGLISH} "Downloading from $JRE_URL"
    LangString NSISDL_CONNECTING ${LANG_ENGLISH} "Connecting ..."
    LangString NSISDL_SECOND ${LANG_ENGLISH} "second"
    LangString NSISDL_MINUTE ${LANG_ENGLISH} "minute"
    LangString NSISDL_HOUR ${LANG_ENGLISH} "hour"
    LangString NSISDL_PLURAL ${LANG_ENGLISH} "s"
    LangString NSISDL_PROGRESS ${LANG_ENGLISH} "%dkB (%d%%) of %ukB @ %d.%01dkB/s"
    LangString NSISDL_REMAINING ${LANG_ENGLISH} " (%d %s%s remaining)"
    

    LangString DESC_BRANDINGTEXT ${LANG_ENGLISH} "${PRODUCT_NAME} ${PRODUCT_WEB_SITE}"
    LangString DESC_FULLINSTALLSECTION ${LANG_ENGLISH} "Install the full Components provided by ${PRODUCT_NAME}."

    LangString DESC_JRE_INSTALL_HEADER ${LANG_ENGLISH} "Installing JAVA Runtime Virtual Machine"
    LangString DESC_JRE_INSTALL_SUBHEADER ${LANG_ENGLISH} "Please wait while we install the JAVA Runtime Virtual Machine,This will takes few minutes,please be patient."
    LangString DESC_JRE_INSTALL_NOTFOUND_SUBHEADER ${LANG_ENGLISH} "${PRODUCT_NAME} requires JAVA ${JRE_VERSION} or higher"
    LangString DESC_JRE_INSTALL_NOTFOUND_TEXT ${LANG_ENGLISH} "This application ${PRODUCT_NAME} requires installation of the JAVA Runtime Virtual Machine.$\n$\n\
                                                             Click the Next button It will be downloaded and installed as part of the application. \
                                                             Also You can download it manually from url:$\n$\nhttp://java.com/en/download/"
    LangString DESC_JRE_INSTALL_FOUNDOLD_SUBHEADER ${LANG_ENGLISH} "${PRODUCT_NAME} requires JAVA ${JRE_VERSION} or higher"
    LangString DESC_JRE_INSTALL_FOUNDOLD_TEXT ${LANG_ENGLISH} "${PRODUCT_NAME} requires a more recent version of the JAVA Runtime Virtual Machine. This will be downloaded and installed as part of the installation."

    LangString DESC_JRE_INSTALL_MESSAGE ${LANG_ENGLISH} "Need to install JAVA Runtime Virtual Machine, Do you want to continue?"
    LangString DESC_JRE_DOWNLOAD_ERROR ${LANG_ENGLISH} "There was a problem downloading required JAVA component,you can install JAVA Runtime Virtual Machine manually."

    LangString DESC_JRE_INSTALL_FINISHED_SUBHEADER ${LANG_ENGLISH} "JAVA runtime Virtual Machine installed successfully."
    LangString DESC_JRE_FINISHPAGE_TITLE ${LANG_ENGLISH} "Runtime Environment Validation Finish"
    LangString DESC_JRE_FINISHPAGE_TEXT ${LANG_ENGLISH} "We had Detected JAVA had installed in your computer already,\
                                                        Click Finish to continue the rest installation of $(^Name)."
    LangString DESC_JRE_INSTALL_ABORTHEADER_TEXT ${LANG_ENGLISH} "Java Virtual Machine Installation Exception"
    LangString DESC_JRE_INSTALL_ABORTHEADER_SUBHEADER ${LANG_ENGLISH} "failure Network Connection,Download here: $JRE_URL"
    
    LangString DESC_JRE_DIRECTORYPAGE_HEADER_TEXT ${LANG_ENGLISH} "Choose the install destination"
    LangString DESC_JRE_DIRECTORYPAGE_HEADER_SUBTEXT ${LANG_ENGLISH} "Choose the folder in which to install JAVA Runtime Virtual Machine"
    LangString DESC_JRE_DIRECTORYPAGE_TEXT_TOP ${LANG_ENGLISH} "Setup will install JAVA Runtime Virtual Machine in the following folder.\
    To install in a different folder, click Browser and select another folder. Click Next to start the installation."
     LangString DESC_JRE_DIRECTORYPAGE_VERIFYONLEAVE ${LANG_ENGLISH} "Cancel install JAVA Virtual machine and $(^Name)?"
    
    ;English display for uninstall
    LangString DESC_UNINSTALL_MESSAGE ${LANG_ENGLISH} "Are you sure you want to completely remove $(^Name) and all of its components?"
    LangString DESC_UNINSTALL_CLOSEAPP_MESSAGE ${LANG_ENGLISH} "Found ${PRODUCT_NAME} is still running,Please close ${PRODUCT_NAME} firstly,then Retry uninstall it again."
    LangString DESC_UNINSTALL_FINISH_MESSAGE ${LANG_ENGLISH} "$(^Name) was successfully removed from your computer."

!endif


;-------------------------------------------------------------------------
; Sublime cannot display Chinese in UTF-8 you need to keep it as ANSI
; But you can save it as UTF-8 and use the ConvertUTF8 plugin to make it works too
; Simple Chinese Display;
; Download i18n : http://forums.winamp.com/showthread.php?postid=1279800
!ifdef LANG_SIMPCHINESE
     ; The nsdl:download plugin translate  
    LangString NSISDL_DOWNLOADING ${LANG_SIMPCHINESE} "正在下载资源 $JRE_URL"
    LangString NSISDL_CONNECTING ${LANG_SIMPCHINESE} "正在连接..."
    LangString NSISDL_PLURAL ${LANG_SIMPCHINESE} " "
    LangString NSISDL_SECOND ${LANG_SIMPCHINESE} "秒"
    LangString NSISDL_MINUTE ${LANG_SIMPCHINESE} "分钟"
    LangString NSISDL_HOUR ${LANG_SIMPCHINESE} "小时"
    ;
    LangString NSISDL_PROGRESS ${LANG_SIMPCHINESE} "已下载%d KB (%d%%)/大小 %u KB, 速度 %d.%01d KB/s"
    LangString NSISDL_REMAINING ${LANG_SIMPCHINESE} "(剩余 %d %s%s)"



    LangString DESC_BRANDINGTEXT ${LANG_SIMPCHINESE} "${PRODUCT_NAME} ${PRODUCT_WEB_SITE}"
    LangString DESC_FULLINSTALLSECTION ${LANG_SIMPCHINESE} "安装${PRODUCT_NAME}的全部组件。"

    LangString DESC_JRE_INSTALL_HEADER ${LANG_SIMPCHINESE} "JAVA虚拟机安装"
    LangString DESC_JRE_INSTALL_SUBHEADER ${LANG_SIMPCHINESE} "正在下载JAVA虚拟机，这可能需要几分钟，请耐心等待"
    LangString DESC_JRE_INSTALL_NOTFOUND_SUBHEADER ${LANG_SIMPCHINESE} "${PRODUCT_NAME}需要运行在JAVA虚拟机版本${JRE_VERSION}以上"
    LangString DESC_JRE_INSTALL_NOTFOUND_TEXT ${LANG_SIMPCHINESE} "${PRODUCT_NAME} 程序需要运行在JAVA虚拟机上，但是我们没有发现JAVA虚拟机安装在当前机器上。$\n$\n \
                                                             你可以点击下一步按钮进行JAVA虚拟机的下载安装，当然你也可以进行手动安装请访问下面的官方地址下载安装:$\n$\n \
                                                              http://java.com/en/download/"
    LangString DESC_JRE_INSTALL_FOUNDOLD_SUBHEADER ${LANG_SIMPCHINESE} "${PRODUCT_NAME}需要JAVA虚拟机版本${JRE_VERSION}以上"
    LangString DESC_JRE_INSTALL_FOUNDOLD_TEXT ${LANG_SIMPCHINESE} "${PRODUCT_NAME}需要安装新版本的JAVA虚拟机，请点击下一步安装完成虚拟机的升级安装操作。"

    LangString DESC_JRE_INSTALL_MESSAGE ${LANG_SIMPCHINESE} "需要安装JAVA虚拟机，是否需要继续安装？"
    LangString DESC_JRE_DOWNLOAD_ERROR ${LANG_SIMPCHINESE} "非常抱歉下载JAVA虚拟机遇到了未知的网络异常错误，你可以选择手动进行安装JAVA虚拟机有时候或者稍后重试"


    LangString DESC_JRE_INSTALL_FINISHED_SUBHEADER ${LANG_SIMPCHINESE} "JAVA虚拟机已经安装在你的机器上，请点击完成按钮继续剩下的安装步骤。"
    LangString DESC_JRE_FINISHPAGE_TITLE ${LANG_SIMPCHINESE} "运行环境检查完成"
    LangString DESC_JRE_FINISHPAGE_TEXT ${LANG_SIMPCHINESE} "检查当前机器已经安装了JAVA虚拟机，请点击完成按钮继续$(^Name)的安装"
    LangString DESC_JRE_INSTALL_ABORTHEADER_TEXT ${LANG_SIMPCHINESE} "JAVA虚拟机安装异常"
    LangString DESC_JRE_INSTALL_ABORTHEADER_SUBHEADER ${LANG_SIMPCHINESE} "网络连接失败,手动下载安装地址: $JRE_URL"
    
    LangString DESC_JRE_DIRECTORYPAGE_HEADER_TEXT ${LANG_SIMPCHINESE} "选择安装路径"
    LangString DESC_JRE_DIRECTORYPAGE_HEADER_SUBTEXT ${LANG_SIMPCHINESE} "请选择在那个文件目录下安装JAVA虚拟机"
    LangString DESC_JRE_DIRECTORYPAGE_TEXT_TOP ${LANG_SIMPCHINESE} "JAVA虚拟机将会安装在如下选择的安装目录下.\
               如果你想安装在其它的目录下请点击浏览按钮进行选择，点击下一步将会开始下载安装JAVA虚拟机。"
    LangString DESC_JRE_DIRECTORYPAGE_VERIFYONLEAVE ${LANG_SIMPCHINESE} "你确认要取消JAVA虚拟机和$(^Name)的安装吗?"
    
    ;Simple Chinese Display for uninstall
    LangString DESC_UNINSTALL_MESSAGE ${LANG_SIMPCHINESE} "你确定要完全卸载$(^Name)和它所有的组件吗？"
    LangString DESC_UNINSTALL_CLOSEAPP_MESSAGE ${LANG_SIMPCHINESE} "发现${PRODUCT_NAME}正在运行，请先关闭该应用程序再重新尝试卸载。"
    LangString DESC_UNINSTALL_FINISH_MESSAGE ${LANG_SIMPCHINESE} "$(^Name)已经卸载成功！谢谢你的使用！"
    ;-------------------------------------------------------------------
  
!endif

