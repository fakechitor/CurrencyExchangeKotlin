package main.kotlin.currencyexchange.controllers

import com.google.gson.Gson
import jakarta.servlet.annotation.WebServlet
import jakarta.servlet.http.HttpServlet
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import main.kotlin.currencyexchange.data.dao.CurrencyDAO
import main.kotlin.currencyexchange.data.entities.Currency


@WebServlet(name = "getCurrencies", value = ["/currencies"])
class GetCurrencies : HttpServlet() {
    private val gson = Gson()
    private val currencyDAO  = CurrencyDAO()

    override fun doGet(req: HttpServletRequest, resp: HttpServletResponse) {
        resp.contentType = "application/json"
        val printWriter = resp.writer
        val currencies : MutableList<Currency> = currencyDAO.getAll()
        var responseData : List<Map<String, Comparable<*>>> = listOf()
        while (currencies.isNotEmpty()) {
            val currency = currencies[0]
            currencies.remove(currency)
            val currencyData: Map<String, Comparable<*>> = mapOf(
                "id" to currency.id,
                "currencyCode" to currency.currencyCode,
                "name" to currency.name,
                "sign" to currency.sign,
            )
            responseData = responseData.plus(currencyData)
        }
        val jsonResponse = gson.toJson(responseData)
        printWriter.write(jsonResponse)


    }
}