import com.example.GwtPlugin
import com.example.gwtSources

apply<GwtPlugin>()
gwtSources("com/example/service/gwt")

dependencies {
    implementation(project(":lib-B"))
    implementation("org.apache.commons:commons-collections4:4.5.0")
}