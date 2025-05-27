package com.example.teamup

data class CardEvent(val title: String, val description: String, val date: String, val eventId: Int) {
    // Add any additional properties or methods if needed
}
data class Person(val name: String) {
    // Add any additional properties or methods if needed
}
data class Team(val name: String, val event: String, val description: String) {
    // Add any additional properties or methods if needed
}