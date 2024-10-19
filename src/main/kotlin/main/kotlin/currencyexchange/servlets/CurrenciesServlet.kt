package main.kotlin.currencyexchange.servlets

import com.google.gson.Gson
import jakarta.servlet.annotation.WebServlet
import jakarta.servlet.http.HttpServlet
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import main.kotlin.currencyexchange.data.entities.Currency
import main.kotlin.currencyexchange.dto.CurrencyDTO
import main.kotlin.currencyexchange.exceptions.CurrencyAlreadyExistsException
import main.kotlin.currencyexchange.service.CurrencyService


@WebServlet(name = "getCurrencies", value = ["/currencies"])
class CurrenciesServlet : HttpServlet() {
    private val gson = Gson()
    private val currencyService = CurrencyService()

    override fun doGet(req: HttpServletRequest, resp: HttpServletResponse) {
        try {
            resp.contentType = "application/json"
            val printWriter = resp.writer
            val currencies: List<Currency> = currencyService.getAll()
            val jsonResponse = gson.toJson(currencies)
            printWriter.write(jsonResponse)
        }
        catch (e: Exception) {
            resp.status = HttpServletResponse.SC_INTERNAL_SERVER_ERROR
            val answer = mapOf("message" to "Ошибка внутреннего сервера")
            val jsonResponse = gson.toJson(answer)
            resp.writer.write(jsonResponse)
        }
    }

    override fun doPost(req: HttpServletRequest, resp: HttpServletResponse) {
        try{
            resp.contentType = "application/json"
            val currencyCode = req.getParameter("code")
            val name = req.getParameter("name")
            val sign = req.getParameter("sign")
            if (currencyCode.isNullOrEmpty() || name.isNullOrEmpty() || sign.isNullOrEmpty()) {
                resp.status = HttpServletResponse.SC_BAD_REQUEST
                val answer = mapOf("message" to "Некорректный ввод")
                val jsonResponse = gson.toJson(answer)
                resp.writer.write(jsonResponse)
            }
            val currencyDTO = CurrencyDTO(null, currencyCode, name, sign)
            val response = currencyService.save(currencyDTO)
            val printWriter = resp.writer
            val jsonResponse = gson.toJson(response)
            printWriter.write(jsonResponse)
            resp.status = HttpServletResponse.SC_CREATED
        }
        catch (e: CurrencyAlreadyExistsException){
            resp.status = HttpServletResponse.SC_CONFLICT
            val answer = mapOf("message" to "Валюта уже существует")
            val jsonResponse = gson.toJson(answer)
            resp.writer.write(jsonResponse)
        }
        catch(e : Exception){
            e.printStackTrace()
            resp.status = HttpServletResponse.SC_INTERNAL_SERVER_ERROR
            val answer = mapOf("message" to "Ошибка внутреннего сервера")
            val jsonResponse = gson.toJson(answer)
            resp.writer.write(jsonResponse)
        }

    }
}