plugins {
    id("java")
}

group = "icu.zhangbotong"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}
val lombokVersion = "1.18.30"
dependencies {
    annotationProcessor("org.projectlombok:lombok:$lombokVersion")
    implementation ("org.web3j:core:4.10.0")
    compileOnly("org.projectlombok:lombok:$lombokVersion")
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

tasks.test {
    useJUnitPlatform()
}