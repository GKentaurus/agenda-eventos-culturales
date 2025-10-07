package com.app.adec

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.app.adec.screens.CustomerServiceScreen
import com.app.adec.screens.EventExplorerScreen
import com.app.adec.screens.EventManagerScreen
import com.app.adec.screens.EventRegisterScreen
import com.app.adec.screens.FilterByDateScreen
import com.app.adec.screens.GeneralSearchScreen
import com.app.adec.screens.ProfileScreen
import com.app.adec.screens.WebBrowserScreen
import com.app.adec.screens.PqrScreen // ✅ Importamos la nueva pantalla de PQR

// La MainActivity implementa la interfaz del MenuFragment para poder recibir los eventos de clic.
class MainActivity : AppCompatActivity(), MenuFragment.OnOptionClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Si es la primera vez que se crea la actividad, carga el fragmento por defecto.
        // Esto evita que se recargue el fragmento al girar la pantalla, por ejemplo.
        if (savedInstanceState == null) {
            onOptionClicked("") // Mantengo el código original
        }
    }

    // Este es el métod0 obligatorio que implementa la interfaz. Se ejecuta cuando se pulsa un botón en el menú.
    override fun onOptionClicked(option: String) {
        // Usa una expresión 'when' (similar a un switch) para decidir qué fragmento mostrar.
        val fragment: Fragment = when (option) {
            "event_explorer" -> EventExplorerScreen() // Carlos
            "generic_search" -> GeneralSearchScreen() // Carlos
            "filter_by_date" -> FilterByDateScreen() // Peter
            "event_manager" -> EventManagerScreen() // Camilo?
            "event_register" -> EventRegisterScreen() // Camilo?
            "customer_service" -> CustomerServiceScreen() // TODO: Ferney
            "pqr" -> PqrScreen() //  Nuevo caso para abrir el formulario PQR
            "profile" -> ProfileScreen() // TODO: Omar
            "web" -> WebBrowserScreen() // Omar
            else -> EventExplorerScreen() // Caso por defecto
        }

        // Inicia una transacción de fragmentos para reemplazar el contenido del contenedor derecho.
        supportFragmentManager.commit {
            replace(R.id.content_fragment_container, fragment)
            setReorderingAllowed(true) // Optimización para las transiciones
        }
    }
}
