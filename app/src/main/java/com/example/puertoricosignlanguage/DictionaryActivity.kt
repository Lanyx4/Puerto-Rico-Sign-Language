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
            "¿a_qué_hora?",
            "abogado",
            "abuela",
            "abuelo",
            "agua",
            "alegría",
            "amor",
            "arroz",
            "ayúdame",
            "ayudar",
            "bayamón",
            "bebé",
            "bombero",
            "café",
            "caguas",
            "cambiar",
            "cantante",
            "carne",
            "carolina",
            "carta",
            "cataño",
            "cayey",
            "cocinar",
            "cocinero",
            "comer",
            "cómo",
            "comparar",
            "compartir",
            "conducir",
            "conflicto",
            "conversar",
            "cooperación",
            "correo electrónico",
            "correr",
            "creer",
            "cuándo",
            "cuánto",
            "cuñada",
            "cuñado",
            "decidir",
            "decir",
            "decirme",
            "defender",
            "déjalo",
            "descansar",
            "discusión",
            "doctor",
            "dolor",
            "¿dónde?",
            "dorado",
            "dormir",
            "él",
            "ella",
            "ellas",
            "ellos",
            "enseñar",
            "escribir",
            "esperar",
            "explicar",
            "fajardo",
            "familia",
            "fotógrafo",
            "frustración",
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
            "intérprete",
            "jugo",
            "junto",
            "leche",
            "leer",
            "limón",
            "madrastra",
            "maestro",
            "mamá",
            "mayagüez",
            "me olvidé",
            "mecánico",
            "mirar",
            "no",
            "no_entiendo",
            "nombre",
            "no sé",
            "nosotros",
            "odio",
            "ok",
            "papá",
            "película",
            "peluquero",
            "pensar",
            "perder",
            "perdón",
            "periódico",
            "persona",
            "pescado",
            "pizza",
            "plátano",
            "policía",
            "ponce",
            "por_favor",
            "¿por_qué?",
            "precio",
            "presión",
            "prima",
            "primo",
            "puerto_rico",
            "¿qué?",
            "recordar",
            "rincón",
            "saber",
            "san_juan",
            "señas",
            "sí",
            "significado",
            "sobrina",
            "sobrino",
            "soñar",
            "su",
            "suegra",
            "suegro",
            "teléfono",
            "televisión",
            "tía",
            "tío",
            "tostones",
            "tristeza",
            "tú",
            "USC",
            "vieques",
            "yo",
            "abril",
            "¿a_dónde_vas?",
            "buenos_días",
            "buenas_noches",
            "viernes",
            "ayer",
            "ahora",
            "adiós",
            "buenas_tardes",
            "¿cómo_estás?",
            "¿cómo_te_llamas?",
            "cuál",
            "de_nada",
            "cuatro",
            "día",
            "cinco",
            "diciembre",
            "dieciocho",
            "veinte",
            "diecinueve",
            "diecisiete",
            "diez",
            "domingo",
            "doce",
            "febrero",
            "hoy",
            "enero",
            "jueves",
            "hola",
            "junio",
            "lunes",
            "julio",
            "mañana",
            "marzo",
            "martes",
            "ocho",
            "nueve",
            "noviembre",
            "miércoles",
            "mes",
            "mayo",
            "mensual",
            "once",
            "octubre",
            "¿qué pasó?",
            "sábado",
            "quince",
            "¿quieres?",
            "¿quién?",
            "seis",
            "tres",
            "trece",
            "semanal",
            "septiembre",
            "uno",
            "nieta",
            "nieto",
            "novio",
            "peligroso",
            "prohibido",
            "permiso",
            "buen_provecho",
            "pena",
            "baño",
            "emergencia",
            "jabón",
            "paz",
            "oficina",
            "hospital",
            "está_bien",
            "cena",
            "desayuno",
            "cien",
            "frío",
            "treinta",
            "cuarenta",
            "cincuenta",
            "sesenta",
            "ochenta",
            "noventa",
            "agosto",
            "arecibo",
            "catorce",
            "dieciseis",
            "dos",
            "escuchar",
            "esposa",
            "esposo",
            "hermano",
            "mango",
            "manzana",
            "nervios",
            "no_me_gusta",
            "olvidar",
            "padrastro",
            "semana",
            "setenta",
            "siete",
            "sonreír",
            "tocar"
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
        val dialog = android.app.Dialog(
            this,
            android.R.style.Theme_Black_NoTitleBar_Fullscreen
        )

        // Root container (FrameLayout so we can overlay the X button)
        val rootFrame = android.widget.FrameLayout(this)
        rootFrame.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        rootFrame.setBackgroundColor(android.graphics.Color.BLACK)

        // GIF view
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

        // X close button
        val closeButton = android.widget.ImageButton(this)
        val sizePx = (48 * resources.displayMetrics.density).toInt()
        val marginPx = (16 * resources.displayMetrics.density).toInt()
        val closeParams = android.widget.FrameLayout.LayoutParams(sizePx, sizePx)
        closeParams.gravity = android.view.Gravity.TOP or android.view.Gravity.END
        closeParams.topMargin = marginPx
        closeParams.rightMargin = marginPx
        closeButton.layoutParams = closeParams
        closeButton.setImageDrawable(
            androidx.core.content.ContextCompat.getDrawable(
                this, android.R.drawable.ic_menu_close_clear_cancel
            )
        )
        closeButton.setBackgroundResource(android.R.drawable.btn_default)
        closeButton.contentDescription = "Close"
        closeButton.setOnClickListener { dialog.dismiss() }

        rootFrame.addView(gifView)
        rootFrame.addView(closeButton)

        dialog.setContentView(rootFrame)
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