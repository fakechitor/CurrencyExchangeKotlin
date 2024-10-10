package main.kotlin.currencyexchange.data.dao

import main.kotlin.currencyexchange.data.entities.Entity

class CurrencyDAO : DAO{
    fun getCurrencyList(){

    }

    override fun get(id: Int): Entity {
        TODO("Not yet implemented")
    }

    override fun getAll(): List<Entity> {
        return listOf()
    }

    override fun save(entity: Entity) {
        TODO("Not yet implemented")
    }
}