package main.kotlin.currencyexchange.servlets

import com.google.gson.Gson
import jakarta.servlet.annotation.WebServlet
import jakarta.servlet.http.HttpServlet
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import main.kotlin.currencyexchange.dto.CurrencyDTO
import main.kotlin.currencyexchange.exceptions.CurrencyAlreadyExistsException
import main.kotlin.currencyexchange.service.CurrencyService
import main.kotlin.currencyexchange.utils.Utils
import main.kotlin.currencyexchange.utils.Validation


@WebServlet(name = "getCurrencies", value = ["/currencies"])
class CurrenciesServlet : HttpServlet() {
    private val gson = Gson()
    private val currencyService = CurrencyService()
    private val utils = Utils()
    private val validator = Validation()

    override fun doGet(req: HttpServletRequest, resp: HttpServletResponse) {
        try {
            val printWriter = resp.writer
            val currencies: List<CurrencyDTO> = currencyService.getAll()
            val jsonResponse = gson.toJson(currencies)
            printWriter.write(jsonResponse)
        }
        catch (e: Exception) {
            resp.status = HttpServletResponse.SC_INTERNAL_SERVER_ERROR
            utils.printStatus("Ошибка внутреннего сервера",resp)
        }
    }

    override fun doPost(req: HttpServletRequest, resp: HttpServletResponse) {
        try{
            val currencyCode = req.getParameter("code").uppercase()
            val name = req.getParameter("name")
            val sign = req.getParameter("sign")
            validator.isCurrencyCodeValid(currencyCode)
            validator.isNotEmpty(listOf(currencyCode,name,sign))
            val currencyDTO = CurrencyDTO(null, currencyCode, name, sign)
            val response = currencyService.save(currencyDTO)
            val printWriter = resp.writer
            val jsonResponse = gson.toJson(response)
            printWriter.write(jsonResponse)
            resp.status = HttpServletResponse.SC_CREATED
        }
        catch (e: IllegalArgumentException) {
            resp.status = HttpServletResponse.SC_BAD_REQUEST
            utils.printStatus("Некорректный ввод",resp)
        }
        catch (e: CurrencyAlreadyExistsException){
            resp.status = HttpServletResponse.SC_CONFLICT
            utils.printStatus("Валюта уже существует", resp)
        }
        catch(e : Exception){
            e.printStackTrace()
            resp.status = HttpServletResponse.SC_INTERNAL_SERVER_ERROR
            utils.printStatus("Ошибка внутреннего сервера",resp)
        }

    }
}