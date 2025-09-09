import android.content.Context
import com.example.medicineapp.Medicine
import com.google.gson.reflect.TypeToken
import com.google.gson.Gson

object MedicineStorage {

    private const val PREFS_NAME = "medicine_prefs"
    private const val KEY_MEDICINES = "medicines_list"

    // Uloží seznam léků jako JSON do SharedPreferences
    // Shared prefferences = trvalé úložiště aplikace, json
    fun saveMedicines(context: Context, medicines: List<Medicine>) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val json = Gson().toJson(medicines)
        prefs.edit().putString(KEY_MEDICINES, json).apply()
    }

    // Načte seznam léků ze SharedPreferences a převede JSON zpět na seznam objektů
    fun loadMedicines(context: Context): List<Medicine> {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val json = prefs.getString(KEY_MEDICINES, null)
        return if (json != null) {
            val type = object : TypeToken<List<Medicine>>() {}.type
            Gson().fromJson(json, type)
        } else {
            emptyList()
        }
    }

    // Vymaže celý seznam léků ze SharedPreferences
    fun clearAllMedicines(context: Context) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().remove(KEY_MEDICINES).apply()
    }

    // Odstraní jeden lék ze SharedPreferences
    fun removeMedicine(context: Context, medicine: Medicine) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

        // Load current list
        val currentList = loadMedicines(context).toMutableList()

        // Remove the medicine
        currentList.remove(medicine)

        // Save updated list
        saveMedicines(context, currentList)
    }

}
