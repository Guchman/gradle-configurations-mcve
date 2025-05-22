import com.example.GwtLibPlugin

apply<GwtLibPlugin>()

dependencies {
    api("com.google.guava:guava:32.1.2-jre")
    implementation("org.apache.commons:commons-lang3:3.17.0")
}