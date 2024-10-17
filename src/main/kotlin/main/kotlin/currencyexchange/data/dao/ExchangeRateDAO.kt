package main.kotlin.currencyexchange.data.dao

import main.kotlin.currencyexchange.data.database.DatabaseConnector
import main.kotlin.currencyexchange.data.entities.ExchangeRate
import main.kotlin.currencyexchange.dto.ExchangeRateDTO
import main.kotlin.currencyexchange.dto.ExchangeRateMapper
import main.kotlin.currencyexchange.service.CurrencyService
import java.sql.Connection
import java.sql.SQLException

class ExchangeRateDAO : DAO<ExchangeRate, ExchangeRateDTO> {
    private val connector = DatabaseConnector()
    private val mapper = ExchangeRateMapper
    private val currencyService = CurrencyService()

    override fun getByCode(code: String): ExchangeRate {
        var exchangeRate : ExchangeRate
        try{
            connector.getConnection().use { connection ->
                exchangeRate = mapper.toModel(getByCode(code, connection))

            }
        }
        catch (e: SQLException) {
            e.printStackTrace()
            throw e
        }
        return exchangeRate
    }
    private fun getByCode(code: String, connection: Connection?) : ExchangeRateDTO {
        val codes = splitCurrencyCodes(code)
        val targetId = currencyService.getByCode(codes[0]).id
        val baseId = currencyService.getByCode(codes[1]).id
        val sql = "SELECT * FROM ExchangeRates WHERE BaseCurrencyId=? and TargetCurrencyId=?"
        var exchangeRateDTO = ExchangeRateDTO()
        connection?.prepareStatement(sql)?.use { ps ->
            ps.setInt(1, targetId)
            ps.setInt(2, baseId)
            ps.executeQuery().use { rs ->
                if (rs.next()) {
                    val id = rs.getInt(1)
                    val baseCur = currencyService.getById(rs.getInt(2))
                    val targetCur = currencyService.getById(rs.getInt(3))
                    val rate = rs.getDouble(4)
                    exchangeRateDTO = ExchangeRateDTO(id,baseCur, targetCur,rate)
                }
            }
        }
        return exchangeRateDTO
    }

    private fun splitCurrencyCodes(code: String): List<String> {
        return listOf(code.substring(0,3), code.substring(3))
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
                    val baseCurrencyId = currencyService.getById(rs.getInt(2))
                    val targetCurrencyId = currencyService.getById(rs.getInt(3))
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