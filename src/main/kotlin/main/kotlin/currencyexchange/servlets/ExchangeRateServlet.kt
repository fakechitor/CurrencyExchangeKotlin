package main.kotlin.currencyexchange.servlets

import com.google.gson.Gson
import jakarta.servlet.annotation.WebServlet
import jakarta.servlet.http.HttpServlet
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import main.kotlin.currencyexchange.exceptions.CurrencyCodeIsNotExists
import main.kotlin.currencyexchange.service.ExchangeService
import main.kotlin.currencyexchange.utils.Utils
import main.kotlin.currencyexchange.utils.Validation

@WebServlet(name = "exchangeRate", value = ["/exchangeRate/*"])
class ExchangeRateServlet : HttpServlet() {
    private val exchangeService = ExchangeService()
    private val gson = Gson()
    private val utils = Utils()
    private val validator = Validation()

    override fun service(req: HttpServletRequest, resp: HttpServletResponse) {
        if (req.method.equals("PATCH", ignoreCase = true)) {
            doPatch(req, resp)
        } else {
            super.service(req, resp)
        }
    }

    override fun doGet(req: HttpServletRequest, resp: HttpServletResponse) {
        resp.contentType = "application/json"
        val connectedCodes = req.pathInfo.trim('/').uppercase()
        try {
            validator.makeCodesValidation(connectedCodes)
            val exchangeRate = exchangeService.getByCode(connectedCodes)
            val printWriter = resp.writer
            val jsonResponse = gson.toJson(exchangeRate)
            printWriter.write(jsonResponse)
        }
        catch (e: IllegalArgumentException) {
            resp.status = HttpServletResponse.SC_BAD_REQUEST
            utils.printStatus("Некорректный ввод", resp)
        }
        catch (e: CurrencyCodeIsNotExists) {
            resp.status = HttpServletResponse.SC_NOT_FOUND
            utils.printStatus("Валюта не найдена", resp)
        }
        catch (e: Exception) {
            e.printStackTrace()
            resp.status = HttpServletResponse.SC_INTERNAL_SERVER_ERROR
            utils.printStatus("Ошибка внутреннего сервера",resp)
        }
    }

    override fun doPatch(req: HttpServletRequest, resp: HttpServletResponse) {
        resp.contentType = "application/json"
        val reader = req.reader
        val requestBody = reader.readText()
        val rate = requestBody.split("=")[1].toDouble()
        val connectedCodes = req.pathInfo.trim('/').uppercase()
        try {
            validator.makeExchangeRateValidation(connectedCodes.substring(0..2), connectedCodes.substring(3..5), rate)
            val exchangeRate = exchangeService.patchRate(connectedCodes, rate)
            val responseJson = gson.toJson(exchangeRate)
            resp.writer.write(responseJson)
            resp.status = HttpServletResponse.SC_OK
        }
        catch (e: IllegalArgumentException) {
            resp.status = HttpServletResponse.SC_BAD_REQUEST
            utils.printStatus("Некорректный ввод", resp)
        }
        catch (e: Exception) {
            e.printStackTrace()
            resp.status = HttpServletResponse.SC_INTERNAL_SERVER_ERROR
            utils.printStatus("Ошибка внутреннего сервера",resp)
        }
    }
}
