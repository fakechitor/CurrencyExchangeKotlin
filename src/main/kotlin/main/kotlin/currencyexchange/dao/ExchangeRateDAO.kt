package main.kotlin.currencyexchange.dao

import main.kotlin.currencyexchange.data.entities.ExchangeRate
import main.kotlin.currencyexchange.dto.ExchangeRateDTO

interface ExchangeRateDAO : DAO<ExchangeRate, ExchangeRateDTO> {
    override fun getByCode(code: String): ExchangeRate
    override fun getAll(): MutableList<ExchangeRateDTO>
    override fun save(item: ExchangeRateDTO): ExchangeRate
}