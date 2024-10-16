package main.kotlin.currencyexchange.data.dao

import main.kotlin.currencyexchange.data.database.DatabaseConnector
import main.kotlin.currencyexchange.data.entities.ExchangeRate
import main.kotlin.currencyexchange.dto.ExchangeRateDTO

class ExchangeRateDAO : DAO<ExchangeRate, ExchangeRateDTO> {
    private val connector = DatabaseConnector()
    private val currencyDAO = CurrencyDAO()

    override fun getByCode(code: String): ExchangeRate {
        var exchangeRate : ExchangeRate? = null
        TODO()
    }

    override fun getById(id: Int): ExchangeRate {
        TODO("Not yet implemented")
    }

    override fun getAll(): MutableList<ExchangeRate> {
        val exchangeRatesList : MutableList<ExchangeRate> = mutableListOf()
        val query = "SELECT * FROM ExchangeRates"
        connector.getConnection().use { conn ->
            conn!!.createStatement().use { stmt ->
                val rs = stmt.executeQuery(query)
                while (rs.next()) {
                    val id : Int = rs.getInt(1)
                    val baseCurrencyId = currencyDAO.getById(rs.getInt(2))
                    val targetCurrencyId = currencyDAO.getById(rs.getInt(3))
                    val rate : Double = rs.getDouble(4)
                    exchangeRatesList.add(ExchangeRate(id, baseCurrencyId, targetCurrencyId,rate))
                }
            }
        }
        return exchangeRatesList
    }

    override fun save(item: ExchangeRateDTO) {
        TODO("Not yet implemented")
    }

}