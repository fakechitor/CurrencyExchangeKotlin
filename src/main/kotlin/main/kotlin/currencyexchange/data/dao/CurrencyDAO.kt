package main.kotlin.currencyexchange.data.dao

import main.kotlin.currencyexchange.data.database.DatabaseConnector
import main.kotlin.currencyexchange.data.entities.Currency
import main.kotlin.currencyexchange.dto.CurrencyDTO
import main.kotlin.currencyexchange.exceptions.CurrencyAlreadyExistsException
import java.sql.Connection
import java.sql.SQLException

class CurrencyDAO() : DAO{

    private val connector = DatabaseConnector()

    override fun getByCode(code: String): Currency? {
        var currency : Currency? = null
        try{
            connector.getConnection().use { connection ->
                currency = getByCode(code, connection)
            }
        }
        catch (e: SQLException) {
            e.printStackTrace()
            throw e
        }
        return currency

    }
    private fun getByCode(currencyCode: String, connection: Connection?): Currency? {
        val sql = "SELECT ID, Code, FullName, Sign FROM Currencies WHERE Code=?"
        var currency: Currency? = null
        connection?.prepareStatement(sql)?.use { ps ->
            ps.setString(1, currencyCode)
            ps.executeQuery().use { rs ->
                if (rs.next()) {
                    val id = rs.getInt(1)
                    val code = rs.getString(2)
                    val fullName = rs.getString(3)
                    val sign = rs.getString(4)
                    currency = Currency(id, code, fullName, sign)
                }
            }
        }
        return currency
    }

    override fun getAll(): MutableList<Currency> {
        val currencyList : MutableList<Currency> = mutableListOf()
        val query = ("SELECT * FROM Currencies")
        // TODO() try to delete try-catch

            connector.getConnection().use { connection ->
                connection!!.createStatement().use { statement ->
                    val rs = statement.executeQuery(query)
                    while (rs.next()) {
                        val id = rs.getInt(1)
                        val code = rs.getString(2)
                        val fullName = rs.getString(3)
                        val sign = rs.getString(4)
                        currencyList.add(Currency(id, code, fullName, sign))
                    }
                    rs.close()
                }
            }

        return currencyList
    }

    override fun save(currencyDTO: CurrencyDTO) {
        try {
            isCurrencyExist(currencyDTO)
            val sql = "INSERT INTO Currencies (Code, FullName, Sign) VALUES (?, ?, ?)"
            connector.getConnection()?.use { connection ->
                connection.prepareStatement(sql).use { ps ->
                    ps.setString(1, currencyDTO.currencyCode)
                    ps.setString(2, currencyDTO.name)
                    ps.setString(3, currencyDTO.sign)
                    ps.executeUpdate()
                }
            }
        } catch (e: SQLException) {
            e.printStackTrace()
            throw e
        }
    }

    private fun isCurrencyExist(currencyDTO: CurrencyDTO){
        val currency = getByCode(currencyDTO.currencyCode!!)
        if (currency != null){
            throw CurrencyAlreadyExistsException()
        }
    }


}