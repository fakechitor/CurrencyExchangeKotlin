package main.kotlin.currencyexchange.servlets.exchange

import com.google.gson.Gson
import jakarta.servlet.annotation.WebServlet
import jakarta.servlet.http.HttpServlet
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import main.kotlin.currencyexchange.exceptions.CurrencyAlreadyExistsException
import main.kotlin.currencyexchange.service.ExchangeService

@WebServlet(name = "ExchangeRatesServlet", value = ["/exchangeRates"])
class ExchangeRatesServlet : HttpServlet() {
    private val gson = Gson()
    private val exchangeService = ExchangeService()

    override fun doGet(req: HttpServletRequest, resp: HttpServletResponse) {
        try {
            resp.contentType = "application/json"
            val printWriter = resp.writer
            val exchangeRates = exchangeService.getAll()
            val jsonResponse = gson.toJson(exchangeRates)
            printWriter.write(jsonResponse)
        }
        catch (e: Exception) {
            resp.status = HttpServletResponse.SC_INTERNAL_SERVER_ERROR
        }
    }

    override fun doPost(req: HttpServletRequest, resp: HttpServletResponse) {
        try {
            val baseCurrencyCode = req.getParameter("baseCurrencyCode")
            val targetCurrencyCode = req.getParameter("targetCurrencyCode")
            val rate  = req.getParameter("rate")
            if (baseCurrencyCode.isNullOrEmpty() || targetCurrencyCode.isNullOrEmpty() || rate.isNullOrEmpty()) {
                resp.status = HttpServletResponse.SC_BAD_REQUEST
            }

        }
        catch (e: CurrencyAlreadyExistsException) {
            resp.status = HttpServletResponse.SC_CONFLICT
        }
    }
}