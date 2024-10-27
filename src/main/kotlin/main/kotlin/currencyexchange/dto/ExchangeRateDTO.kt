package main.kotlin.currencyexchange.dto

import main.kotlin.currencyexchange.data.entities.Currency

data class ExchangeRateDTO(
    val id : Int? = null,
    val baseCurrency : Currency? = null,
    val targetCurrency : Currency? = null,
    val rate : Double? = null,
)