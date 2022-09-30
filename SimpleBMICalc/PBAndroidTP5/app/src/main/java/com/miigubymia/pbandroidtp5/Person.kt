package com.miigubymia.pbandroidtp5

data class Person (
    var weight: Double,
    var height:Double
        ) {
    var bodyMassIndex = 0.0

    fun getBodyMassIndex (person: Person, weight: Double, height: Double): Person {
        weight.toString().toDouble()
        height.toString().toDouble()
        var bodyMassIndex = weight / (height * height)
        person.bodyMassIndex = bodyMassIndex
        return person
    }
}