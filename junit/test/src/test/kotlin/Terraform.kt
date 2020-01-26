import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper

fun Map<String, Any>.tfargs(): String =
    map { (k: String, v: Any) ->
        """-var="$k=${
        if (v is String) v
        else jacksonObjectMapper().writeValueAsString(v)
        }""""
    }.joinToString(" ")