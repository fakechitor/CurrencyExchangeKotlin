package main.kotlin.currencyexchange.utils

class Validation {
    @Throws(IllegalArgumentException::class)
    fun isNotEmpty(elements : List<String>) : Boolean {
        for (i in elements.indices) {
            if (elements[i] == "") {
                throw IllegalArgumentException()
            }
        }
        return true
    }

    @Throws(IllegalArgumentException::class)
    fun isCurrencyCodeValid(code : String){
        if (!isCodeContainsOnlyEnglish(code) || code.length != 3){
            throw IllegalArgumentException()
        }
    }

    private fun isCodeContainsOnlyEnglish(code : String) : Boolean {
        for (i in code.chars()){
            if (i !in 65..90){
                return false
            }
        }
        return true
    }
}