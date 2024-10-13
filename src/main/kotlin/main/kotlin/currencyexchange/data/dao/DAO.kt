package main.kotlin.currencyexchange.data.dao

import main.kotlin.currencyexchange.data.entities.Currency
import main.kotlin.currencyexchange.dto.CurrencyDTO

interface DAO {
    fun getByCode(code: String) : Currency?
    fun getAll() : List<Currency>
    fun save(currencyDTO: CurrencyDTO)
}