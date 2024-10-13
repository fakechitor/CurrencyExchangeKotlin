package main.kotlin.currencyexchange.servlets.currency

import com.google.gson.Gson
import jakarta.servlet.annotation.WebServlet
import jakarta.servlet.http.HttpServlet
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import main.kotlin.currencyexchange.data.dao.CurrencyDAO
import main.kotlin.currencyexchange.dto.CurrencyDTO

@WebServlet(name = "getCurrency", value = ["/currency/*"])
class CurrencyServlet : HttpServlet() {
    private val currencyDAO = CurrencyDAO()

    override fun doGet(req: HttpServletRequest, resp: HttpServletResponse) {
        val pathInfo = req.pathInfo?.split("/") ?: listOf()
        val code = if (pathInfo.size > 1) pathInfo[1] else ""
        try{
            if (code != "") {
                val currency = currencyDAO.getByCode(code)
                resp.contentType = "application/json"
                val printWriter = resp.writer
                if (currency != null) {
                    val currencyDTO = CurrencyDTO(
                        id = currency.id,
                        currencyCode = currency.currencyCode,
                        name = currency.name,
                        sign = currency.sign,
                    )
                    val jsonResponse = Gson().toJson(currencyDTO)
                    printWriter.write(jsonResponse)
                } else {
                    resp.status = HttpServletResponse.SC_NOT_FOUND
                }
            } else {
                resp.status = HttpServletResponse.SC_BAD_REQUEST
            }
        }
        catch (e: Exception){
            resp.status = HttpServletResponse.SC_INTERNAL_SERVER_ERROR
        }
    }

}