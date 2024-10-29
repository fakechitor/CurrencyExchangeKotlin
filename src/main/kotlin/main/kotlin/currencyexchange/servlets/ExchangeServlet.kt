package main.kotlin.currencyexchange.servlets

import com.google.gson.Gson
import jakarta.servlet.annotation.WebServlet
import jakarta.servlet.http.HttpServlet
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import main.kotlin.currencyexchange.exceptions.ExchangeRateIsNotExists
import main.kotlin.currencyexchange.service.ExchangeService
import main.kotlin.currencyexchange.utils.Utils
import main.kotlin.currencyexchange.utils.Validation

@WebServlet(name = "exchange", value = ["/exchange"])
class ExchangeServlet : HttpServlet() {
    private val gson = Gson()
    private val exchangeService = ExchangeService()
    private val utils = Utils()
    private val validator = Validation()

    override fun doGet(req: HttpServletRequest, resp: HttpServletResponse) {
        resp.contentType = "application/json"
        try {
            val baseCurrencyCode = req.getParameter("from").uppercase()
            val targetCurrencyCode = req.getParameter("to").uppercase()
            val amount = req.getParameter("amount")
            validator.makeExchangeRateValidation(baseCurrencyCode, targetCurrencyCode, amount.toDouble())
            val exchangeData = listOf(baseCurrencyCode, targetCurrencyCode, amount)
            val exchangeTransaction = exchangeService.exchange(exchangeData)
            val jsonResponse = gson.toJson(exchangeTransaction)
            resp.writer.write(jsonResponse)
            resp.status = HttpServletResponse.SC_OK
        }
        catch (e: IllegalArgumentException) {
            resp.status = HttpServletResponse.SC_BAD_REQUEST
            utils.printStatus("Некорректный ввод",resp)
        }
        catch (e:ExchangeRateIsNotExists) {
            resp.status = HttpServletResponse.SC_NOT_FOUND
            utils.printStatus("Обменный курс не найден",resp)
        }
        catch (e: Exception) {
            e.printStackTrace()
            resp.status = HttpServletResponse.SC_INTERNAL_SERVER_ERROR
            utils.printStatus("Ошибка внутреннего сервера",resp)
        }
    }
}