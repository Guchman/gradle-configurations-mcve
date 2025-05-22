allprojects {
    repositories {
        mavenCentral()
    }
}

group = "com.example"
version = "unspecified"

subprojects {
    apply(plugin = "java-library")
}