package com.example.doglist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.doglist.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initRecyclerView()
    }

    /*
    * Iniciar el RecyclerView
    * */
    private fun initRecyclerView() {

    }

    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://dog.ceo/api/breed/") // Siempre debe terminar con una barra (baseUrl)
            .addConverterFactory(GsonConverterFactory.create()) // Convierte nuestra respuesta a Data Class
            .build();
    }

    private fun searchByName(query: String) {
        // Usamos Coroutines y aplicamos nuestro APIService
        CoroutineScope(Dispatchers.IO).launch {
            val call = getRetrofit().create(APIService::class.java).getDogsByBreeds("$query/images")
            val puppies: DogsResponse? = call.body()

            // Verificamos si la petición fue exitosa
            if (call.isSuccessful) {
                // Mostrar Recyclerview
            } else {
                // Mostrar error
            }

        }
    }

}