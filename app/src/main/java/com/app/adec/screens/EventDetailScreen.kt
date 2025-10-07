package com.app.adec.screens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import com.app.adec.R
import com.app.adec.model.Event
import java.time.format.DateTimeFormatter

class EventDetailScreen : Fragment() {

    private var event: Event? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Recupera el Event desde los argumentos
        event = arguments?.getParcelable("event_data")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.screen_event_detail, container, false)

        // Vincula datos con vistas
        event?.let { e ->
            view.findViewById<TextView>(R.id.event_detail_artist_name).text = e.artist
            view.findViewById<TextView>(R.id.event_detail_category).text = e.category

            if (e.imageResId != null) {
                view.findViewById<ImageView>(R.id.event_detail_image)
                    .setImageResource(e.imageResId!!)
            } else if (e.imageUri != null) {
                view.findViewById<ImageView>(R.id.event_detail_image)
                    .setImageURI(e.imageUri!!.toUri())
            }

            view.findViewById<TextView>(R.id.event_detail_description).text = e.description

            view.findViewById<TextView>(R.id.event_detail_location).text = e.location
            view.findViewById<TextView>(R.id.event_detail_date).text = e.datetime.toLocalDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
            view.findViewById<TextView>(R.id.event_detail_date).text = e.datetime.toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm:ss"))

            view.findViewById<TextView>(R.id.event_detail_ticket_value).text = String.format("$ %s.00", e.ticketValue.toString())

        }

        return view
    }

    companion object {
        fun newInstance(event: Event): EventDetailScreen {
            val fragment = EventDetailScreen()
            val args = Bundle()
            args.putParcelable("event_data", event)
            fragment.arguments = args
            return fragment
        }
    }
}
