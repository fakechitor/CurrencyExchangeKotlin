package main.kotlin.currencyexchange.controllers

import com.google.gson.Gson
import jakarta.servlet.annotation.WebServlet
import jakarta.servlet.http.HttpServlet
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import main.kotlin.currencyexchange.data.dao.CurrencyDAO

@WebServlet(name = "getCurrency", value = ["/currency/*"])
class GetCurrency : HttpServlet() {
    private val currencyDAO = CurrencyDAO()

    override fun doGet(req: HttpServletRequest, resp: HttpServletResponse) {
        val pathInfo = req.pathInfo?.split("/") ?: listOf()
        val code = if (pathInfo.size > 1) pathInfo[1] else ""
        if (code != "") {
            val currency = currencyDAO.getByCode(code)
            resp.contentType = "application/json"
            val printWriter = resp.writer
            if (currency != null) {
                val responseData: Map<String, Comparable<*>> = mapOf(
                    "id" to currency.id,
                    "currencyCode" to currency.currencyCode,
                    "name" to currency.name,
                    "sign" to currency.sign,
                )
                val jsonResponse = Gson().toJson(responseData)
                printWriter.write(jsonResponse)
            }
            else {
                resp.status = HttpServletResponse.SC_NOT_FOUND
            }
        }
        else{
            resp.status = HttpServletResponse.SC_BAD_REQUEST
        }
    }

}