package main.kotlin.currencyexchange.service

import main.kotlin.currencyexchange.dao.CurrencyDAO
import main.kotlin.currencyexchange.data.entities.Currency
import main.kotlin.currencyexchange.dto.CurrencyDTO
import main.kotlin.currencyexchange.exceptions.CurrencyCodeIsNotExists

class CurrencyService {
    private val currencyDAO = CurrencyDAO()

    @Throws(CurrencyCodeIsNotExists::class)
    fun getByCode(code : String) : Currency {
        val currency = currencyDAO.getByCode(code)
        if (currency.id!=0){
            return currency
        }
        throw CurrencyCodeIsNotExists()
    }
    fun getById(id : Int) : Currency? {
        return currencyDAO.getById(id)
    }
    fun getAll() : List<CurrencyDTO> {
        return currencyDAO.getAll()
    }
    fun save(currencyDTO: CurrencyDTO) : Currency{
        return currencyDAO.save(currencyDTO)
    }
}
