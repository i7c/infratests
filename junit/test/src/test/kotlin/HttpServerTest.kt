import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import java.io.File
import java.net.HttpURLConnection
import java.net.URL

class HttpServerTest {
    companion object {
        private val message: String = "The Answer is forty two!"
        private val workingDir = File("..")
        private val variables = mapOf(
            "value" to message
        )

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
        val ip = cd(workingDir) { bashCaptureOutput { "terraform output ip_address" } }.trim()

        val content = with(URL("http://$ip/file.txt").openConnection() as HttpURLConnection) {
            inputStream.bufferedReader().use { it.readLines() }.joinToString("\n")
        }

        assertThat(content, equalTo(message))
    }
}