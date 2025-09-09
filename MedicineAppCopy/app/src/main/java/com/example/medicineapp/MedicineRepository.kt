package com.example.medicineapp

import android.content.Context
import androidx.compose.runtime.mutableStateListOf

object MedicineRepository {

    // Seznam léků, mutable pro aktualizace ui
    private val _medicines = mutableStateListOf<Medicine>()
    val medicines: List<Medicine> get() = _medicines

    // Načte léky z úložiště
    fun loadMedicines(context: Context) {
        _medicines.clear()
        _medicines.addAll(MedicineStorage.loadMedicines(context))
    }

    // Přidá nový lék a uloží celý seznam do úložiště
    fun addMedicine(context: Context, medicine: Medicine) {
        _medicines.add(medicine)
        MedicineStorage.saveMedicines(context, _medicines)
    }

    // Vymaže všechny léky z paměti i úložiště
    fun clearAll(context: Context) {
        _medicines.clear()
        MedicineStorage.clearAllMedicines(context)
    }

    // Odstraní jeden lék z paměti a uložiště
    fun removeMedicine(context: Context, medicine: Medicine) {
        _medicines.remove(medicine)
        MedicineStorage.saveMedicines(context, _medicines)
    }

    fun getMedicinesAlphabeticalAsc(): List<Medicine> =
        _medicines.sortedBy { it.name }

    fun getMedicinesAlphabeticalDesc(): List<Medicine> =
        _medicines.sortedByDescending { it.name }

    fun getMedicinesByNearestTime(): List<Medicine> =
        emptyList()

}
