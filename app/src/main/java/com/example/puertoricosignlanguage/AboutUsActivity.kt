package com.example.puertoricosignlanguage
// Melanie
import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView

class AboutUsActivity : AppCompatActivity() {
    private lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about_us)

        // Inicializar el DrawerLayout que está en activity_main.xml
        drawerLayout = findViewById(R.id.drawer_layout)

        // Declarando el nav home
        val navView: NavigationView = findViewById(R.id.nav_view)
        val btnOpenDrawer: ImageButton = findViewById(R.id.btn_open_drawer)

        navView.setCheckedItem(R.id.nav_about_us)

        // Abrirá el menú al tocar el botón
        btnOpenDrawer.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }

        // La navegación de menú
        navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {

                // Menú para ir a la página principal
                R.id.nav_home -> {
                    startActivity(Intent(this, MainActivity::class.java))
                }

                // Menú para misma actividad (About Us) en la que estamos
                R.id.nav_about_us -> {
                    Intent(this, AboutUsActivity::class.java)
                }

                // Menú para ir a la página del Diccionario
                R.id.nav_dictionary -> {
                    startActivity(Intent(this, DictionaryActivity::class.java))
                }
            }
            drawerLayout.closeDrawers()
            true
        }
    }
}