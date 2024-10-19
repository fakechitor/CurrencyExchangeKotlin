package main.kotlin.currencyexchange.service

import main.kotlin.currencyexchange.data.dao.CurrencyDAO
import main.kotlin.currencyexchange.data.dao.ExchangeRateDAO
import main.kotlin.currencyexchange.data.entities.ExchangeRate
import main.kotlin.currencyexchange.data.entities.ExchangeTransaction
import main.kotlin.currencyexchange.dto.ExchangeRateDTO

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
        val amount = exchangeData[2].toDouble()
        val exchangeTransaction = ExchangeTransaction(
            baseCurrency = currencyDAO.getByCode(baseCurrCode),
            targetCurrency = currencyDAO.getByCode(targetCurrCode),
            rate = 0.0,
            amount = amount,
            convertedAmount = 0.0,
        )
        if (exchangeRateDAO.getByCode(baseCurrCode + targetCurrCode).id != 0) {
            val exchangeRate = exchangeRateDAO.getByCode(baseCurrCode + targetCurrCode).rate
            exchangeTransaction.rate = exchangeRate
            exchangeTransaction.convertedAmount = amount * exchangeRate
        } else if (exchangeRateDAO.getByCode(targetCurrCode + baseCurrCode).id != 0) {
            val exchangeRate = 1 / exchangeRateDAO.getByCode(targetCurrCode + baseCurrCode).rate
            exchangeTransaction.rate = exchangeRate
            exchangeTransaction.convertedAmount = amount * exchangeRate
        } else if (exchangeRateDAO.getByCode("USD$baseCurrCode").id != 0 && exchangeRateDAO.getByCode("USD$targetCurrCode").id != 0) {
            val USDToBaseCurr = exchangeRateDAO.getByCode("USD$baseCurrCode").rate
            val USDToTargetCurr = exchangeRateDAO.getByCode("USD$targetCurrCode").rate
            val exchangeRate = USDToTargetCurr / USDToBaseCurr
            exchangeTransaction.rate = exchangeRate
            exchangeTransaction.convertedAmount = amount * exchangeRate
        }
        return exchangeTransaction
    }
}