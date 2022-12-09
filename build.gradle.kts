plugins {
    `java-library`
    checkstyle
    id("edu.sc.seis.launch4j") version("2.5.3")
    id("com.github.johnrengelman.shadow") version "4.0.4"
}
repositories {
    mavenCentral()
}


/* Maven artifact coordinates */
group = "edu.rpi.Legup"
version = "2.0.0"
val artifactId = "Legup"

/* important / reused build properties */
object BuildProps {
    const val mainClassName = "edu.rpi.legup.Legup"
    const val splashImagePath = "edu/rpi/legup/images/Legup/LegupSplash.png"
    val minJavaVersion = JavaVersion.VERSION_1_8
    const val minJavaVersionString = "1.8.0"
}


java {
    sourceCompatibility = BuildProps.minJavaVersion
    targetCompatibility = BuildProps.minJavaVersion
}

dependencies {
    implementation("org.jetbrains:annotations:23.0.0")
    implementation("org.apache.httpcomponents:httpclient:4.5.13")
    api("org.slf4j:slf4j-api:2.0.4")
    api("org.slf4j:slf4j-simple:2.0.4")
    api("org.apache.logging.log4j:log4j-api:2.19.0")
    api("org.apache.logging.log4j:log4j-core:2.19.0")
    api("org.apache.commons:commons-lang3:3.12.0")
    testImplementation("junit:junit:4.13.2")
}

tasks {
    jar {
        manifest {
            attributes(
                "Implementation-Title" to artifactId,
                "Implementation-Version" to project.version,
                "Main-Class" to BuildProps.mainClassName,
                "SplashScreen-Image" to BuildProps.splashImagePath
            )
        }
    }

    /*
     * CREATES NATIVE WINDOWS EXECUTABLE
     * Launches launch4j to create an executable (.exe) file wrapping the jar
     * THIS IS NOT THE INSTALLER
     * Add "icon = "path/to/icon.ico"" to set an icon for the executable
     */
    createExe {
        mainClassName = BuildProps.mainClassName
        outputDir = "../native/windows"
        outfile = "bin/Legup.exe"
        bundledJrePath = "jre"
        bundledJre64Bit = true
        jdkPreference = "preferJre"
        jreMinVersion = BuildProps.minJavaVersionString
        jreRuntimeBits = "64/32"
    }

    /*
     * CREATES NATIVE WINDOWS INSTALLER -- ONLY RUNS ON WINDOWS
     * Runs the shipped version of Inno Setup (6.2) to compile the installer
     * Modify the setup settings in native/windows/legup_inno_setup.iss
     *
     * Modifications are likely required to run the setup script on your computer:
     * Edit the "CHANGE ME" line in native/windows/legup_inno_setup.iss to reflect
     * the path to the Java installation you want to ship inside the executable.
     */
    val buildNativeWindows = register<Exec>("buildNativeWindows") {
        dependsOn(shadowJar, createExe)
        workingDir = File("${project.buildDir}/../native/windows")
        commandLine("cmd", "/c", "make_windows_installer.bat")
    }
}