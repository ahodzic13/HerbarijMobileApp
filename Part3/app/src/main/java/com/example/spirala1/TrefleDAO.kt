package com.example.spirala1

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

class TrefleDAO {


    private val api_key:String = "gzkmCLq1YXtDVKPEBTC7LpkS6MiwoJ99FCKKGHLp7s4"

    private val defaultBitmap: Bitmap by lazy {
        BitmapFactory.decodeResource(Resources.getSystem(), R.drawable.picture1)
    }

    suspend fun getImage(biljka: Biljka): Bitmap {
        return withContext(Dispatchers.IO) {
            try {
                val latinName = if ("(" in biljka.naziv && ")" in biljka.naziv) {
                    biljka.naziv.substringAfterLast("(").substringBeforeLast(")")
                } else {
                    biljka.naziv
                }

                val url1 = "https://trefle.io/api/v1/species?filter[scientific_name]=$latinName&token=gzkmCLq1YXtDVKPEBTC7LpkS6MiwoJ99FCKKGHLp7s4"
                val url = URL(url1)

                (url.openConnection() as? HttpURLConnection)?.run {
                    requestMethod = "GET"
                    connect()

                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        inputStream.use { inputStream ->
                            val result = inputStream.bufferedReader().use { it.readText() }
                            val jsonObject = JSONObject(result)
                            val dataArray = jsonObject.getJSONArray("data")

                            if (dataArray.length() > 0) {
                                val plantObject = dataArray.getJSONObject(0)
                                val imageUrl = plantObject.getString("image_url")

                                val bitmapUrlConnection = URL(imageUrl).openConnection() as HttpURLConnection
                                bitmapUrlConnection.connect()
                                if (bitmapUrlConnection.responseCode == HttpURLConnection.HTTP_OK) {
                                    val bitmapInputStream: InputStream = bitmapUrlConnection.inputStream
                                    return@withContext BitmapFactory.decodeStream(bitmapInputStream)
                                }
                            }
                        }
                    }
                }
                // Ako ne uspijete dohvatiti sliku s weba, koristite defaultnu sliku
                defaultBitmap ?: throw Exception("Default bitmap not initialized")
            } catch (e: Exception) {
                e.printStackTrace()
                return@withContext defaultBitmap
            }
        }
    }

    suspend fun fixData(biljka: Biljka): Biljka = withContext(Dispatchers.IO) {
        try {
            val latinName = biljka.naziv?.let { extractLatinName(it) }
            val speciesDetails = getSpeciesDetails(latinName)
            speciesDetails?.let { details ->
                updateBiljkaWithDetails(biljka, details)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            biljka.porodica = "EXCEPTION"
        }
        biljka
    }



    private fun getSpeciesDetails(latinName: String?): JSONObject? {
        val baseurl = "https://trefle.io/api/v1/species/"
        val urlString = "$baseurl$latinName?token=$api_key"
        val url = URL(urlString)

        return (url.openConnection() as? HttpURLConnection)?.run {
            requestMethod = "GET"
            connect()

            if (responseCode == HttpURLConnection.HTTP_OK) {
                inputStream.use { inputStream ->
                    val result = inputStream.bufferedReader().use { it.readText() }
                    return JSONObject(result).optJSONObject("data")
                }
            }
            null
        }
    }

    private fun updateBiljkaWithDetails(biljka: Biljka, details: JSONObject) {
        val family = details.optString("family")
        val edible = details.optBoolean("edible")
        val toxicity = details.optJSONObject("specifications")?.optString("toxicity")
        val soilTexture = details.optInt("soil_textures")
        val light = details.optJSONObject("growth")?.optInt("light")
        val humidity = details.optJSONObject("growth")?.optInt("atmospheric_humidity")

        // Update family
        if (family.isNotEmpty() && family != biljka.porodica) biljka.porodica = family

        // Update edibility
        if (!edible) {
            biljka.jela = emptyList()
            if (!biljka.medicinskoUpozorenje.orEmpty().contains("NIJE JESTIVO")) {
                biljka.medicinskoUpozorenje += " NIJE JESTIVO"
            }
        }

        // Update toxicity
        if (toxicity != null && toxicity != "null" && toxicity != "none") {
            if (!biljka.medicinskoUpozorenje.orEmpty().contains("TOKSIČNO")) {
                biljka.medicinskoUpozorenje += " TOKSIČNO"
            }
        }

        // Update soil types
        biljka.zemljisniTipovi = getSoilTypes(soilTexture)

        // Update climate types
        biljka.klimatskiTipovi = getClimateTypes(light, humidity)
    }

    private fun getSoilTypes(soilTexture: Int): List<Zemljiste> {
        val validSoilTypes = mutableListOf<Zemljiste>()

        when (soilTexture) {
            9 -> validSoilTypes.add(Zemljiste.SLJUNKOVITO)
            10 -> validSoilTypes.add(Zemljiste.KRECNJACKO)
            1, 2 -> validSoilTypes.add(Zemljiste.GLINENO)
            3, 4 -> validSoilTypes.add(Zemljiste.PJESKOVITO)
            5, 6 -> validSoilTypes.add(Zemljiste.ILOVACA)
            7, 8 -> validSoilTypes.add(Zemljiste.CRNICA)
        }

        return validSoilTypes
    }

    private fun getClimateTypes(light: Int?, humidity: Int?): List<KlimatskiTip> {
        val validClimateTypes = mutableListOf<KlimatskiTip>()

        if (light in 6..9 && humidity in 1..5) {
            validClimateTypes.add(KlimatskiTip.SREDOZEMNA)
        }

        if (light in 8..10 && humidity in 7..10) {
            validClimateTypes.add(KlimatskiTip.TROPSKA)
        }

        if (light in 6..9 && humidity in 5..8) {
            validClimateTypes.add(KlimatskiTip.SUBTROPSKA)
        }

        if (light in 4..7 && humidity in 3..7) {
            validClimateTypes.add(KlimatskiTip.UMJERENA)
        }

        if (light in 7..9 && humidity in 1..2) {
            validClimateTypes.add(KlimatskiTip.SUHA)
        }

        if (light in 0..5 && humidity in 3..7) {
            validClimateTypes.add(KlimatskiTip.PLANINSKA)
        }

        return validClimateTypes
    }


    suspend fun getPlantsWithFlowerColor(
        flowerColor: String,
        substr: String
    ): List<Biljka> {
        return withContext(Dispatchers.IO) {
            val plantsList = mutableListOf<Biljka>()
            try {
                // Prvo tražimo biljke prema substringu
                val searchUrl = URL("https://trefle.io/api/v1/plants/search?token=gzkmCLq1YXtDVKPEBTC7LpkS6MiwoJ99FCKKGHLp7s4&q=$substr&filter_not[flower_color]=null")
                (searchUrl.openConnection() as? HttpURLConnection)?.run {
                    val searchResult = inputStream.bufferedReader().use { it.readText() }
                    val searchJo = JSONObject(searchResult)
                    val searchResults = searchJo.getJSONArray("data")

                    for (i in 0 until searchResults.length()) {
                        val plantJson = searchResults.getJSONObject(i)
                        val naziv = plantJson.getString("scientific_name")
                        val porodica = plantJson.getString("family")
                        val plantId = plantJson.getString("id")

                        // Sada provjeravamo boju cvijeta za svaku biljku
                        val speciesUrl = URL("https://trefle.io/api/v1/species/$plantId?token=gzkmCLq1YXtDVKPEBTC7LpkS6MiwoJ99FCKKGHLp7s4")
                        (speciesUrl.openConnection() as? HttpURLConnection)?.run {
                            val speciesResult = inputStream.bufferedReader().use { it.readText() }
                            val details = JSONObject(speciesResult).optJSONObject("data")
                            val flowerColors = details?.optJSONObject("flower")?.optJSONArray("color")

                            // Provjeravamo da li boja cvijeta odgovara zadanoj boji
                            if (flowerColors != null) {
                                for (j in 0 until flowerColors.length()) {
                                    if (flowerColors.getString(j).equals(flowerColor, ignoreCase = true)) {
                                        plantsList.add(
                                            fixData(
                                                Biljka(
                                                    naziv,
                                                    porodica,
                                                    null,
                                                    null,
                                                    null,
                                                    emptyList(),
                                                    emptyList(),
                                                    emptyList()
                                                )
                                            )
                                        )
                                        break
                                    }
                                }
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                // Ako se dogodi bilo koja greška, nema podataka za vratiti
                // Možemo zabilježiti grešku ili jednostavno vratiti praznu listu
                e.printStackTrace()
            }
            plantsList
        }
    }

    private fun extractLatinName(fullName: String): String {
        val regex = Regex("\\(([^)]+)\\)")
        val matchResult = regex.find(fullName)
        val latinName = matchResult?.groups?.get(1)?.value ?: fullName
        return latinName.toLowerCase().replace(" ", "-")
    }

}
