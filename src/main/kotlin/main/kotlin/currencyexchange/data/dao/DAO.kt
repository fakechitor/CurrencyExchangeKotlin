package main.kotlin.currencyexchange.data.dao

import main.kotlin.currencyexchange.data.entities.Currency

interface DAO {
    fun getByCode(code : String) : Currency?
    fun getAll() : List<Currency>
    fun save(currency: Currency)
}