package com.app.adec.screens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.app.adec.R
import com.app.adec.data.EventsConstants
import java.time.format.DateTimeFormatter

class EventExplorerScreen : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Infla el layout base del fragmento
        val view = inflater.inflate(R.layout.screen_event_explorer, container, false)

        // Busca el contenedor dentro del layout inflado
        val cardsContainer = view.findViewById<LinearLayout>(R.id.cards_container)

        // Infla y añade cada card
        for (event in EventsConstants().EVENTS) {
            val cardView = layoutInflater.inflate(R.layout.component_event_card, cardsContainer, false)

            val artistName = cardView.findViewById<TextView>(R.id.artist_name)
            val category = cardView.findViewById<TextView>(R.id.event_category)
            val eventTitle = cardView.findViewById<TextView>(R.id.event_title)
            val eventDateTime = cardView.findViewById<TextView>(R.id.event_datetime)
            val eventDescription = cardView.findViewById<TextView>(R.id.event_description)
            val eventLogoImage = cardView.findViewById<ImageView>(R.id.event_logo_image)
            val eventMainImage = cardView.findViewById<ImageView>(R.id.event_main_image)
            val button = cardView.findViewById<Button>(R.id.btn_event_explorer)

            artistName.text = event.artist
            category.text = event.category
            eventTitle.text = event.title
            eventDateTime.text = event.datetime.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"))
            eventDescription.text = event.description
            eventLogoImage.setImageResource(event.logo)
            eventMainImage.setImageResource(event.image)

            button.setOnClickListener {
                val detailFragment = EventDetailScreen.newInstance(event)

                parentFragmentManager.beginTransaction()
                    .replace(R.id.content_fragment_container, detailFragment)
                    .addToBackStack(null)
                    .commit()
            }

            cardsContainer.addView(cardView)
        }

        return view
    }
}