@if "%DEBUG%" == "" @echo off
@rem ##########################################################################
@rem
@rem  Legup startup script for Windows
@rem
@rem ##########################################################################

@rem Set local scope for the variables with windows NT shell
if "%OS%"=="Windows_NT" setlocal

set DIRNAME=%~dp0
if "%DIRNAME%" == "" set DIRNAME=.
set APP_BASE_NAME=%~n0
set APP_HOME=%DIRNAME%..

@rem Add default JVM options here. You can also use JAVA_OPTS and LEGUP_OPTS to pass JVM options to this script.
set DEFAULT_JVM_OPTS=

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

set CLASSPATH=%APP_HOME%\lib\Legup.jar;%APP_HOME%\lib\legup-update-2.0.jar;%APP_HOME%\lib\firebase-admin-6.3.0.jar;%APP_HOME%\lib\google-api-client-gson-1.23.0.jar;%APP_HOME%\lib\google-cloud-storage-1.27.0.jar;%APP_HOME%\lib\google-cloud-core-http-1.27.0.jar;%APP_HOME%\lib\google-api-client-1.23.0.jar;%APP_HOME%\lib\google-cloud-firestore-0.45.0-beta.jar;%APP_HOME%\lib\google-cloud-core-grpc-1.27.0.jar;%APP_HOME%\lib\gax-grpc-1.23.0.jar;%APP_HOME%\lib\google-cloud-core-1.27.0.jar;%APP_HOME%\lib\gax-httpjson-0.40.0.jar;%APP_HOME%\lib\gax-1.23.0.jar;%APP_HOME%\lib\google-auth-library-oauth2-http-0.9.0.jar;%APP_HOME%\lib\google-http-client-jackson2-1.23.0.jar;%APP_HOME%\lib\google-oauth-client-1.23.0.jar;%APP_HOME%\lib\google-http-client-gson-1.23.0.jar;%APP_HOME%\lib\google-http-client-appengine-1.23.0.jar;%APP_HOME%\lib\google-http-client-jackson-1.23.0.jar;%APP_HOME%\lib\google-http-client-1.23.0.jar;%APP_HOME%\lib\httpclient-4.5.1.jar;%APP_HOME%\lib\slf4j-simple-1.7.25.jar;%APP_HOME%\lib\slf4j-api-1.7.25.jar;%APP_HOME%\lib\commons-lang3-3.7.jar;%APP_HOME%\lib\log4j-core-2.10.0.jar;%APP_HOME%\lib\log4j-api-2.10.0.jar;%APP_HOME%\lib\grpc-protobuf-1.10.1.jar;%APP_HOME%\lib\protobuf-java-util-3.5.1.jar;%APP_HOME%\lib\grpc-netty-shaded-1.10.1.jar;%APP_HOME%\lib\grpc-stub-1.10.1.jar;%APP_HOME%\lib\grpc-auth-1.10.1.jar;%APP_HOME%\lib\grpc-protobuf-lite-1.10.1.jar;%APP_HOME%\lib\grpc-core-1.10.1.jar;%APP_HOME%\lib\gson-2.8.2.jar;%APP_HOME%\lib\commons-cli-1.4.jar;%APP_HOME%\lib\commons-io-2.6.jar;%APP_HOME%\lib\proto-google-cloud-firestore-v1beta1-0.10.0.jar;%APP_HOME%\lib\proto-google-iam-v1-0.10.0.jar;%APP_HOME%\lib\api-common-1.5.0.jar;%APP_HOME%\lib\opencensus-contrib-http-util-0.11.1.jar;%APP_HOME%\lib\opencensus-contrib-grpc-metrics-0.11.0.jar;%APP_HOME%\lib\opencensus-api-0.11.1.jar;%APP_HOME%\lib\guava-20.0.jar;%APP_HOME%\lib\json-20160810.jar;%APP_HOME%\lib\netty-codec-http-4.1.22.Final.jar;%APP_HOME%\lib\netty-handler-4.1.22.Final.jar;%APP_HOME%\lib\netty-codec-4.1.22.Final.jar;%APP_HOME%\lib\netty-transport-4.1.22.Final.jar;%APP_HOME%\lib\httpcore-4.4.3.jar;%APP_HOME%\lib\commons-logging-1.2.jar;%APP_HOME%\lib\commons-codec-1.9.jar;%APP_HOME%\lib\jsr305-3.0.1.jar;%APP_HOME%\lib\google-auth-library-credentials-0.9.0.jar;%APP_HOME%\lib\google-api-services-storage-v1-rev114-1.23.0.jar;%APP_HOME%\lib\auto-value-1.4.jar;%APP_HOME%\lib\netty-buffer-4.1.22.Final.jar;%APP_HOME%\lib\netty-resolver-4.1.22.Final.jar;%APP_HOME%\lib\jackson-core-2.1.3.jar;%APP_HOME%\lib\guava-jdk5-17.0.jar;%APP_HOME%\lib\joda-time-2.9.2.jar;%APP_HOME%\lib\proto-google-common-protos-1.9.0.jar;%APP_HOME%\lib\protobuf-java-3.5.1.jar;%APP_HOME%\lib\grpc-context-1.10.1.jar;%APP_HOME%\lib\threetenbp-1.3.3.jar;%APP_HOME%\lib\netty-common-4.1.22.Final.jar;%APP_HOME%\lib\jackson-core-asl-1.9.11.jar;%APP_HOME%\lib\error_prone_annotations-2.2.0.jar

@rem Execute Legup
"%JAVA_EXE%" %DEFAULT_JVM_OPTS% %JAVA_OPTS% %LEGUP_OPTS%  -classpath "%CLASSPATH%" edu.rpi.legup.Legup %CMD_LINE_ARGS%

:end
@rem End local scope for the variables with windows NT shell
if "%ERRORLEVEL%"=="0" goto mainEnd

:fail
rem Set variable LEGUP_EXIT_CONSOLE if you need the _script_ return code instead of
rem the _cmd.exe /c_ return code!
if  not "" == "%LEGUP_EXIT_CONSOLE%" exit 1
exit /b 1

:mainEnd
if "%OS%"=="Windows_NT" endlocal

:omega
