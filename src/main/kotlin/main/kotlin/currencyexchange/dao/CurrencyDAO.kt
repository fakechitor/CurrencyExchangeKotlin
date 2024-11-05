package main.kotlin.currencyexchange.dao

import main.kotlin.currencyexchange.data.entities.Currency
import main.kotlin.currencyexchange.dto.CurrencyDTO

interface CurrencyDAO<T, U> : DAO<Currency, CurrencyDTO> {
    override fun getByCode(code: String): Currency

    override fun getAll(): MutableList<CurrencyDTO>

    override fun save(item: CurrencyDTO): Currency
}