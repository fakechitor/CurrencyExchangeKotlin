package main.kotlin.currencyexchange.service

import main.kotlin.currencyexchange.dao.JdbcCurrencyDAO
import main.kotlin.currencyexchange.data.entities.Currency
import main.kotlin.currencyexchange.dto.CurrencyDTO
import main.kotlin.currencyexchange.exceptions.CurrencyCodeIsNotExists

class CurrencyService {
    private val jdbcCurrencyDAO = JdbcCurrencyDAO()

    @Throws(CurrencyCodeIsNotExists::class)
    fun getByCode(code : String) : Currency {
        val currency = jdbcCurrencyDAO.getByCode(code)
        if (currency.id!=0){
            return currency
        }
        throw CurrencyCodeIsNotExists()
    }
    fun getAll() : List<CurrencyDTO> {
        return jdbcCurrencyDAO.getAll()
    }
    fun save(currencyDTO: CurrencyDTO) : Currency{
        return jdbcCurrencyDAO.save(currencyDTO)
    }
}
