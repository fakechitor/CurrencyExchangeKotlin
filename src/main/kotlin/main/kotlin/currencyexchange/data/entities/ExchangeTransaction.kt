package main.kotlin.currencyexchange.data.entities

import java.math.BigDecimal

class ExchangeTransaction(
    val baseCurrency: Currency,
    val targetCurrency: Currency,
    var rate: BigDecimal,
    val amount: BigDecimal,
    var convertedAmount: BigDecimal,
)