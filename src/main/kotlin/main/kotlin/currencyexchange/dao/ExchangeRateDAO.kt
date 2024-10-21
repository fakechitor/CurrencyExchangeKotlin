package main.kotlin.currencyexchange.dao

import main.kotlin.currencyexchange.data.database.DatabaseConnector
import main.kotlin.currencyexchange.data.entities.Currency
import main.kotlin.currencyexchange.data.entities.ExchangeRate
import main.kotlin.currencyexchange.dto.ExchangeRateDTO
import main.kotlin.currencyexchange.dto.ExchangeRateMapper
import main.kotlin.currencyexchange.exceptions.CurrencyAlreadyExistsException
import main.kotlin.currencyexchange.utils.Utils
import java.sql.Connection
import java.sql.SQLException

class ExchangeRateDAO : DAO<ExchangeRate, ExchangeRateDTO> {
    private val connector = DatabaseConnector()
    private val mapper = ExchangeRateMapper
    private val currencyDAO = CurrencyDAO()
    private val utils = Utils()

    override fun getByCode(code: String): ExchangeRate {
        var exchangeRate: ExchangeRate?
        try {
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

    private fun getByCode(code: String, connection: Connection?): ExchangeRateDTO {
        val codes = utils.splitCurrencyCodes(code)
//        val targetId = currencyDAO.getByCode(codes[0]).id
//        val baseId = currencyDAO.getByCode(codes[1]).id
        val sql = "SELECT ExchangeRates.ID, c.Code, c2.Code, Rate FROM ExchangeRates\n" +
                "         JOIN main.Currencies c ON c.ID = ExchangeRates.BaseCurrencyId\n" +
                "         JOIN main.Currencies c2 ON c2.ID = ExchangeRates.TargetCurrencyId\n" +
                "WHERE c.Code = ? AND c2.Code = ?;\n"
        var exchangeRateDTO = ExchangeRateDTO()
        connection?.prepareStatement(sql)?.use { ps ->
            ps.setString(1, codes[0])
            ps.setString(2, codes[1])
            ps.executeQuery().use { rs ->
                if (rs.next()) {
                    val id = rs.getInt(1)
                    val baseCur = currencyDAO.getByCode(rs.getString(2))
                    val targetCur = currencyDAO.getByCode(rs.getString(3))
                    val rate = rs.getDouble(4)
                    exchangeRateDTO = ExchangeRateDTO(id, baseCur, targetCur, rate)
                }

            }
        }
        return exchangeRateDTO
    }

    override fun getAll(): MutableList<ExchangeRateDTO> {
        val exchangeRatesList: MutableList<ExchangeRateDTO> = mutableListOf()
        val query = "SELECT * FROM ExchangeRates"
        connector.getConnection().use { conn ->
            conn!!.createStatement().use { stmt ->
                val rs = stmt.executeQuery(query)
                while (rs.next()) {
                    val id: Int = rs.getInt(1)
                    val baseCurrency = currencyDAO.getById(rs.getInt(2))
                    val targetCurrency = currencyDAO.getById(rs.getInt(3))
                    val rate: Double = rs.getDouble(4)
                    exchangeRatesList.add(ExchangeRateDTO(id, baseCurrency, targetCurrency, rate))
                }
            }
        }
        return exchangeRatesList
    }

    override fun save(item: ExchangeRateDTO): ExchangeRate {
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
