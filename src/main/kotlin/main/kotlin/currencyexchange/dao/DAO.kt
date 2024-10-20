package main.kotlin.currencyexchange.dao

interface DAO<T,K> {
    fun getByCode(code : String) : T
    fun getAll() : MutableList<T>
    fun save(item: K) : T
}