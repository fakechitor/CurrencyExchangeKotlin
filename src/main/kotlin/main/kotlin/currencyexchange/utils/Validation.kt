package main.kotlin.currencyexchange.utils

class Validation {
    @Throws(IllegalArgumentException::class)
    fun makeCodesValidation(code: String) {
        if (code.length != 3 && code.length != 6) {
            throw IllegalArgumentException()
        }
    }

    @Throws(IllegalArgumentException::class)
    fun makeExchangeRateValidation(baseCode: String, targetCode : String,rate : Double){
        isCurrencyCodeValid(baseCode)
        isCurrencyCodeValid(targetCode)
        isRatePositive(rate)
    }

    @Throws(IllegalArgumentException::class)
    fun makeCurrencyValidation(code: String, name : String, sign : String){
            isCurrencyCodeValid(code)
            isNotEmpty(listOf(code,name,sign))
    }

    @Throws(IllegalArgumentException::class)
    private fun isNotEmpty(elements : List<String>) : Boolean {
        for (i in elements.indices) {
            if (elements[i] == "") {
                throw IllegalArgumentException()
            }
        }
        return true
    }

    @Throws(IllegalArgumentException::class)
    private fun isCurrencyCodeValid(code : String){
        if (!isCodeContainsOnlyEnglish(code) || code.length != 3){
            throw IllegalArgumentException()
        }
    }

    @Throws(IllegalArgumentException::class)
    private fun isRatePositive(rate: Double){
        if (rate <= 0.0){
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