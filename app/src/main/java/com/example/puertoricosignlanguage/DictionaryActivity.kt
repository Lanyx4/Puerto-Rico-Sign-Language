package com.example.puertoricosignlanguage
//Nova
import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.SectionIndexer
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.navigation.NavigationView
import android.widget.Toast
import pl.droidsonroids.gif.GifDrawable
import pl.droidsonroids.gif.GifImageView

class DictionaryActivity : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navView: NavigationView
    private lateinit var menuButton: ImageButton
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dictionary)

        drawerLayout = findViewById(R.id.drawer_layout)
        navView = findViewById(R.id.nav_view)
        menuButton = findViewById(R.id.btn_open_drawer)
        recyclerView = findViewById(R.id.dictionaryRecyclerView)

        setupDrawer()
        setupRecyclerView()
        loadAvailableGifs()
    }

    // ---------------- Drawer holds container, navigation view and menu button ----------------
    private fun setupDrawer() {
        menuButton.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }

        navView.setNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home ->
                    startActivity(Intent(this, MainActivity::class.java))

                R.id.nav_about_us ->
                    startActivity(Intent(this, AboutUsActivity::class.java))

                R.id.nav_dictionary -> {
                    drawerLayout.closeDrawer(GravityCompat.START)
                    return@setNavigationItemSelectedListener true
                }
            }
            drawerLayout.closeDrawer(GravityCompat.START)
            true
        }
    }

    // ---------------- RecyclerView: muestra las palabras en el app  ----------------
    private fun setupRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(this)

        val normalizedWords = listOf(
            "a_que_hora",
            "abogado",
            "abuela",
            "abuelo",
            "agua",
            "alegria",
            "amor",
            "arroz",
            "ayudame",
            "ayudar",
            "bayamon",
            "bebe",
            "bombero",
            "cafe",
            "caguas",
            "cambiar",
            "cantante",
            "carne",
            "carolina",
            "carta",
            "catano",
            "cayey",
            "cocinar",
            "cocinero",
            "comer",
            "como",
            "comparar",
            "compartir",
            "conducir",
            "conflicto",
            "conversar",
            "cooperarcion",
            "correo_electronico",
            "correr",
            "creer",
            "cuando",
            "cunada",
            "cunado",
            "decidir",
            "decir",
            "decirme",
            "defender",
            "dejalo",
            "descansar",
            "discusion",
            "doctor",
            "dolor",
            "donde",
            "dorado",
            "dormir",
            "el",
            "ella",
            "ellas",
            "ellos",
            "ensenar",
            "escribir",
            "esperar",
            "explicar",
            "fajardo",
            "familia",
            "fotografo",
            "frustracion",
            "fruta",
            "gracias",
            "guineo",
            "habichuela",
            "hablar",
            "hermana",
            "hervir",
            "hija",
            "hijo",
            "huevo",
            "idea",
            "individual",
            "influenciar",
            "interpretar",
            "interprete",
            "jugo",
            "junto",
            "leche",
            "leer",
            "limon",
            "madrastra",
            "maestro",
            "mama",
            "mayaguez",
            "me_olvide",
            "mecanico",
            "mirar",
            "no",
            "no_entender",
            "nombre",
            "nose",
            "nosotros",
            "odio",
            "ok",
            "papa",
            "pelicula",
            "peluquero",
            "pensar",
            "perder",
            "perdon",
            "periodico",
            "persona",
            "pescado",
            "pizza",
            "platano",
            "policia",
            "ponce",
            "por_favor",
            "por_que",
            "precio",
            "presion",
            "prima",
            "primo",
            "puerto_rico",
            "que",
            "quien",
            "recordar",
            "rincon",
            "saber",
            "san_juan",
            "senas",
            "si",
            "significados",
            "sobrina",
            "sobrino",
            "su",
            "suegra",
            "suegro",
            "telefono",
            "television",
            "tia",
            "tio",
            "tostones",
            "tristeza",
            "tu",
            "usc",
            "vieques",
            "yo"
        )



        val displayWords = normalizedWords
            .map { denormalizeString(it) }
            .sorted()

        recyclerView.adapter = DictionaryAdapter(displayWords)
    }
    private val availableGifs = mutableMapOf<String, Int>()
    private fun loadAvailableGifs() {
        try {
            val drawableFields = R.drawable::class.java.fields

            for (field in drawableFields) {
                val resourceName = field.name
                try {
                    val resourceId = field.getInt(null)

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

    // ---------------- Adapter:It tells RecyclerView how many items exist, creates row views, fills each row with data  ----------------
    inner class DictionaryAdapter(
        private val items: List<String>
    ) : RecyclerView.Adapter<DictionaryAdapter.ViewHolder>(), SectionIndexer {

        private val sections: List<Char> =
            items.map { it.firstOrNull()?.uppercaseChar() ?: '#' }.distinct()

        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val text: TextView = view.findViewById(android.R.id.text1)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(android.R.layout.simple_list_item_1, parent, false)
            return ViewHolder(view)
        }

        // onBindViewHolder---- shows gif on click
        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val word = items[position]
            holder.text.text = word

            holder.itemView.setOnClickListener {
                showGifDialog(word)
            }
        }

        override fun getItemCount(): Int = items.size

        override fun getSections(): Array<Any> =
            sections.map { it.toString() }.toTypedArray()

        override fun getPositionForSection(sectionIndex: Int): Int {
            val letter = sections[sectionIndex]
            return items.indexOfFirst {
                (it.firstOrNull()?.uppercaseChar() ?: '#') == letter
            }.coerceAtLeast(0)
        }

        override fun getSectionForPosition(position: Int): Int {
            val letter = items[position].firstOrNull()?.uppercaseChar() ?: '#'
            return sections.indexOf(letter)
        }
    }
    private fun showGifDialog(word: String) {
        // Full‑screen dialog
        val dialog = android.app.Dialog(
            this,
            android.R.style.Theme_Black_NoTitleBar_Fullscreen
        )

        // Create the GIF view
        val gifView = pl.droidsonroids.gif.GifImageView(this)
        gifView.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        gifView.scaleType = android.widget.ImageView.ScaleType.FIT_CENTER

        val normalized = normalizeString(word)
        val display = denormalizeString(normalized)
        val resId = availableGifs[display]

        if (resId == null) {
            Toast.makeText(this, "GIF not found for $word", Toast.LENGTH_LONG).show()
            return
        }

        gifView.setImageDrawable(
            pl.droidsonroids.gif.GifDrawable(resources, resId)
        )

        dialog.setContentView(gifView)
        dialog.show()
    }

    // ---------------- String utilities ----------------
    private fun normalizeString(str: String?): String {
        var word = str ?: return ""

        return word
            .replace("á", "a").replace("Á", "a")
            .replace("é", "e").replace("É", "e")
            .replace("í", "i").replace("Í", "i")
            .replace("ó", "o").replace("Ó", "o")
            .replace("ú", "u").replace("Ú", "u")
            .replace("ñ", "n").replace("Ñ", "n")
            .replace("ü", "u").replace("Ü", "u")
            .replace(" ", "_")
            .replace("[^a-zA-Z0-9_]".toRegex(), "")
            .lowercase()
    }

    private fun denormalizeString(str: String?): String {
        return str
            ?.replace("_", " ")
            ?.split(" ")
            ?.joinToString(" ") { it.replaceFirstChar { ch -> ch.uppercase() } }
            ?: ""
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
}