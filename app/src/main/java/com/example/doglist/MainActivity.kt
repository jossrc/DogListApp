package com.example.doglist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.doglist.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: DogAdapter
    private val dogImages = mutableListOf<String>()

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
        adapter = DogAdapter(dogImages)
        binding.rvDogs.layoutManager = LinearLayoutManager(this)
        binding.rvDogs.adapter = adapter
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

            // Se debe trabajar en otro hilo (este método es recomendable para manejos con la UI )
            runOnUiThread {
                // Verificamos si la petición fue exitosa
                if (call.isSuccessful) {
                    // Si es null que me retorne una lista vacía
                    val images = puppies?.images ?: emptyList()
                    // Se limpia la lista
                    dogImages.clear()
                    // Mostrar Recyclerview
                    dogImages.addAll(images)
                    // Se notifica al adapter del cambio
                    adapter.notifyDataSetChanged()
                } else {
                    // Mostrar error
                    showError()
                }
            }
        }
    }

    private fun showError() {
        Toast.makeText(this, "Ha ocurrido un error", Toast.LENGTH_SHORT).show()
    }

}