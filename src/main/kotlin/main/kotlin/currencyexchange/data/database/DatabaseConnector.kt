package main.kotlin.currencyexchange.data.database

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import java.io.File
import java.nio.file.Paths
import java.sql.Connection

class DatabaseConnector {
    private val dbURL: String =
        "jdbc:sqlite:" + Paths.get(
            File(this::class.java.classLoader.getResource("currencies.sqlite").file).absolutePath
        ).toString()
    private val dataSource: HikariDataSource


    init {
        val config = HikariConfig().apply {
            jdbcUrl = dbURL
            driverClassName = "org.sqlite.JDBC"
            maximumPoolSize = 10
            minimumIdle = 5
            connectionTimeout = 30000
            idleTimeout = 600000
            maxLifetime = 1800000
        }
        dataSource = HikariDataSource(config)
    }

    fun getConnection(): Connection? {
        return try {
            dataSource.connection
        } catch (e: Exception) {
            e.printStackTrace()
            throw e
        }
    }

    fun close() {
        if (!dataSource.isClosed) {
            dataSource.close()
        }
    }
}
