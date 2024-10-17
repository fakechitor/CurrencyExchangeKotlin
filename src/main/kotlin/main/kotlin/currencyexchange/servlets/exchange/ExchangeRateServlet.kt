package main.kotlin.currencyexchange.servlets.exchange

import com.google.gson.Gson
import jakarta.servlet.annotation.WebServlet
import jakarta.servlet.http.HttpServlet
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import main.kotlin.currencyexchange.service.ExchangeService

@WebServlet(name = "exchangeRate", value = ["/exchangeRate/*"])
class ExchangeRateServlet : HttpServlet() {
    private val exchangeService = ExchangeService()
    private val gson = Gson()


    override fun doGet(req: HttpServletRequest, resp: HttpServletResponse) {
        val connectedCodes = req.pathInfo.trim('/')
        try {
            if (connectedCodes.length == 6) {
                val exchangeRate = exchangeService.getByCode(connectedCodes)
                resp.contentType = "application/json"
                val printWriter = resp.writer
                val jsonResponse = gson.toJson(exchangeRate)
                printWriter.write(jsonResponse)
            } else {
                resp.status = HttpServletResponse.SC_BAD_REQUEST
            }
        }
        catch (e: IllegalArgumentException) {
            resp.status = HttpServletResponse.SC_NOT_FOUND
        }
        catch (e: Exception) {
            e.printStackTrace()
            resp.status = HttpServletResponse.SC_INTERNAL_SERVER_ERROR
        }

    }
}