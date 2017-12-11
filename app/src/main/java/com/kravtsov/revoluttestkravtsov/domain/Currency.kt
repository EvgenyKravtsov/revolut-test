package com.kravtsov.revoluttestkravtsov.domain

data class Currency(val abbreviation: String, val value: Double) {

    //// OBJECT

    override fun equals(other: Any?): Boolean {
        return other is Currency && other.abbreviation == abbreviation
    }

    override fun hashCode(): Int {
        var result = abbreviation.hashCode()
        result = 31 * result + value.hashCode()
        return result
    }
}