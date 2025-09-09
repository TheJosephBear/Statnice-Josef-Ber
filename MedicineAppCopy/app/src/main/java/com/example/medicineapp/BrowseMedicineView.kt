import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.medicineapp.Medicine
import com.example.medicineapp.MedicineRepository
import com.example.medicineapp.TimeOfDay
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import coil.compose.rememberAsyncImagePainter

@Composable
fun BrowseMedicineView(navController : NavController) {
    val context = LocalContext.current
    var medicines by remember { mutableStateOf(MedicineRepository.getMedicinesAlphabeticalAsc()) }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 50.dp)
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(top = 26.dp)
                .fillMaxHeight(0.98f)
        ) {
            items(medicines) { medicine ->
                MedicineItemCard(
                    medicine,
                    onDelete = {
                        MedicineRepository.removeMedicine(context = context, medicine = medicine)
                        medicines = MedicineRepository.getMedicinesAlphabeticalAsc()
                    }
                )
            }
        }
        Column(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(50.dp)
        ){
            var expanded by remember { mutableStateOf(false) }

            Box(

            ) {
                Column(horizontalAlignment = Alignment.End, verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    if (expanded) {
                        FloatingActionButton(
                            onClick = {
                                medicines = MedicineRepository.getMedicinesAlphabeticalAsc()
                                expanded = !expanded

                        }) {
                            Text("A→Z")
                        }
                        FloatingActionButton(
                            onClick = {
                                medicines = MedicineRepository.getMedicinesAlphabeticalDesc()
                                expanded = !expanded
                        }) {
                            Text("Z→A")
                        }
                    }
                    FloatingActionButton(
                        onClick = { expanded = !expanded },
                        modifier = Modifier.padding(bottom = 16.dp)
                    ) {
                        Icon(imageVector = Icons.Default.MoreVert, contentDescription = "Filter")
                    }
                }
            }

            FloatingActionButton(
                onClick = { navController.navigate("add_medicine") },
            ) {
                Icon(imageVector = Icons.Default.AddCircle, contentDescription = "Add Medicine")
            }
        }
    }
}

@Composable
fun MedicineItemCard(
    medicine: Medicine,
    modifier: Modifier = Modifier,
    onDelete: () -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Row(

        ){
            val painter = rememberAsyncImagePainter(medicine.imageUri)
            Image(
                painter = painter,
                contentDescription = "Selected Image",
                modifier = Modifier.size(100.dp)
            )
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = medicine.name,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = medicine.altName,
                    fontSize = 14.sp,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "You are supposed to take " + medicine.howManyToTake.toString(),
                    fontSize = 14.sp,
                    color = Color.Blue
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = medicine.description,
                    fontSize = 16.sp,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFE0E0E0))
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row {
                        medicine.whenToTake.forEach { time ->
                            Text(
                                text = time.name,
                                modifier = Modifier
                                    .padding(end = 8.dp)
                                    .background(Color.White, RoundedCornerShape(4.dp))
                                    .padding(horizontal = 6.dp, vertical = 2.dp),
                                fontSize = 12.sp
                            )
                        }
                    }
                    val context = LocalContext.current
                    IconButton(
                        onClick = {
                            onDelete()
                        },
                        modifier = Modifier.align(Alignment.Top)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete Medicine",
                            tint = Color.Red
                        )
                    }
                }
            }
        }
    }
}