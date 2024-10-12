package main.kotlin.currencyexchange.data.database

import main.kotlin.currencyexchange.data.entities.Currency
import java.sql.Connection
import java.sql.DriverManager


class DatabaseConnector() {
    private val dbURL = "jdbc:sqlite:C:/Users/Lenovo/IdeaProjects/CurrencyExchangeKotlin/src/main/resources/webapp/WEB-INF/currencies.sqlite"

    fun getConnection(): Connection? {
        return try {
            Class.forName("org.sqlite.JDBC")
            DriverManager.getConnection(dbURL)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}