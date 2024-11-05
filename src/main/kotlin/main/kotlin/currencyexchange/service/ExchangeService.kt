package main.kotlin.currencyexchange.service

import main.kotlin.currencyexchange.dao.JdbcCurrencyDAO
import main.kotlin.currencyexchange.dao.JdbcExchangeRateDAO
import main.kotlin.currencyexchange.data.entities.ExchangeRate
import main.kotlin.currencyexchange.data.entities.ExchangeTransaction
import main.kotlin.currencyexchange.dto.ExchangeRateDTO
import main.kotlin.currencyexchange.exceptions.CurrencyCodeIsNotExists
import main.kotlin.currencyexchange.exceptions.ExchangeRateIsNotExists
import java.math.BigDecimal
import java.math.RoundingMode

class ExchangeService {
    private val jdbcExchangeRateDAO = JdbcExchangeRateDAO()
    private val jdbcCurrencyDAO = JdbcCurrencyDAO()

    fun getByCode(code : String) : ExchangeRate {
        val exchangeRate = jdbcExchangeRateDAO.getByCode(code)
        if (exchangeRate.id == 0) {
            throw CurrencyCodeIsNotExists()
        }
        return exchangeRate
    }

    fun getAll() : List<ExchangeRateDTO> {
        return jdbcExchangeRateDAO.getAll()
    }

    fun save(exchange: ExchangeRateDTO) : ExchangeRate {
        return jdbcExchangeRateDAO.save(exchange)
    }

    fun patchRate(code: String, rate: Double): ExchangeRate {
        return jdbcExchangeRateDAO.patchData(code, rate)
    }

    fun exchange(exchangeData: List<String>): ExchangeTransaction {
        val baseCurrCode = exchangeData[0]
        val targetCurrCode = exchangeData[1]
        val amount = BigDecimal(exchangeData[2])
        val exchangeTransaction = ExchangeTransaction(
            baseCurrency = jdbcCurrencyDAO.getByCode(baseCurrCode),
            targetCurrency = jdbcCurrencyDAO.getByCode(targetCurrCode),
            rate = BigDecimal(0),
            amount = amount.setScale(2, RoundingMode.HALF_UP),
            convertedAmount = BigDecimal(0),
        )
        if (jdbcExchangeRateDAO.getByCode(baseCurrCode + targetCurrCode).id != 0) {
            val exchangeRate = BigDecimal(jdbcExchangeRateDAO.getByCode(baseCurrCode + targetCurrCode).rate)
            exchangeTransaction.rate = exchangeRate.setScale(5, RoundingMode.HALF_UP)
            exchangeTransaction.convertedAmount = amount.times(exchangeRate).setScale(2, RoundingMode.HALF_UP)
        } else if (jdbcExchangeRateDAO.getByCode(targetCurrCode + baseCurrCode).id != 0) {
            var exchangeRate = BigDecimal(jdbcExchangeRateDAO.getByCode(targetCurrCode + baseCurrCode).rate)
            val a = BigDecimal("1")
            exchangeRate = a.divide(exchangeRate, 10, RoundingMode.HALF_UP)
            exchangeTransaction.rate = exchangeRate.setScale(5, RoundingMode.HALF_UP)
            exchangeTransaction.convertedAmount = amount.times(exchangeRate).setScale(2, RoundingMode.HALF_UP)
        } else if (jdbcExchangeRateDAO.getByCode("USD$baseCurrCode").id != 0 && jdbcExchangeRateDAO.getByCode("USD$targetCurrCode").id != 0) {
            val USDToBaseCurr = BigDecimal(jdbcExchangeRateDAO.getByCode("USD$baseCurrCode").rate.toString())
            val USDToTargetCurr = BigDecimal(jdbcExchangeRateDAO.getByCode("USD$targetCurrCode").rate.toString())
            val exchangeRate = USDToTargetCurr.divide(USDToBaseCurr,5,RoundingMode.HALF_UP)
            exchangeTransaction.rate = exchangeRate
            exchangeTransaction.convertedAmount = amount.multiply(exchangeRate).setScale(2, RoundingMode.HALF_UP)
        }
        else{
            throw ExchangeRateIsNotExists()
        }
        return exchangeTransaction
    }


}