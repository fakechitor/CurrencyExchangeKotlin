package main.kotlin.currencyexchange.dto

import main.kotlin.currencyexchange.data.entities.ExchangeRate

object ExchangeRateMapper {
    fun toDTO(exchangeRate : ExchangeRate) : ExchangeRateDTO {
        return ExchangeRateDTO(
            id = exchangeRate.id,
            baseCurrency = exchangeRate.baseCurrency,
            targetCurrency = exchangeRate.targetCurrency,
            rate = exchangeRate.rate,
        )
    }

    fun toModel(eDTO: ExchangeRateDTO) : ExchangeRate {
        return ExchangeRate(
            id = eDTO.id ?: 0,
            baseCurrency = eDTO.baseCurrency ?: throw IllegalArgumentException(),
            targetCurrency = eDTO.targetCurrency ?: throw IllegalArgumentException(),
            rate = eDTO.rate ?: 0.0,
        )
    }
}