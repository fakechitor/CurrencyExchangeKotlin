package main.kotlin.currencyexchange.controllers

import com.google.gson.Gson
import jakarta.servlet.annotation.WebServlet
import jakarta.servlet.http.HttpServlet
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import main.kotlin.currencyexchange.data.dao.CurrencyDAO
import main.kotlin.currencyexchange.data.entities.Currency


@WebServlet(name = "getCurrencies", value = ["/currencies"])
class CurrenciesServlet : HttpServlet() {
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

    override fun doPost(req: HttpServletRequest, resp: HttpServletResponse) {
        try{
            val currencyCode = req.getParameter("code")
            val name = req.getParameter("name")
            val sign = req.getParameter("sign")
            if (currencyCode.isNullOrEmpty() || name.isNullOrEmpty() || sign.isNullOrEmpty()) {
                resp.status = HttpServletResponse.SC_BAD_REQUEST
            }
            val currency = Currency(currencyCode, name,sign)
            currencyDAO.save(currency)
        }
        catch(e : Exception){
            e.printStackTrace()
        }

    }
}