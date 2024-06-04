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
import java.util.Timer
import kotlin.concurrent.schedule

//val filePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).absolutePath + "/Cities.json"

data class City(val name: String, val latitude: Double, val longitude: Double)

@Composable
fun CitySelectionScreen(navController: NavController, viewModel: WeatherViewModel) {
    var text by remember { mutableStateOf("") }
    var long by remember { mutableStateOf("") }
    var lat by remember { mutableStateOf("") }
    var itemList by remember { mutableStateOf(listOf<String>()) }
    val timer = Timer()
    var btntxt = "Add city"

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
            label = { Text("Enter city") },
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.LightGray)
        )
        OutlinedTextField(
            value = lat,
            onValueChange = {
                if(it.length <= 6){
                    lat = it
                }
            },
            label = { Text("Enter Latitude") },
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.LightGray)
        )
        OutlinedTextField(
            value = long,
            onValueChange = {
                if(it.length <= 6){
                    long = it
                }
                            },
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
                    try {
                        if (text.isNotBlank() || lat.isNotBlank() || long.isNotBlank()) {
                            if(lat.toDouble() >= -90 && lat.toDouble()<=90 && long.toDouble() >= -180 && long.toDouble()<=180) {
                                itemList = itemList + text
                                //saveDataToJsonFile(City(text, lat.toDouble(), long.toDouble()), filePath)
                                text = ""
                            }else{
                                timer.schedule(0, 1000){
                                    btntxt = "Enter the correct value"
                                }
                                btntxt = "Add city"
                            }
                        }else{
                            timer.schedule(0, 1000) {
                                btntxt = "Fill all fields"
                            }
                            btntxt = "Add city"
                        }
                    }catch (exeption: Exception){
                        text = exeption.toString()
                    }
                }
            ) {
                Text(btntxt)
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
                        .weight(1f)
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .background(Color.Gray)
                        .clickable {
                            //cities = getDataFromJsonFile(filePath)
                            //var data = cities.filter { it.name == item }
                            //data.forEach { city ->
                            viewModel.loadWeatherInfo(lat.toDouble(), long.toDouble())
                            navController.navigate("weather_screen")
                            //}
                        },
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = item,
                        modifier = Modifier
                            .weight(1f)
                            .align(Alignment.CenterVertically)
                            .padding(start = 6.dp)
                    )
                    Icon(imageVector = Icons.Default.Delete,
                        contentDescription = "Delete",
                        modifier = Modifier
                            .clickable {
                                //removeDatafromJsonFile(City(text, lat.toDouble(), long.toDouble()), filePath)
                                itemList = itemList.filter { it != item }
                            }
                            .padding(8.dp)
                    )
                }
            }
        }
    }
}
//fun saveDataToJsonFile(data: City, filePath: String) {
//    val jsonArray = JSONArray()
//    val jsonObj = JSONObject()
//    jsonObj.put("name", data.name)
//    jsonObj.put("lat", data.latitude)
//    jsonObj.put("long", data.longitude)
//    jsonArray.put(jsonObj)
//    val jsonString = jsonArray.toString()
//    val file = File(filePath)
//    file.writeText(jsonString)
//}
//fun getDataFromJsonFile(filePath: String): MutableList<City> {
//    val file = File(filePath)
//    val jsonText = file.readText()
//    val jsonArray = JSONArray(jsonText)
//    val cities = mutableListOf<City>()
//
//    for (i in 0 until jsonArray.length()) {
//        val jsonObj = jsonArray.getJSONObject(i)
//        val text = jsonObj.getString("name")
//        val lat = jsonObj.getDouble("lat")
//        val long = jsonObj.getDouble("long")
//        cities.add(City(text, lat, long))
//    }
//    return cities
//}
//fun removeDatafromJsonFile(dataToRemove: City, filePath: String) {
//    val jsonString = File(filePath).readText()
//    val jsonArray = JSONArray(jsonString)
//
//    // Remove the data to be deleted
//    val updatedArray = JSONArray()
//    for (i in 0 until jsonArray.length()) {
//        val jsonObject = jsonArray.getJSONObject(i)
//        val city = City(
//            jsonObject.getString("name"),
//            jsonObject.getDouble("lat"),
//            jsonObject.getDouble("long")
//        )
//        if (city != dataToRemove) {
//            updatedArray.put(jsonObject)
//        }
//    }
//    File(filePath).writeText(updatedArray.toString())
//}
