@if "%DEBUG%" == "" @echo off
@rem ##########################################################################
@rem
@rem  Wikipedia startup script for Windows
@rem
@rem ##########################################################################

@rem Set local scope for the variables with windows NT shell
if "%OS%"=="Windows_NT" setlocal

set DIRNAME=%~dp0
if "%DIRNAME%" == "" set DIRNAME=.
set APP_BASE_NAME=%~n0
set APP_HOME=%DIRNAME%..

@rem Add default JVM options here. You can also use JAVA_OPTS and WIKIPEDIA_OPTS to pass JVM options to this script.
set DEFAULT_JVM_OPTS="-Xmx30g"

@rem Find java.exe
if defined JAVA_HOME goto findJavaFromJavaHome

set JAVA_EXE=java.exe
%JAVA_EXE% -version >NUL 2>&1
if "%ERRORLEVEL%" == "0" goto init

echo.
echo ERROR: JAVA_HOME is not set and no 'java' command could be found in your PATH.
echo.
echo Please set the JAVA_HOME variable in your environment to match the
echo location of your Java installation.

goto fail

:findJavaFromJavaHome
set JAVA_HOME=%JAVA_HOME:"=%
set JAVA_EXE=%JAVA_HOME%/bin/java.exe

if exist "%JAVA_EXE%" goto init

echo.
echo ERROR: JAVA_HOME is set to an invalid directory: %JAVA_HOME%
echo.
echo Please set the JAVA_HOME variable in your environment to match the
echo location of your Java installation.

goto fail

:init
@rem Get command-line arguments, handling Windows variants

if not "%OS%" == "Windows_NT" goto win9xME_args

:win9xME_args
@rem Slurp the command line arguments.
set CMD_LINE_ARGS=
set _SKIP=2

:win9xME_args_slurp
if "x%~1" == "x" goto execute

set CMD_LINE_ARGS=%*

:execute
@rem Setup the command line

set CLASSPATH=%APP_HOME%\lib\Wikipedia.jar;%APP_HOME%\lib\poi-3.15.jar;%APP_HOME%\lib\poi-ooxml-3.15.jar;%APP_HOME%\lib\log4j-1.2.17.jar;%APP_HOME%\lib\stanford-corenlp-3.7.0.jar;%APP_HOME%\lib\slf4j-simple-1.7.24.jar;%APP_HOME%\lib\commons-io-2.5.jar;%APP_HOME%\lib\commons-codec-1.10.jar;%APP_HOME%\lib\commons-collections4-4.1.jar;%APP_HOME%\lib\poi-ooxml-schemas-3.15.jar;%APP_HOME%\lib\curvesapi-1.04.jar;%APP_HOME%\lib\AppleJavaExtensions-1.4.jar;%APP_HOME%\lib\jollyday-0.4.9.jar;%APP_HOME%\lib\commons-lang3-3.3.1.jar;%APP_HOME%\lib\lucene-queryparser-4.10.3.jar;%APP_HOME%\lib\lucene-analyzers-common-4.10.3.jar;%APP_HOME%\lib\lucene-queries-4.10.3.jar;%APP_HOME%\lib\lucene-core-4.10.3.jar;%APP_HOME%\lib\javax.servlet-api-3.0.1.jar;%APP_HOME%\lib\xom-1.2.10.jar;%APP_HOME%\lib\joda-time-2.9.jar;%APP_HOME%\lib\ejml-0.23.jar;%APP_HOME%\lib\javax.json-1.0.4.jar;%APP_HOME%\lib\protobuf-java-2.6.1.jar;%APP_HOME%\lib\xmlbeans-2.6.0.jar;%APP_HOME%\lib\jaxb-api-2.2.7.jar;%APP_HOME%\lib\lucene-sandbox-4.10.3.jar;%APP_HOME%\lib\xercesImpl-2.8.0.jar;%APP_HOME%\lib\xalan-2.7.0.jar;%APP_HOME%\lib\stax-api-1.0.1.jar;%APP_HOME%\lib\slf4j-api-1.7.24.jar;%APP_HOME%\lib\xml-apis-2.0.2.jar

@rem Execute Wikipedia
"%JAVA_EXE%" %DEFAULT_JVM_OPTS% %JAVA_OPTS% %WIKIPEDIA_OPTS%  -classpath "%CLASSPATH%" SentencesContainsLinks.Main %CMD_LINE_ARGS%

:end
@rem End local scope for the variables with windows NT shell
if "%ERRORLEVEL%"=="0" goto mainEnd

:fail
rem Set variable WIKIPEDIA_EXIT_CONSOLE if you need the _script_ return code instead of
rem the _cmd.exe /c_ return code!
if  not "" == "%WIKIPEDIA_EXIT_CONSOLE%" exit 1
exit /b 1

:mainEnd
if "%OS%"=="Windows_NT" endlocal

:omega
