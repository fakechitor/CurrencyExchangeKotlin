package main.kotlin.currencyexchange.utils

import com.google.gson.Gson
import jakarta.servlet.http.HttpServletResponse

class Utils {
    private val gson = Gson()
    fun splitCurrencyCodes(code: String): List<String> {
        return listOf(code.substring(0,3), code.substring(3))
    }
    fun printStatus(message : String, resp : HttpServletResponse) {
        val jsonResponse = gson.toJson(mapOf("message" to message))
        resp.writer.write(jsonResponse)
    }
}