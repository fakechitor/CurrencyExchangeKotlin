package main.kotlin.currencyexchange.servlets.exchange

import com.google.gson.Gson
import jakarta.servlet.annotation.WebServlet
import jakarta.servlet.http.HttpServlet
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import main.kotlin.currencyexchange.dto.ExchangeRateDTO
import main.kotlin.currencyexchange.exceptions.CurrencyAlreadyExistsException
import main.kotlin.currencyexchange.service.CurrencyService
import main.kotlin.currencyexchange.service.ExchangeService

@WebServlet(name = "ExchangeRatesServlet", value = ["/exchangeRates"])
class ExchangeRatesServlet : HttpServlet() {
    private val gson = Gson()
    private val exchangeService = ExchangeService()
    private val currencyService = CurrencyService()

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
            resp.contentType = "application/json"
            val baseCurrencyCode = req.getParameter("baseCurrencyCode")
            val targetCurrencyCode = req.getParameter("targetCurrencyCode")
            val rate = req.getParameter("rate").toDouble()
            if (baseCurrencyCode.isNullOrEmpty() || targetCurrencyCode.isNullOrEmpty() || rate <= 0) {
                resp.status = HttpServletResponse.SC_BAD_REQUEST
            }
            val baseCurrency = currencyService.getByCode(baseCurrencyCode)
            val targetCurrency = currencyService.getByCode(targetCurrencyCode)
            val exchangeRate = ExchangeRateDTO(null, baseCurrency, targetCurrency, rate)
            val response = exchangeService.save(exchangeRate)
            val printWriter = resp.writer
            val jsonResponse = gson.toJson(response)
            printWriter.write(jsonResponse)
            resp.status = HttpServletResponse.SC_CREATED
        } catch (e: IllegalArgumentException) {
            resp.status = HttpServletResponse.SC_NOT_FOUND
        }
        catch (e: CurrencyAlreadyExistsException) {
            resp.status = HttpServletResponse.SC_CONFLICT
        } catch (e: Exception) {
            e.printStackTrace()
            resp.status = HttpServletResponse.SC_INTERNAL_SERVER_ERROR
        }
    }
}