package com.app.adec.screens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import com.app.adec.R
import com.app.adec.data.GLOBALEvents
import com.app.adec.model.Event
import java.time.format.DateTimeFormatter

class GeneralSearchScreen : Fragment() {

    private lateinit var cardsContainer: LinearLayout
    private lateinit var searchEditText: EditText
    private lateinit var searchButton: ImageView
    private lateinit var resultLabel: TextView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Infla el layout base del fragmento
        val view = inflater.inflate(R.layout.screen_general_search, container, false)

        // Busca el contenedor dentro del layout inflado
        cardsContainer = view.findViewById(R.id.cards_container)
        searchEditText = view.findViewById(R.id.filter_search_text)
        searchButton = view.findViewById(R.id.search_icon)
        resultLabel = view.findViewById(R.id.result_label)

        // Set an OnClickListener for the search button
        searchButton.setOnClickListener {
            filterEvents(searchEditText.text.toString())
        }

        // Initially, the cardsContainer is empty
        return view
    }

    private fun filterParameters(query: String, field: String): Boolean {
        return field.contains(query, ignoreCase = true)
    }

    private fun filterEvents(query: String) {
        // Clear existing cards
        cardsContainer.removeAllViews()

        val allEvents = GLOBALEvents.getEvents(showDeleted = false)
        val filteredEvents = if (query.isBlank()) {
            emptyList()
        } else {
            allEvents.filter { event ->

                filterParameters(query, event.title) ||
                        filterParameters(query, event.artist) ||
                        filterParameters(query, event.category) ||
                        filterParameters(query, event.location) ||
                        filterParameters(query, event.description)
            }
        }

        if (filteredEvents.isEmpty()) {
            this.resultLabel.visibility = View.VISIBLE
        } else {
            this.resultLabel.visibility = View.INVISIBLE
            for (event in filteredEvents) {
                addEventCardToContainer(event)
            }
        }
    }

    private fun addEventCardToContainer(event: Event) { // Assuming Event is your data class
        // Infla y añade cada card filtrada
        val cardView = layoutInflater.inflate(R.layout.component_event_card_view, cardsContainer, false)

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
        eventLogoImage.setImageURI(event.logoUri!!.toUri())
        eventMainImage.setImageURI(event.imageUri!!.toUri())

        button.setOnClickListener {
            val detailFragment = EventDetailScreen.newInstance(event)

            parentFragmentManager.beginTransaction()
                .replace(R.id.content_fragment_container, detailFragment)
                .addToBackStack(null)
                .commit()
        }

        cardsContainer.addView(cardView)
    }
}