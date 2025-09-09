import android.content.Context
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.medicineapp.AddMedicineView

sealed class Screen(val route: String) {
    object BrowseMedicine : Screen("browse_medicine")
    object AddMedicine : Screen("add_medicine")
}

@Composable
fun NavigationManager(navController: NavHostController = rememberNavController(), context : Context) {
    // Navigation manager přepíná různé pohledy
    NavHost(navController = navController, startDestination = Screen.BrowseMedicine.route) {
        composable(Screen.BrowseMedicine.route) {
            BrowseMedicineView(navController = navController)
        }
        composable(Screen.AddMedicine.route) {
            AddMedicineView(navController = navController, context = context)
        }
    }
}
