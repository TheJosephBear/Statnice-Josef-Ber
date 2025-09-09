package com.example.medicineapp

import NavigationManager
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import kotlinx.coroutines.delay
import java.util.concurrent.TimeUnit

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Načtení uložených léků
        MedicineRepository.loadMedicines(this)

        // Spuštění kontrol notifikací
        scheduleNotifWorkers()

        setContent {
            // Content je řízený navigation managerem
            NavigationManager(context = this)
        }
    }

    fun scheduleNotifWorkers(){
        // Vytvoření notifikačního kanálu
        MedicineNotificationManager.createNotificationChannel(this)
        // Každou hodinu se pokus poslat notifikaci
        val workRequest = PeriodicWorkRequestBuilder<MedicineReminderWorker>(30,
            TimeUnit.MINUTES)
            .build()
        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "medicine_reminder",
            ExistingPeriodicWorkPolicy.REPLACE,
            workRequest
        )

        // Testovací kód pro okamžité spuštění workeru
        /*
            val testRequest = OneTimeWorkRequestBuilder<MedicineReminderWorker>().build()
            WorkManager.getInstance(this).enqueue(testRequest)
        */
    }
}


class MedicineReminderWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {
    override suspend fun doWork(): Result {
        // Kdykoliv chci poslat notifikaci tak potřebuji načtený seznam léků
        MedicineRepository.loadMedicines(applicationContext)
        // Pokus o poslání notifikace
        MedicineNotificationManager.checkAndNotify(applicationContext)
        return Result.success()
    }
}