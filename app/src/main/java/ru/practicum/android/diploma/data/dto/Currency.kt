package ru.practicum.android.diploma.data.dto

enum class Currency(val code: String, val symbol: String, val description: String) {
    USD("USD", "$", "Доллар США"),
    EUR("EUR", "€", "Евро"),
    RUR("RUR", "₽", "Российский рубль"),
    BYR("BYR", "Br", "Белорусский рубль"),
    KZT("KZT", "₸", "Казахстанский тенге"),
    UAH("UAH", "₴", "Украинская гривна"),
    AZN("AZN", "₼", "Азербайджанский манат"),
    UZS("UZS", "лв", "Узбекский сум"),
    GEL("GEL", "₾", "Грузинский лари"),
    KGT("KGT", "\u20C0", "Киргизский сом"),
    NONE("NONE", "0", "Неопознанная валюта");

    override fun toString(): String {
        return "$name ($code, $symbol)"
    }

    companion object {
        fun toCurrency(code: String): Currency {
            return entries.find { it.code == code } ?: NONE
        }
    }
}
