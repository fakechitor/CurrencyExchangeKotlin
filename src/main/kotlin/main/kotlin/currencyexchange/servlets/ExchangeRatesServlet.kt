package main.kotlin.currencyexchange.servlets

import com.google.gson.Gson
import jakarta.servlet.annotation.WebServlet
import jakarta.servlet.http.HttpServlet
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import main.kotlin.currencyexchange.dto.ExchangeRateDTO
import main.kotlin.currencyexchange.exceptions.CurrencyAlreadyExistsException
import main.kotlin.currencyexchange.service.CurrencyService
import main.kotlin.currencyexchange.service.ExchangeService
import main.kotlin.currencyexchange.utils.Utils

@WebServlet(name = "ExchangeRatesServlet", value = ["/exchangeRates"])
class ExchangeRatesServlet : HttpServlet() {
    private val gson = Gson()
    private val exchangeService = ExchangeService()
    private val currencyService = CurrencyService()
    private val utils = Utils()

    override fun doGet(req: HttpServletRequest, resp: HttpServletResponse) {
        try {
            val printWriter = resp.writer
            val exchangeRates = exchangeService.getAll()
            val jsonResponse = gson.toJson(exchangeRates)
            printWriter.write(jsonResponse)
        }
        catch (e: Exception) {
            resp.status = HttpServletResponse.SC_INTERNAL_SERVER_ERROR
            utils.printStatus("Внутренняя ошибка сервера", resp)
        }
    }

    override fun doPost(req: HttpServletRequest, resp: HttpServletResponse) {
        try {
            val baseCurrencyCode = req.getParameter("baseCurrencyCode")
            val targetCurrencyCode = req.getParameter("targetCurrencyCode")
            val rate = req.getParameter("rate").toDouble()
            if (baseCurrencyCode.isNullOrEmpty() || targetCurrencyCode.isNullOrEmpty() || rate <= 0) {
                resp.status = HttpServletResponse.SC_BAD_REQUEST
                utils.printStatus("Некорректный ввод",resp)
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
            utils.printStatus("Валюта не найдена",resp)
        }
        catch (e: CurrencyAlreadyExistsException) {
            resp.status = HttpServletResponse.SC_CONFLICT
            utils.printStatus("Обменный курс уже существует", resp)
        } catch (e: Exception) {
            e.printStackTrace()
            resp.status = HttpServletResponse.SC_INTERNAL_SERVER_ERROR
            utils.printStatus("Внутренняя ошибка сервера", resp)
        }
    }
}