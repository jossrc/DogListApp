package com.example.doglist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.doglist.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// Al estar trabajando con un buscador es necesario agregar un evento para que funcione
// Se importa el SearchView de androidx
class MainActivity : AppCompatActivity(), SearchView.OnQueryTextListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: DogAdapter
    private val dogImages = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.svDogs.setOnQueryTextListener(this)
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
        // Usamos Coroutines para trabajar de manera asíncrona para aplicar nuestro APIService
        CoroutineScope(Dispatchers.IO).launch {
            val call = getRetrofit().create(APIService::class.java).getDogsByBreeds("$query/images")
            val puppies: DogsResponse? = call.body()

            // Tod0 lo que esté entre esas llaves se hará en el hilo principal aunque esté dentro de una corrutina
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
                // esconder el teclado después de buscar
                hideKeyboard()
            }
        }
    }

    private fun showError() {
        Toast.makeText(this, "Ha ocurrido un error", Toast.LENGTH_SHORT).show()
    }

    /**
     * Esconde el teclado cuando se presiona el botón de buscar
     */
    private fun hideKeyboard() {
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.viewRoot.windowToken, 0)
    }

    /**
     * Permite detectar cuando el usuario presiona Enter o habilita
     * la opción de buscar
     */
    override fun onQueryTextSubmit(query: String?): Boolean {
        if (!query.isNullOrEmpty()) {
            searchByName(query.lowercase())
        }
        return true
    }

    /**
     * Permite detectar cuando el texto dentro de un buscador
     * cambia, la importación es obligatoria a pesar que no lo
     * necesitemos
     */
    override fun onQueryTextChange(newText: String?): Boolean {
        return true
    }

}