import com.example.GwtLibPlugin
import com.example.gwtSources

apply<GwtLibPlugin>()
gwtSources("com/example/service/gwt")

dependencies {
    api(project(":lib-A"))
    api("org.apache.commons:commons-text:1.13.1")
}