package com.app.adec.screens

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.app.adec.R

class CustomerServiceScreen : Fragment() {

    private val faqs = listOf(
        "¿Cómo encuentro eventos culturales?" to "Usa 'Explorar eventos' o 'Búsqueda general' para filtrar por ciudad o fecha.",
        "¿Puedo guardar favoritos?" to "En la siguiente versión habrá lista de favoritos.",
        "¿Cómo reporto un problema con un evento?" to "Usa la opción PQR y describe el problema con el mayor detalle posible.",
        "¿Necesito cuenta para usar la app?" to "No, pero si completas el perfil mejoramos las recomendaciones.",
        "¿Cómo contacto soporte?" to "Desde esta pantalla o vía PQR. Tiempo de respuesta 24–48 horas hábiles."
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.screen_customer_service, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val lv: ListView = view.findViewById(R.id.lvFaqs)
        val questions = faqs.map { it.first }
        lv.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, questions)
        lv.setOnItemClickListener { _, _, pos, _ ->
            androidx.appcompat.app.AlertDialog.Builder(requireContext())
                .setTitle(faqs[pos].first)
                .setMessage(faqs[pos].second)
                .setPositiveButton("OK", null)
                .show()
        }

        // 🔥 Botón de WhatsApp con verificación de instalación
        view.findViewById<Button>(R.id.btnContactWhatsApp).setOnClickListener {
            val phoneNumber = "573001112233" // <-- Cambia a tu número real (con indicativo)
            val message = Uri.encode("¡Hola! Necesito ayuda con la app de eventos culturales.")
            val url = "https://wa.me/$phoneNumber?text=$message"

            try {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(url)
                startActivity(intent)
            } catch (e: ActivityNotFoundException) {
                Toast.makeText(requireContext(), "⚠ WhatsApp no está instalado en este dispositivo.", Toast.LENGTH_LONG).show()
            }
        }
    }
}
