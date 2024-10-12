package main.kotlin.currencyexchange.data.entities

class Currency(
    val id : Int,
    val currencyCode : String,
    val name : String,
    val sign: String,
)
{
constructor(
    currencyCode : String,
    name : String,
    sign: String,
) : this(0, currencyCode, name, sign)
}