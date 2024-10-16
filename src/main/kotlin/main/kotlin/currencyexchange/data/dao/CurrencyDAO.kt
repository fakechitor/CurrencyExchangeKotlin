package main.kotlin.currencyexchange.data.dao

import main.kotlin.currencyexchange.data.database.DatabaseConnector
import main.kotlin.currencyexchange.data.entities.Currency
import main.kotlin.currencyexchange.dto.CurrencyDTO
import main.kotlin.currencyexchange.exceptions.CurrencyAlreadyExistsException
import java.sql.Connection
import java.sql.SQLException

class CurrencyDAO() : DAO<Currency, CurrencyDTO>{

    private val connector = DatabaseConnector()
    private val mapper = main.kotlin.currencyexchange.dto.CurrencyMapper

    override fun getByCode(code : String): Currency {
        var currency : Currency
        try{
            connector.getConnection().use { connection ->
                currency = mapper.toModel(getByCode(code, connection))
            }
        }
        catch (e: SQLException) {
            e.printStackTrace()
            throw e
        }
        return currency

    }

    private fun getByCode(currencyCode: String, connection: Connection?): CurrencyDTO {
        val sql = "SELECT ID, Code, FullName, Sign FROM Currencies WHERE Code=?"
        var currencyDTO = CurrencyDTO()
        connection?.prepareStatement(sql)?.use { ps ->
            ps.setString(1, currencyCode)
            ps.executeQuery().use { rs ->
                if (rs.next()) {
                    val id = rs.getInt(1)
                    val code = rs.getString(2)
                    val fullName = rs.getString(3)
                    val sign = rs.getString(4)
                    currencyDTO = CurrencyDTO(id, code, fullName, sign)
                }
            }
        }
        return currencyDTO
    }

    override fun getById(id : Int) : Currency {
        var currency : Currency
        try {
            connector.getConnection().use { connection ->
                currency = getById(id,connection)
            }
        }
        catch (e: SQLException) {
            e.printStackTrace()
            throw e
        }
        return currency
    }

    private fun getById(currencyId : Int, connection: Connection?) : Currency {
        val sql = "SELECT ID, Code, FullName, Sign FROM Currencies WHERE Id=?"
        var currency : Currency? = null
        connection?.prepareStatement(sql)?.use { ps ->
            ps.setInt(1, currencyId)
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
        return currency!!

    }

    override fun getAll(): MutableList<Currency> {
        val currencyList : MutableList<Currency> = mutableListOf()
        val query = ("SELECT * FROM Currencies")
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

    override fun save(item: CurrencyDTO) {
        try {
            isCurrencyExist(item)
            val sql = "INSERT INTO Currencies (Code, FullName, Sign) VALUES (?, ?, ?)"
            connector.getConnection()?.use { connection ->
                connection.prepareStatement(sql).use { ps ->
                    ps.setString(1, item.currencyCode)
                    ps.setString(2, item.name)
                    ps.setString(3, item.sign)
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
        if (currency.id  != 0){
            throw CurrencyAlreadyExistsException()
        }
    }
}