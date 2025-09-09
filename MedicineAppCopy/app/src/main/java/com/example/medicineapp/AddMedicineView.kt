package com.example.medicineapp


import android.content.Context
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter

@Composable
fun AddMedicineView(navController: NavController, context : Context) {
    var name by remember { mutableStateOf("") }
    var altName by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var howMany by remember { mutableStateOf(1) }
    var selectedTimes = remember { mutableStateListOf(TimeOfDay.MORNING) }
    var showWarning by remember { mutableStateOf(false) }
    var imageUri by remember { mutableStateOf<String?>(null) }

    Column(modifier = Modifier.padding(16.dp)) {
        // Horní panel
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            // Zpět
            Button(onClick = { navController.popBackStack() }) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Filter")
            }
            Spacer(modifier = Modifier.width(16.dp))
            // Nadpis
            Text(text = "Add New Medicine", style = MaterialTheme.typography.titleMedium)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Název input
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Name") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Alternativní název input
        OutlinedTextField(
            value = altName,
            onValueChange = { altName = it },
            label = { Text("Alternative Name") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Popis input
        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Description") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Počet input
        NumberSelector(
            value = howMany,
            onValueChange = { howMany = it },
            modifier = Modifier.fillMaxWidth(),
            min = 1
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Čas input
        Text(text = "When to Take:")
        Row {
            TimeOfDay.values().forEach { time ->
                val isSelected = time in selectedTimes
                Row(
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .background(if (isSelected) Color.Blue.copy(alpha = 0.2f) else Color.Transparent, RoundedCornerShape(4.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                        .clickable {
                            if (isSelected) selectedTimes.remove(time) else selectedTimes.add(time)
                        },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = time.name, color = if (isSelected) Color.Blue else Color.Black)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Obrázek input
        ImagePicker(onImageSelected = { uri ->
            imageUri = uri
        })

        imageUri?.let { uri ->
            // show preview
            val painter = rememberAsyncImagePainter(uri)
            Image(
                painter = painter,
                contentDescription = "Selected image",
                modifier = Modifier
                    .size(100.dp)
                    .padding(top = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Varování pokud nejsou vyplněná povinná pole
        if (showWarning) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Please fill in all fields!",
                color = Color.Red,
                fontSize = 14.sp
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Submit
        Button(
            onClick = {
                if (name.isNotBlank() && altName.isNotBlank() && description.isNotBlank() && selectedTimes.isNotEmpty()) {
                    MedicineRepository.addMedicine(
                        context = context,
                        medicine = Medicine(
                            name = name,
                            altName = altName,
                            description = description,
                            howManyToTake = howMany,
                            whenToTake = selectedTimes.toList(),
                            imageUri = imageUri
                        )
                    )
                    navController.popBackStack() // go back to BrowseMedicine
                } else {
                    showWarning = true
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Save Medicine")
        }
    }
}

@Composable
fun NumberSelector(
    value: Int,
    onValueChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
    min: Int = 0,
    max: Int = 100
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .background(Color(0xFFE0E0E0), RoundedCornerShape(8.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Button(
            onClick = { if (value > min) onValueChange(value - 1) },
            contentPadding = PaddingValues(0.dp),
            modifier = Modifier.size(32.dp)
        ) {
            Text("-", fontSize = 20.sp)
        }

        Spacer(modifier = Modifier.width(16.dp))

        Text(text = value.toString(), fontSize = 18.sp, modifier = Modifier.width(40.dp), textAlign = TextAlign.Center)

        Spacer(modifier = Modifier.width(16.dp))

        Button(
            onClick = { if (value < max) onValueChange(value + 1) },
            contentPadding = PaddingValues(0.dp),
            modifier = Modifier.size(32.dp)
        ) {
            Text("+", fontSize = 20.sp)
        }
    }
}
@Composable
fun ImagePicker(
    onImageSelected: (String) -> Unit
) {
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            uri?.let {
                onImageSelected(it.toString()) // save URI string
            }
        }
    )

    Button(onClick = { launcher.launch("image/*") }) {
        Text("Select Image")
    }
}
