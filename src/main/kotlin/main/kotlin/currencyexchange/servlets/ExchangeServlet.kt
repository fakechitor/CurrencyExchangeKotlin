package main.kotlin.currencyexchange.servlets

import com.google.gson.Gson
import jakarta.servlet.annotation.WebServlet
import jakarta.servlet.http.HttpServlet
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import main.kotlin.currencyexchange.service.ExchangeService

@WebServlet(name = "exchange", value = ["/exchange"])
class ExchangeServlet : HttpServlet() {
    private val gson = Gson()
    private val exchangeService = ExchangeService()

    override fun doGet(req: HttpServletRequest, resp: HttpServletResponse) {
        try {
            resp.contentType = "application/json"
            resp.writer
            val baseCurrencyCode = req.getParameter("from")
            val targetCurrencyCode = req.getParameter("to")
            val amount = req.getParameter("amount")
            if (baseCurrencyCode.length == 3 && targetCurrencyCode.length == 3 && amount.toDouble() > 0.0) {
                val exchangeData = listOf(baseCurrencyCode, targetCurrencyCode, amount)
                val exchangeTransaction = exchangeService.exchange(exchangeData)
                val jsonResponse = gson.toJson(exchangeTransaction)
                resp.writer.write(jsonResponse)
                resp.status = HttpServletResponse.SC_OK
            } else {
                resp.status = HttpServletResponse.SC_BAD_REQUEST
            }
        } catch (e: Exception) {
            e.printStackTrace()
            resp.status = HttpServletResponse.SC_INTERNAL_SERVER_ERROR
        }
    }
}