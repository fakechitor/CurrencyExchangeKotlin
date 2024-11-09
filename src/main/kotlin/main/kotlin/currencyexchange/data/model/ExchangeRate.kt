package main.kotlin.currencyexchange.data.model

class ExchangeRate(
    val id : Int,
    val baseCurrency : Currency,
    val targetCurrency : Currency,
    val rate : Double,
)