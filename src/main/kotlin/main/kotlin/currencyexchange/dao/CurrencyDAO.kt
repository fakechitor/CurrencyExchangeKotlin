package main.kotlin.currencyexchange.dao

import main.kotlin.currencyexchange.data.model.Currency
import main.kotlin.currencyexchange.dto.CurrencyDTO

interface CurrencyDAO<Currency, CurrencyDTO> : DAO<Currency, CurrencyDTO> {
    override fun getByCode(code: String): Currency

    override fun getAll(): MutableList<CurrencyDTO>

    override fun save(item: CurrencyDTO): Currency
}