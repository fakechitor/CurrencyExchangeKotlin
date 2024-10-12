package main.kotlin.currencyexchange.data.dao

import main.kotlin.currencyexchange.data.database.DatabaseConnector
import main.kotlin.currencyexchange.data.entities.Currency
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
        try{
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
        }
        catch (e: SQLException) {
            e.printStackTrace()
        }
        return currencyList
    }

    override fun save(Currency: Currency) {
        TODO("Not yet implemented")

    }
}