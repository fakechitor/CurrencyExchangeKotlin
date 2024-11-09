package main.kotlin.currencyexchange.data.model

import java.math.BigDecimal

class ExchangeTransaction(
    val baseCurrency: Currency,
    val targetCurrency: Currency,
    var rate: BigDecimal,
    val amount: BigDecimal,
    var convertedAmount: BigDecimal,
)