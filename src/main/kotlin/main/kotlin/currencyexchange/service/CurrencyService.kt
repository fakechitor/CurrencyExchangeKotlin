package main.kotlin.currencyexchange.service

import main.kotlin.currencyexchange.dao.CurrencyDAO
import main.kotlin.currencyexchange.data.entities.Currency
import main.kotlin.currencyexchange.dto.CurrencyDTO

class CurrencyService {
    private val currencyDAO = CurrencyDAO()

    fun getByCode(code : String) : Currency {
        return currencyDAO.getByCode(code)
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
