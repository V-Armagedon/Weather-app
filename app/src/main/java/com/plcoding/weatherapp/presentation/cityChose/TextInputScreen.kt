package com.plcoding.weatherapp.presentation.cityChose

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.plcoding.weatherapp.presentation.WeatherViewModel

data class City(val name: String, val latitude: Double, val longitude: Double)

@Composable
fun CitySelectionScreen(navController: NavController, viewModel: WeatherViewModel) {
    var text by remember { mutableStateOf("") }
    var long by remember { mutableStateOf("") }
    var lat by remember { mutableStateOf("") }
    var itemList by remember { mutableStateOf(listOf<String>()) }

    var cities = mutableListOf(
        City("Kyiv", 50.4501, 30.5234),
        City("Lviv", 49.8397, 24.0297),
        City("Odessa", 46.4825, 30.7233),
        City("Kharkiv", 49.9935, 36.2304)
    )
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Cyan)
    ) {
        OutlinedTextField(
            value = text,
            onValueChange = { text = it },
            label = { Text("Enter item") },
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.LightGray)
        )
        OutlinedTextField(
            value = lat,
            onValueChange = { lat = it },
            label = { Text("Enter Lattitude") },
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.LightGray)
        )
        OutlinedTextField(
            value = long,
            onValueChange = { long = it },
            label = { Text("Enter Longitude") },
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.LightGray)
        )
        Spacer(modifier = Modifier.height(8.dp))

        Row {
            Button(
                modifier = Modifier.weight(1f),
                onClick = {
                    if (text.isNotBlank()) {
                        if (text.isNotBlank()) {
                            cities.add(City(text, lat.toDouble(), long.toDouble()))
                            itemList = itemList + text
                            text = ""
                        }
                    }
                }
            ) {
                Text("Add Item")
            }
            Button(
                modifier = Modifier
                    .weight(1f),
                onClick = {
                    viewModel.loadWeatherInfo(null, null)
                    navController.navigate("weather_screen")
                }
            ) {
                Text("Current weather")
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(10.dp)
        ) {
            items(itemList) { item ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .background(Color.Blue)
                        .clickable {
                            //viewModel.loadWeatherInfo(city.latitude, city.longitude)
                            navController.navigate("weather_screen")
                        },
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = item, modifier = Modifier.weight(1f).align(Alignment.CenterVertically).padding(start = 6.dp))
                    Icon(imageVector = Icons.Default.Delete,
                        contentDescription = "Delete",
                        modifier = Modifier
                            .clickable {
                                itemList = itemList.filter { it != item }
                            }
                            .padding(8.dp)
                    )
                }
            }
        }
    }
}