package main.kotlin.currencyexchange.dto

import main.kotlin.currencyexchange.data.entities.Currency

object CurrencyMapper {
    fun toDTO(currency : Currency) : CurrencyDTO {
        return CurrencyDTO(
            id = currency.id,
            code = currency.currencyCode,
            name = currency.name,
            sign = currency.sign,
        )
    }
    fun toModel(c : CurrencyDTO) : Currency {
        return Currency(
            id = c.id ?: 0,
            currencyCode = c.code ?: "",
            name = c.name ?: "",
            sign = c.sign ?: ""
        )
    }
}