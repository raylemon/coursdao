plugins {
    java
    application
}

group = "tuto"
version = "0.1"

repositories {
    mavenCentral()
}

dependencies {
    implementation( group = "com.h2database", name = "h2", version = "1.4.200")
    implementation(group = "com.esotericsoftware", name = "minlog", version = "1.3.0")
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_11
}

application {
    mainClassName = "org.github.raylemon.tuto.dao.MainApp"
}