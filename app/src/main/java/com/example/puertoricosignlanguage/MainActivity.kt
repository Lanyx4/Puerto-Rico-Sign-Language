package com.example.puertoricosignlanguage

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Resources

import android.os.Bundle
import android.speech.RecognizerIntent
import android.util.TypedValue
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.bumptech.glide.Glide
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView

import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var searchEditText: AutoCompleteTextView
    private lateinit var searchButton: Button
    private lateinit var gifImageView: ImageView
    private lateinit var searchTermTextView: TextView
    private lateinit var voiceButton: FloatingActionButton

    // Melanie: Variables para el menú lateral
    private lateinit var drawerLayout: DrawerLayout
    private val availableGifs = mutableMapOf<String, Int>()


    //Adrian
    //This is where the library of fixed words is located.
    //It is read-only Map where all of our keys and values are strings.
    private val spellingDictionary = mapOf(
        "adios" to "adiós",
        "alegria" to "alegría",
        "ayudame" to "ayúdame",
        "bano" to "baño",
        "bayamon" to "bayamón",
        "bebe" to "bebé",
        "cafe" to "café",
        "catano" to "cataño",
        "como" to "cómo",
        "cooperacion" to "cooperación",
        "cual" to "cuál",
        "cuando" to "cuándo",
        "cuanto" to "cuánto",
        "cunada" to "cuñada",
        "cunado" to "cuñado",
        "dejalo" to "déjalo",
        "dia" to "día",
        "discusion" to "discusión",
        "donde" to "dónde",
        "el" to "él",
        "ensenar" to "enseñar",
        "fotografo" to "fotógrafo",
        "frio" to "frío",
        "frustracion" to "frustración",
        "jabon" to "jabón",
        "limon" to "limón",
        "mama" to "mamá",
        "manana" to "mañana",
        "mayaguez" to "mayagüez",
        "mecanico" to "mecánico",
        "miercoles" to "miércoles",
        "papa" to "papá",
        "pelicula" to "película",
        "perdon" to "perdón",
        "periodico" to "periódico",
        "platano" to "plátano",
        "policia" to "policía",
        "presion" to "presión",
        "que" to "qué",
        "quien" to "quién",
        "rincon" to "rincón",
        "sabado" to "sábado",
        "senas" to "señas",
        "si" to "sí",
        "sonar" to "soñar",
        "telefono" to "teléfono",
        "television" to "televisión",
        "tia" to "tía",
        "tio" to "tío",
        "tu" to "tú",
    )

    private fun AutoCompleteTextView.hideKeyboard() {
        val imm =
            context.getSystemService(INPUT_METHOD_SERVICE) as android.view.inputmethod.InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }

    // Angel y Juan Jimenez
    private val speechRecognitionLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK && result.data != null) {
            val data = result.data
            val results = data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            results?.get(0)?.let { spokenText ->
                searchEditText.setText(spokenText)
                performSearch()
            }
        }
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            startSpeechRecognition()
        } else {
            Toast.makeText(
                this,
                "Permiso de micrófono necesario para esta función",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    // Christian
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize views
        searchEditText = findViewById(R.id.searchEditText)
        searchButton = findViewById(R.id.searchButton)
        gifImageView = findViewById(R.id.gifImageView)
        searchTermTextView = findViewById(R.id.searchTermTextView)
        voiceButton = findViewById(R.id.voiceButton)

        // Melanie: Inicializar menú lateral
        drawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val btnOpenDrawer: ImageButton = findViewById(R.id.btn_open_drawer)

        navView.setCheckedItem(R.id.nav_home)

        // Abrirá el menú al tocar el botón
        btnOpenDrawer.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }

        // Configurar los clics de las opciones del menú
        navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_home -> {
                    val intent = Intent(this, MainActivity::class.java)
                }

                // "Botón" de las opciones del menú que te lleva a la activity About Us
                R.id.nav_about_us -> {
                    val intent = Intent(this, AboutUsActivity::class.java)
                    startActivity(intent)
                }

                // Menú del diccionario
                R.id.nav_dictionary -> {
                    val intent = Intent(this, DictionaryActivity::class.java)
                    startActivity(intent)
                }
            }
            drawerLayout.closeDrawers()
            true
        }
        // Melanie: Termina el programa de menú

        // Load all available GIFs from drawable folder
        loadAvailableGifs()

        // Setup autocomplete with available GIF names
        setupAutocomplete()

        // Set up search functionality through keyboard action
        searchEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                performSearch()
                searchEditText.hideKeyboard()
                return@setOnEditorActionListener true
            }
            false
        }

        // Set up search functionality through button click
        searchButton.setOnClickListener {
            performSearch()
            searchEditText.hideKeyboard()
        }

        // Set up voice search
        voiceButton.setOnClickListener {
            checkMicrophonePermission()
        }

        // Perform search when a suggestion is clicked
        searchEditText.setOnItemClickListener { _, _, _, _ ->
            performSearch()
            searchEditText.hideKeyboard()
        }
    }

    private fun loadAvailableGifs() {
        try {
            val drawableFields = R.drawable::class.java.fields

            for (field in drawableFields) {
                val resourceName = field.name
                try {
                    val resourceId = field.getInt(null) //

                    if (isLikelyGif(resources, resourceId)) {
                        val displayName = denormalizeString(resourceName)
                        availableGifs[displayName] = resourceId
                    }
                } catch (e: IllegalAccessException) {
                    System.err.println("Error accessing resource ID for ${field.name}: ${e.message}")
                } catch (e: Resources.NotFoundException) {
                    System.err.println("Resource not found for ID obtained from ${field.name}: ${e.message}")
                }
            }
        } catch (e: Exception) {
            System.err.println("Failed to load available GIFs: ${e.message}")
            e.printStackTrace()
        }
    }

    private fun isLikelyGif(res: Resources, resourceId: Int): Boolean {
        try {
            val value = TypedValue()
            res.getValue(resourceId, value, true) // true to resolve references

            // The 'value.string' field will hold the path to the resource file,
            // e.g., "res/drawable-mdpi/my_animation.gif"
            val resourcePath = value.string?.toString()

            return resourcePath != null && resourcePath.endsWith(".gif", ignoreCase = true)
        } catch (e: Resources.NotFoundException) {
            // This can happen if the ID is not a valid file-based resource
            return false
        } catch (e: Exception) {
            System.err.println("Error getting resource value for ID $resourceId: ${e.message}")
            return false
        }
    }

    private fun setupAutocomplete() {
        // Get all display names for autocomplete suggestions
        val suggestions = availableGifs.keys.toList().sorted()

        // Create a custom adapter for autocomplete with limited suggestions
        val adapter = object : ArrayAdapter<String>(
            this,
            android.R.layout.simple_dropdown_item_1line,
            suggestions
        ) {
            override fun getCount(): Int {
                // Limit the number of suggestions shown at a time
                return minOf(5, super.getCount())
            }
        }

        searchEditText.setAdapter(adapter)
        searchEditText.threshold = 1  // Show suggestions after typing 1 character
    }

    // Angel y Juan Jimenez
    private fun checkMicrophonePermission() {
        when {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.RECORD_AUDIO
            ) == PackageManager.PERMISSION_GRANTED -> {
                startSpeechRecognition()
            }

            ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.RECORD_AUDIO
            ) -> {
                Toast.makeText(
                    this,
                    "Se necesita permiso para usar el micrófono",
                    Toast.LENGTH_SHORT
                ).show()
                requestPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
            }

            else -> {
                requestPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
            }
        }
    }

    // Angel y Juan Jimenez
    private fun startSpeechRecognition() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
            )
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, "es-ES")
            putExtra(RecognizerIntent.EXTRA_PROMPT, "Habla para buscar una seña")
        }

        try {
            speechRecognitionLauncher.launch(intent)
        } catch (e: Exception) {
            Toast.makeText(
                this,
                "El reconocimiento de voz no está disponible en este dispositivo",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    // Juan Colon y Victor
    private fun performSearch() {
        val searchTerm = searchEditText.text.toString().trim()

        if (searchTerm.isEmpty()) {
            Toast.makeText(this, "Por favor, introduce un término de búsqueda", Toast.LENGTH_SHORT)
                .show()
            return
        }

        // First try exact match with entered text
        var resourceId = availableGifs[searchTerm]

        // If not found, try with capitalized version
        if (resourceId == null) {
            resourceId = availableGifs[capitalize(searchTerm)]
        }

        // If still not found, try with normalized version
        if (resourceId == null) {
            val normalizedTerm = normalizeString(searchTerm)
            // Get the denormalized version that might be in our map
            val possibleMatches = availableGifs.keys.filter {
                normalizeString(it) == normalizedTerm
            }

            if (possibleMatches.isNotEmpty()) {
                resourceId = availableGifs[possibleMatches.first()]
            }
        }

        if (resourceId != null) {
            // Update the search term display (keeping original input for display)
            searchTermTextView.apply {
                text = capitalize(searchTerm)
                visibility = View.VISIBLE
            }

            // If resource is found, display it using Glide to handle GIF animation
            Glide.with(this)
                .asGif()
                .load(resourceId)
                .into(gifImageView)

            searchEditText.text.clear()
        } else {
            searchTermTextView.visibility = View.GONE
            // If resource is not found, show an error message
            Toast.makeText(
                this,
                "No se encontró una seña con la palabra: $searchTerm",
                Toast.LENGTH_SHORT
            ).show()
            // Clear the current image
            gifImageView.setImageDrawable(null)
        }
    }

    // Juan Colon y Victor
    private fun normalizeString(str: String?): String {
        var word = str ?: return ""

        // Replace special characters with their standard versions (both upper and lower case)
        word = word
            .replace("á", "a").replace("Á", "a")
            .replace("é", "e").replace("É", "e")
            .replace("í", "i").replace("Í", "i")
            .replace("ó", "o").replace("Ó", "o")
            .replace("ú", "u").replace("Ú", "u")
            .replace("ñ", "n").replace("Ñ", "n")
            .replace("ü", "u").replace("Ü", "u")
            .replace(" ", "_") // replace spaces with underscores
            .replace("[^a-zA-Z0-9_]".toRegex(), "") // Remove any other special characters
            .lowercase() // convert to lowercase after handling special characters

        return word
    }

    //Adrian
    // New method to convert normalized names back to display names
    private fun denormalizeString(str: String?): String {
        //Checks if the input string is null.
        //If it is null then it returns an empty string, otherwise it is assigned the variable word.
        var word = str ?: return ""
        //Replaces every instance of an underscore "_" with a space " "
        word = word.replace("_", " ")

        //Splits the word string into a list of individual strings separated by a space.
        val wordList = word.split(" ")
        //Fixed Words are stored in a mutable list.
        val fixedWords = mutableListOf<String>()

        //Loop through every item in wordList, with each item being temporarily assigned to currentWord.
        for (currentWord in wordList) {
            //Checks if the current word is inside the map. If it is found, then that value is assigned.
            // Otherwise, just keep the original word. The processed word is added to the fixedWords list.
            val correctedWord = spellingDictionary[currentWord] ?: currentWord
            fixedWords.add(correctedWord)
        }

        //Takes the fixedWords list and combines it back into a single string, with each word separated by a space.
        val result = fixedWords.joinToString(" ")
        //Then finalResult is assigned the result.
        val finalResult = result
            //Due to the logic of the list, phrases are directly replaced.
            .replace("a donde vas", "a dónde vas")
            .replace("a que hora", "a qué hora")
            .replace("buenos dias", "buenos días")
            .replace("como estas", "cómo estás")
            .replace("como te llamas", "cómo te llamas")
            .replace("correo electronico", "correo electrónico")
            .replace("esta bien", "está bien")
            .replace("me olvide", "me olvidé")
            .replace("no se", "no sé")
            .replace("por que","por qué")
            .replace("que paso", "qué paso")

        //Capitalizes the final results of strings. Otherwise, leave as is.
        return capitalize(finalResult) ?: finalResult

    }

    // Juan Colon y Victor
    private fun capitalize(str: String?): String? {
        if (str.isNullOrEmpty()) return str

        // First check if the string contains "usc" (case insensitive)
        if (str.lowercase(Locale.getDefault()).contains("usc")) {
            return str.uppercase(Locale.getDefault())
        }

        // If not USC, proceed with normal capitalization
        val words = str.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val capitalized = StringBuilder()
        for (word in words) {
            capitalized.append(word.substring(0, 1).uppercase(Locale.getDefault()))
                .append(word.substring(1)).append(" ")
        }
        return capitalized.toString().trim()
    }
}