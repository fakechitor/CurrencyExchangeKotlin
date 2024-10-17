package main.kotlin.currencyexchange.service

import main.kotlin.currencyexchange.data.dao.ExchangeRateDAO
import main.kotlin.currencyexchange.data.entities.ExchangeRate
import main.kotlin.currencyexchange.dto.ExchangeRateDTO

class ExchangeService {
    private val exchangeRateDAO = ExchangeRateDAO()

    fun getById(id : Int) : ExchangeRate {
        return exchangeRateDAO.getById(id)
    }

    fun getByCode(code : String) : ExchangeRate {
        return exchangeRateDAO.getByCode(code)
    }

    fun getAll() : List<ExchangeRate> {
        return exchangeRateDAO.getAll()
    }

    fun save(exchange: ExchangeRateDTO) : ExchangeRate {
        return exchangeRateDAO.save(exchange)
    }
}