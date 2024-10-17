package main.kotlin.currencyexchange.data.dao

interface DAO<T,K> {
    fun getByCode(code : String) : T
    fun getById(id : Int) : T
    fun getAll() : MutableList<T>
    fun save(item: K) : T
}