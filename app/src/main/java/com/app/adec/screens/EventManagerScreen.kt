package com.app.adec.screens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import com.app.adec.R
import com.app.adec.data.GLOBALEvents
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class EventManagerScreen : Fragment() {

    private var cardsContainer: LinearLayout? = null
    private var inflater: LayoutInflater? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Guarda una referencia al inflater y al contenedor para usarlos después
        this.inflater = inflater
        val view = inflater.inflate(R.layout.screen_event_manager, container, false)

        // Busca el contenedor dentro del layout inflado
        cardsContainer = view.findViewById(R.id.cards_container)

        // Carga los eventos por primera vez
        refreshEventList()

        return view
    }

    private fun refreshEventList() {
        // Asegúrate de que el contenedor y el inflater no sean nulos
        val container = cardsContainer ?: return
        val layoutInflater = inflater ?: return

        // Limpia las vistas antiguas antes de volver a dibujar
        container.removeAllViews()

        val filteredEvents = GLOBALEvents.getEvents(showDeleted = true)
            .filter { event -> event.datetime.isAfter(LocalDateTime.now()) }
            .sortedBy { event -> event.datetime }

        // Infla y añade cada card
        for (event in filteredEvents) {
            val cardView = layoutInflater.inflate(R.layout.component_event_card_manage, container, false)

            val artistName = cardView.findViewById<TextView>(R.id.artist_name)
            val category = cardView.findViewById<TextView>(R.id.event_category)
            val eventTitle = cardView.findViewById<TextView>(R.id.event_title)
            val eventDateTime = cardView.findViewById<TextView>(R.id.event_datetime)
            val eventDescription = cardView.findViewById<TextView>(R.id.event_description)
            val eventLogoImage = cardView.findViewById<ImageView>(R.id.event_logo_image)
            val eventMainImage = cardView.findViewById<ImageView>(R.id.event_main_image)
            val toogleButton = cardView.findViewById<Button>(R.id.btn_toogle)
            val editButton = cardView.findViewById<Button>(R.id.bnt_edit)

            artistName.text = event.artist
            category.text = event.category
            eventTitle.text = event.title
            eventDateTime.text = event.datetime.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"))
            eventDescription.text = event.description
            eventLogoImage.setImageURI(event.logoUri!!.toUri())
            eventMainImage.setImageURI(event.imageUri!!.toUri())

            if (event.deleted) {
                toogleButton.text = "Restaurar"
            } else {
                toogleButton.text = "Eliminar"
            }

            toogleButton.setOnClickListener {
                event.deleted = !event.deleted
                refreshEventList()
            }

            editButton.setOnClickListener {
                val detailFragment = EventRegisterScreen.newInstance(event)

                parentFragmentManager.beginTransaction()
                    .replace(R.id.content_fragment_container, detailFragment)
                    .addToBackStack(null)
                    .commit()
            }

            container.addView(cardView)
        }
    }
}
