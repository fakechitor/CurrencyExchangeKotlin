package main.kotlin.currencyexchange.data.dao

import main.kotlin.currencyexchange.data.entities.Entity

interface DAO {
    fun get(id : Int) : Entity
    fun getAll() : List<Entity>
    fun save(entity: Entity)
    
}