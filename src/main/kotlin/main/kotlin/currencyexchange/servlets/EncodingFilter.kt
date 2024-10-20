package main.kotlin.currencyexchange.servlets

import jakarta.servlet.*
import jakarta.servlet.annotation.WebFilter
import java.io.IOException


@WebFilter(urlPatterns = ["/*"])
class EncodingFilter : Filter {
    @Throws(ServletException::class)
    override fun init(filterConfig: FilterConfig?) {
    }

    @Throws(IOException::class, ServletException::class)
    override fun doFilter(servletRequest: ServletRequest, servletResponse: ServletResponse, filterChain: FilterChain) {
        servletResponse.characterEncoding = "UTF-8"
        servletRequest.characterEncoding = "UTF-8"
        servletResponse.contentType = "application/json"

        filterChain.doFilter(servletRequest, servletResponse)
    }

    override fun destroy() {
    }
}