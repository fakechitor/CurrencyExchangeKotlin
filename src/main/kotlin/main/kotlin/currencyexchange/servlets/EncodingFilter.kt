package main.kotlin.currencyexchange.servlets

import jakarta.servlet.*
import jakarta.servlet.annotation.WebFilter
import java.io.IOException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse

@WebFilter(urlPatterns = ["/*"])
class EncodingFilter : Filter {

    override fun init(filterConfig: FilterConfig?) {}

    @Throws(IOException::class, ServletException::class)
    override fun doFilter(servletRequest: ServletRequest, servletResponse: ServletResponse, filterChain: FilterChain) {
        servletResponse.characterEncoding = "UTF-8"
        servletRequest.characterEncoding = "UTF-8"
        servletResponse.contentType = "application/json"

        val httpResponse = servletResponse as HttpServletResponse
        httpResponse.setHeader("Access-Control-Allow-Origin", "*")
        httpResponse.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS")
        httpResponse.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization")

        val httpRequest = servletRequest as HttpServletRequest
        if ("OPTIONS".equals(httpRequest.method, ignoreCase = true)) {
            httpResponse.status = HttpServletResponse.SC_OK
            return
        }

        filterChain.doFilter(servletRequest, servletResponse)
    }
    override fun destroy() {}
}
