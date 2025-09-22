package com.app.adec

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment

// La clase hereda de Fragment y usa el layout que creamos
class MenuFragment : Fragment(R.layout.section_menu) {

    // Define una interfaz para comunicarse con la Activity. Es un contrato.
    // La Activity que contenga este fragmento DEBE implementar esta interfaz.
    interface OnOptionClickListener {
        fun onOptionClicked(option: String)
    }

    private var listener: OnOptionClickListener? = null

    // Este métod0 se llama justo después de que la vista del fragmento ha sido creada.
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Se comprueba si la Activity contenedora implementa la interfaz de comunicación.
        if (context is OnOptionClickListener) {
            listener = context as OnOptionClickListener
        } else {
            // Si no la implementa, lanza un error para avisar al desarrollador.
            throw ClassCastException("$context must implement OnOptionClickListener")
        }

        // Se asigna un listener a cada botón. Al hacer clic, se llama al métod0 de la interfaz.
        view.findViewById<Button>(R.id.btn_event_explorer).setOnClickListener {
            listener?.onOptionClicked("event_explorer")
        }
        view.findViewById<Button>(R.id.btn_generic_search).setOnClickListener {
            listener?.onOptionClicked("generic_search")
        }
        view.findViewById<Button>(R.id.btn_filter_by_date).setOnClickListener {
            listener?.onOptionClicked("filter_by_date")
        }
        view.findViewById<Button>(R.id.btn_event_manager).setOnClickListener {
            listener?.onOptionClicked("event_manager")
        }
        view.findViewById<Button>(R.id.btn_event_register).setOnClickListener {
            listener?.onOptionClicked("event_register")
        }
        view.findViewById<Button>(R.id.btn_customer_service).setOnClickListener {
            listener?.onOptionClicked("customer_service")
        }
        view.findViewById<Button>(R.id.btn_profile).setOnClickListener {
            listener?.onOptionClicked("profile")
        }
        view.findViewById<Button>(R.id.btn_web).setOnClickListener {
            listener?.onOptionClicked("web")
        }
    }
}