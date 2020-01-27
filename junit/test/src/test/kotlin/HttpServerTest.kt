import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.matchesRegex
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import java.io.File
import java.net.HttpURLConnection
import java.net.URL

class HttpServerTest {
    companion object {
        private val workingDir = File("..")
        private val variables = emptyMap<String, Any>()

        @BeforeAll
        @JvmStatic
        fun before() {
            println(variables.tfargs())
            cd(workingDir) {
                bash {
                """
                terraform init
                terraform apply -auto-approve ${variables.tfargs()}
                """.trimIndent()
                }
            }
        }

        @AfterAll
        @JvmStatic
        fun after() = cd(workingDir) { bash { "terraform destroy -auto-approve ${variables.tfargs()}" } }
    }

    @Test
    fun `Make sure HTTP Server is reachable`() {
        val hostName = cd(workingDir) { bashCaptureOutput { "terraform output default_site_hostname" } }.trim()

        val content = with(URL("http://$hostName").openConnection() as HttpURLConnection) {
            inputStream.bufferedReader().use { it.readLines() }.joinToString("\n")
        }

        assertThat(content, matchesRegex(".*HTTP Hello World.*Hello from .*"))
    }
}