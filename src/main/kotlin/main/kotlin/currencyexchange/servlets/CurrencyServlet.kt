package main.kotlin.currencyexchange.servlets

import com.google.gson.Gson
import jakarta.servlet.annotation.WebServlet
import jakarta.servlet.http.HttpServlet
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import main.kotlin.currencyexchange.service.CurrencyService

@WebServlet(name = "getCurrency", value = ["/currency/*"])
class CurrencyServlet : HttpServlet() {
    private val gson = Gson()
    private val currencyService = CurrencyService()

    override fun doGet(req: HttpServletRequest, resp: HttpServletResponse) {
        val pathInfo = req.pathInfo?.split("/") ?: listOf()
        val code = if (pathInfo.size > 1) pathInfo[1] else ""
        try{
            if (code != "") {
                val currency = currencyService.getByCode(code)
                resp.contentType = "application/json"
                val printWriter = resp.writer
                if (currency.id != 0) {
                    val jsonResponse = gson.toJson(currency)
                    printWriter.write(jsonResponse)
                } else {
                    resp.status = HttpServletResponse.SC_NOT_FOUND
                }
            } else {
                resp.status = HttpServletResponse.SC_BAD_REQUEST
            }
        }
        catch (e: Exception){
            resp.status = HttpServletResponse.SC_INTERNAL_SERVER_ERROR
        }
    }

}