package main.kotlin.currencyexchange.servlets.exchange

import com.google.gson.Gson
import jakarta.servlet.annotation.WebServlet
import jakarta.servlet.http.HttpServlet
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import main.kotlin.currencyexchange.dto.ExchangeRateDTO
import main.kotlin.currencyexchange.service.ExchangeService

@WebServlet(name = "ExchangeRatesServlet", value = ["/exchangeRates"])
class ExchangeRatesServlet : HttpServlet() {
    private val gson = Gson()
    private val exchangeService = ExchangeService()

    override fun doGet(req: HttpServletRequest, resp: HttpServletResponse) {
        try {
            resp.contentType = "application/json"
            val printWriter = resp.writer
            val exchangeRates = exchangeService.getAll().toMutableList()
            var responseData: List<ExchangeRateDTO> = listOf()
            while (exchangeRates.isNotEmpty()) {
                val exchangeRate = exchangeRates[0]
                exchangeRates.remove(exchangeRate)
                val exchangeRateDTO = ExchangeRateDTO(
                    id = exchangeRate.id,
                    baseCurrency = exchangeRate.baseCurrency,
                    targetCurrency = exchangeRate.targetCurrency,
                    rate = exchangeRate.rate,
                )
                responseData = responseData.plus(exchangeRateDTO)
            }
            val jsonResponse = gson.toJson(responseData)
            printWriter.write(jsonResponse)
        }
        catch (e: Exception) {
            resp.status = HttpServletResponse.SC_INTERNAL_SERVER_ERROR
        }
    }

    override fun doPost(req: HttpServletRequest?, resp: HttpServletResponse?) {

    }
}