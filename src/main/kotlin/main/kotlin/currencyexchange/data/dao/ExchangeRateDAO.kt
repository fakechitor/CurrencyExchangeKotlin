package main.kotlin.currencyexchange.data.dao

import main.kotlin.currencyexchange.data.database.DatabaseConnector
import main.kotlin.currencyexchange.data.entities.Currency
import main.kotlin.currencyexchange.data.entities.ExchangeRate
import main.kotlin.currencyexchange.dto.ExchangeRateDTO
import main.kotlin.currencyexchange.dto.ExchangeRateMapper
import main.kotlin.currencyexchange.exceptions.CurrencyAlreadyExistsException
import java.sql.Connection
import java.sql.SQLException

class ExchangeRateDAO : DAO<ExchangeRate, ExchangeRateDTO> {
    private val connector = DatabaseConnector()
    private val mapper = ExchangeRateMapper
    private val currencyDAO = CurrencyDAO()

    override fun getByCode(code: String): ExchangeRate {
        var exchangeRate: ExchangeRate?
        try{
            connector.getConnection().use { connection ->
                exchangeRate = mapper.toModel(getByCode(code, connection))
            }
        } catch (e: SQLException) {
            e.printStackTrace()
            throw e
        }
        if (exchangeRate != null) {
            return exchangeRate as ExchangeRate
        }
        return ExchangeRate(0, Currency(0, "", "", ""), Currency(0, "", "", ""), 0.0)
    }
    private fun getByCode(code: String, connection: Connection?) : ExchangeRateDTO {
        val codes = splitCurrencyCodes(code)
        val targetId = currencyDAO.getByCode(codes[0]).id
        val baseId = currencyDAO.getByCode(codes[1]).id
        val sql = "SELECT * FROM ExchangeRates WHERE BaseCurrencyId=? and TargetCurrencyId=?"
        var exchangeRateDTO = ExchangeRateDTO()
        connection?.prepareStatement(sql)?.use { ps ->
            ps.setInt(1, targetId)
            ps.setInt(2, baseId)
            ps.executeQuery().use { rs ->
                if (rs.next()) {
                    val id = rs.getInt(1)
                    val baseCur = currencyDAO.getById(rs.getInt(2))
                    val targetCur = currencyDAO.getById(rs.getInt(3))
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
                    val baseCurrencyId = currencyDAO.getById(rs.getInt(2))
                    val targetCurrencyId = currencyDAO.getById(rs.getInt(3))
                    val rate : Double = rs.getDouble(4)
                    exchangeRatesList.add(ExchangeRate(id, baseCurrencyId, targetCurrencyId,rate))
                }
            }
        }
        return exchangeRatesList
    }

    override fun save(item: ExchangeRateDTO) : ExchangeRate {
        var exchangeRate = ExchangeRate(
            id = 0,
            baseCurrency = item.baseCurrency!!,
            targetCurrency = item.targetCurrency!!,
            rate = item.rate!!
        )
        try {
            isExchangeRateExists(item)
            val sql = "INSERT INTO ExchangeRates (BaseCurrencyId, TargetCurrencyId, Rate) VALUES (?, ?, ?)"
            connector.getConnection()?.use { connection ->
                connection.prepareStatement(sql).use { ps ->
                    ps.setInt(1, item.baseCurrency.id)
                    ps.setInt(2, item.targetCurrency.id)
                    ps.setDouble(3, item.rate)
                    ps.executeUpdate()
                    exchangeRate = getByCode(getCodesFromDTO(item))
                }
            }
        } catch (e: CurrencyAlreadyExistsException) {
            throw e
        } catch (e: SQLException) {
            e.printStackTrace()
            throw e
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return exchangeRate
    }
    fun patchData(code: String, rate: Double): ExchangeRate {
        val query = "UPDATE ExchangeRates SET rate = ? WHERE id=?"
        connector.getConnection()?.use { conn ->
            conn.prepareStatement(query).use { ps ->
                val id = getByCode(code).id
                ps.setDouble(1, rate)
                ps.setInt(2, id)
                ps.executeUpdate()
            }
        }
        return getByCode(code)
    }

    private fun getCodesFromDTO(exchangeRateDTO: ExchangeRateDTO): String {
        val baseCode = exchangeRateDTO.baseCurrency!!.currencyCode
        val targetCode = exchangeRateDTO.targetCurrency!!.currencyCode
        return baseCode + targetCode
    }

    fun isExchangeRateExists(exchangeRateDTO: ExchangeRateDTO) {
        val exchangeRate = getByCode(getCodesFromDTO(exchangeRateDTO))
        if (exchangeRate.id != 0) {
            throw CurrencyAlreadyExistsException()
        }
    }

}