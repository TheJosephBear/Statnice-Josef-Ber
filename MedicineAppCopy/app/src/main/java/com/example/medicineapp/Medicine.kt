package com.example.medicineapp

data class Medicine (
    val name : String,
    val altName : String,
    val description : String,
    val howManyToTake : Int,
    val whenToTake : List<TimeOfDay>,
    val imageUri: String? // Odkaz na obrázek v galerii
)

enum class TimeOfDay {
    MORNING,
    NOON,
    EVENING
}