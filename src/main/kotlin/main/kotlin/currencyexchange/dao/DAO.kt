package main.kotlin.currencyexchange.dao

interface DAO<T,K> {
    fun getByCode(code : String) : T
    fun getAll() : MutableList<K>
    fun save(item: K) : T
}