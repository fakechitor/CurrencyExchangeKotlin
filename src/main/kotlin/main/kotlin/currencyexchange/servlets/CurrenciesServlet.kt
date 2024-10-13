package main.kotlin.currencyexchange.servlets

import com.google.gson.Gson
import jakarta.servlet.annotation.WebServlet
import jakarta.servlet.http.HttpServlet
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import main.kotlin.currencyexchange.data.dao.CurrencyDAO
import main.kotlin.currencyexchange.data.entities.Currency
import main.kotlin.currencyexchange.dto.CurrencyDTO
import main.kotlin.currencyexchange.exceptions.CurrencyAlreadyExistsException
import kotlin.math.sign


@WebServlet(name = "getCurrencies", value = ["/currencies"])
class CurrenciesServlet : HttpServlet() {
    private val gson = Gson()
    private val currencyDAO  = CurrencyDAO()

    override fun doGet(req: HttpServletRequest, resp: HttpServletResponse) {
        try {
            resp.contentType = "application/json"
            val printWriter = resp.writer
            val currencies: MutableList<Currency> = currencyDAO.getAll()
            var responseData: List<CurrencyDTO> = listOf()
            while (currencies.isNotEmpty()) {
                val currency = currencies[0]
                currencies.remove(currency)
                val currencyDTO = CurrencyDTO(
                    id = currency.id,
                    currencyCode = currency.currencyCode,
                    name = currency.name,
                    sign = currency.sign,
                )
                responseData = responseData.plus(currencyDTO)
            }
            val jsonResponse = gson.toJson(responseData)
            printWriter.write(jsonResponse)
        }
        catch (e: Exception) {
            resp.status = HttpServletResponse.SC_INTERNAL_SERVER_ERROR
        }
    }

    override fun doPost(req: HttpServletRequest, resp: HttpServletResponse) {
        try{
            val currencyCode = req.getParameter("code")
            val name = req.getParameter("name")
            val sign = req.getParameter("sign")
            if (currencyCode.isNullOrEmpty() || name.isNullOrEmpty() || sign.isNullOrEmpty()) {
                resp.status = HttpServletResponse.SC_BAD_REQUEST
            }
            val currencyDTO = CurrencyDTO(null, currencyCode, name, sign)
            currencyDAO.save(currencyDTO)
            resp.status = HttpServletResponse.SC_CREATED
        }
        catch (e: CurrencyAlreadyExistsException){
            resp.status = HttpServletResponse.SC_CONFLICT
        }
        catch(e : Exception){
            e.printStackTrace()
            resp.status = HttpServletResponse.SC_INTERNAL_SERVER_ERROR
        }

    }
}