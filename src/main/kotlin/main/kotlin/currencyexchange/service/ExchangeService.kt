package main.kotlin.currencyexchange.service

import main.kotlin.currencyexchange.data.dao.CurrencyDAO
import main.kotlin.currencyexchange.data.dao.ExchangeRateDAO
import main.kotlin.currencyexchange.data.entities.ExchangeRate
import main.kotlin.currencyexchange.data.entities.ExchangeTransaction
import main.kotlin.currencyexchange.dto.ExchangeRateDTO
import java.math.BigDecimal
import java.math.RoundingMode

class ExchangeService {
    private val exchangeRateDAO = ExchangeRateDAO()
    private val currencyDAO = CurrencyDAO()

    fun getByCode(code : String) : ExchangeRate {
        val exchangeRate = exchangeRateDAO.getByCode(code)
        if (exchangeRate.id == 0) {
            throw IllegalArgumentException()
        }
        return exchangeRate
    }

    fun getAll() : List<ExchangeRate> {
        return exchangeRateDAO.getAll()
    }

    fun save(exchange: ExchangeRateDTO) : ExchangeRate {
        return exchangeRateDAO.save(exchange)
    }

    fun patchRate(code: String, rate: Double): ExchangeRate {
        return exchangeRateDAO.patchData(code, rate)
    }

    fun exchange(exchangeData: List<String>): ExchangeTransaction {
        val baseCurrCode = exchangeData[0]
        val targetCurrCode = exchangeData[1]
        val amount = BigDecimal(exchangeData[2])
        val exchangeTransaction = ExchangeTransaction(
            baseCurrency = currencyDAO.getByCode(baseCurrCode),
            targetCurrency = currencyDAO.getByCode(targetCurrCode),
            rate = BigDecimal(0),
            amount = amount.setScale(2, RoundingMode.HALF_UP),
            convertedAmount = BigDecimal(0),
        )
        if (exchangeRateDAO.getByCode(baseCurrCode + targetCurrCode).id != 0) {
            val exchangeRate = BigDecimal(exchangeRateDAO.getByCode(baseCurrCode + targetCurrCode).rate)
            exchangeTransaction.rate = exchangeRate.setScale(2, RoundingMode.HALF_UP)
            exchangeTransaction.convertedAmount = amount.times(exchangeRate).setScale(2, RoundingMode.HALF_UP)
        } else if (exchangeRateDAO.getByCode(targetCurrCode + baseCurrCode).id != 0) {
            var exchangeRate = BigDecimal(exchangeRateDAO.getByCode(targetCurrCode + baseCurrCode).rate)
            val a = BigDecimal("1")
            exchangeRate = a.divide(exchangeRate, 10, RoundingMode.HALF_UP)
            exchangeTransaction.rate = exchangeRate.setScale(2, RoundingMode.HALF_UP)
            exchangeTransaction.convertedAmount = amount.times(exchangeRate).setScale(2, RoundingMode.HALF_UP)
        } else if (exchangeRateDAO.getByCode("USD$baseCurrCode").id != 0 && exchangeRateDAO.getByCode("USD$targetCurrCode").id != 0) {
            val USDToBaseCurr = BigDecimal(exchangeRateDAO.getByCode("USD$baseCurrCode").rate)
            val USDToTargetCurr = BigDecimal(exchangeRateDAO.getByCode("USD$targetCurrCode").rate)
            val exchangeRate = USDToTargetCurr.divide(USDToBaseCurr)
            exchangeTransaction.rate = exchangeRate.setScale(2, RoundingMode.HALF_UP)
            exchangeTransaction.convertedAmount = amount.multiply(exchangeRate).setScale(2, RoundingMode.HALF_UP)
        }
        return exchangeTransaction
    }

}