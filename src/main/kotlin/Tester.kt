import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

const val location = "Geraldton"
fun main() {

    load()
    load2()

}

fun load2() = runBlocking {
    val starttime = System.currentTimeMillis()
    val retrofit = Retrofit.Builder()
        .baseUrl("http://api.apixu.com/v1/")
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .build()

    val service = retrofit.create(Tester2::class.java)
    val job = launch(Dispatchers.Default) {
        val result = service.getAllData(location).await()
        if (result.isSuccessful) {
            var response = result.body()!!
            println(response)
            println(System.currentTimeMillis()-starttime)
        }
    }
    job.join()
}

interface Tester2 {
    @GET("current.json?key=e4e4107b657a4a959f8133513190807")
    fun getAllData(@Query("q") location: String): Deferred<Response<Weather>>
}


private fun load() {

    val starttime = System.currentTimeMillis()

    val retrofit = Retrofit.Builder()
        .baseUrl("http://api.apixu.com/v1/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val service = retrofit.create(Tester::class.java)
    val weather = service.getAllData(location).execute().body()
    println(weather)
    println(System.currentTimeMillis() - starttime)
}


//http://api.apixu.com/v1/current.json?key=e4e4107b657a4a959f8133513190807&q=Perth

interface Tester {
    @GET("current.json?key=e4e4107b657a4a959f8133513190807")
    fun getAllData(@Query("q") location: String): Call<Weather>
}


data class Weather(
    val current: Current,
    val location: Location
)

data class Current(
    val cloud: Int,
    val condition: Condition,
    val feelslike_c: Double,
    val feelslike_f: Double,
    val gust_kph: Double,
    val gust_mph: Double,
    val humidity: Int,
    val is_day: Int,
    val last_updated: String,
    val last_updated_epoch: Int,
    val precip_in: Double,
    val precip_mm: Double,
    val pressure_in: Double,
    val pressure_mb: Double,
    val temp_c: Double,
    val temp_f: Double,
    val uv: Double,
    val vis_km: Double,
    val vis_miles: Double,
    val wind_degree: Int,
    val wind_dir: String,
    val wind_kph: Double,
    val wind_mph: Double
)

data class Condition(
    val code: Int,
    val icon: String,
    val text: String
)

data class Location(
    val country: String,
    val lat: Double,
    val localtime: String,
    val localtime_epoch: Int,
    val lon: Double,
    val name: String,
    val region: String,
    val tz_id: String
)


