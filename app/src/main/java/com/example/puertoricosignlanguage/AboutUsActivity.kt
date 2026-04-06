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

        // Inicializar el DrawerLayout
        drawerLayout = findViewById(R.id.drawer_layout)

        // Declarando el nav home
        val navView: NavigationView = findViewById(R.id.nav_view)
        val btnOpenDrawer: ImageButton = findViewById(R.id.btn_open_drawer)

        navView.setCheckedItem(R.id.nav_about_us)

        // Abrirá el menú al tocar el botón
        btnOpenDrawer.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }

        navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_home -> {
                    val intent = Intent(this, MainActivity::class.java)

                    // Esto evitará que se abran muchas capas de la misma ventana
                    // Nota: Con el flag hace que se sombree Sobre Nosotros
                  //  intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                    startActivity(intent)
                }

                R.id.nav_about_us -> {
                    val intent = Intent(this, AboutUsActivity::class.java)
                }

                // Menú del diccionario
            //  R.id.nav_dictionary -> {
            //        val intent = Intent(this, DictionaryActivity::class.java)
            //       startActivity(intent)
            //  }
            }
            drawerLayout.closeDrawers()
            true
        }
    }
}