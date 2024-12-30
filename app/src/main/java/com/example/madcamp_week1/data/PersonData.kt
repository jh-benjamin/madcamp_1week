package com.example.madcamp_week1.data

import android.content.Context
import com.example.madcamp_week1.R
import com.example.madcamp_week1.model.Person
import org.json.JSONArray


object PersonData{
    val personListFile = mutableListOf<Person>()

    fun initializeData(context: Context) {
        val jsonData = parseJSONToPersonList(context, "assemblyData.json")
        personListFile.clear()
        personListFile.addAll(jsonData)
    }

    fun loadJSONFromAsset(context: Context, fileName: String): String? {
        return try {
            val inputStream = context.assets.open(fileName)
            val size = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()
            String(buffer, Charsets.UTF_8)
        } catch (ex: Exception) {
            ex.printStackTrace()
            null
        }
    }

    fun parseJSONToPersonList(context: Context, fileName: String): List<Person> {
        val personList = mutableListOf<Person>()
        val jsonString = loadJSONFromAsset(context, fileName) ?: return emptyList()

        try {
            val jsonArray = JSONArray(jsonString)
            for (i in 0 until jsonArray.length()) {
                val jsonObject = jsonArray.getJSONObject(i)
                val person = Person(
                    // image = context.resources.getIdentifier(jsonObject.getString("image"), "drawable", context.packageName),
                    name = jsonObject.getString("국회의원명"),
                    party = jsonObject.getString("정당명"),
                    birth = jsonObject.getString("생일일자"),
                    tel = jsonObject.getString("전화번호"),
                    office = jsonObject.getString("사무실 호실"),
                    email = jsonObject.getString("국회의원이메일주소"),
                    img = jsonObject.getString("국회의원사진")
                )
                personList.add(person)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return personList
    }

}