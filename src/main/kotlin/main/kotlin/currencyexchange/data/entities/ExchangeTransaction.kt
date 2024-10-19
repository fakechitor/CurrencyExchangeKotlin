package main.kotlin.currencyexchange.data.entities

class ExchangeTransaction(
    val baseCurrency: Currency,
    val targetCurrency: Currency,
    var rate: Double,
    val amount: Double,
    var convertedAmount: Double,
)