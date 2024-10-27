package main.kotlin.currencyexchange.servlets

import jakarta.servlet.*
import jakarta.servlet.annotation.WebFilter
import jakarta.servlet.http.HttpServletResponse
import java.io.IOException

@WebFilter(urlPatterns = ["/*"])
class CORSFilter : Filter {

    @Throws(IOException::class, ServletException::class)
    override fun doFilter(request: ServletRequest?, response: ServletResponse, chain: FilterChain) {
        val httpResponse = response as HttpServletResponse
        httpResponse.setHeader("Access-Control-Allow-Origin", "*")
        httpResponse.setHeader("Access-Control-Allow-Methods", "GET, PATCH, POST, PUT, DELETE, OPTIONS")
        httpResponse.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization")
        chain.doFilter(request, response)
    }
}
