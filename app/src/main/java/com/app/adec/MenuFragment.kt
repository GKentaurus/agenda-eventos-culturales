package com.app.adec

import android.content.Context
import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.app.adec.R

class MenuFragment : Fragment() {

    interface OnOptionClickListener {
        fun onOptionClicked(option: String)
    }

    private var listener: OnOptionClickListener? = null
    private lateinit var buttons: List<Pair<Button, String>>

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnOptionClickListener) {
            listener = context
        } else {
            throw IllegalStateException("MainActivity debe implementar MenuFragment.OnOptionClickListener")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.section_menu, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnProfile = view.findViewById<Button>(R.id.btn_profile)
        val btnEventExplorer = view.findViewById<Button>(R.id.btn_event_explorer)
        val btnGenericSearch = view.findViewById<Button>(R.id.btn_generic_search)
        val btnFilterByDate = view.findViewById<Button>(R.id.btn_filter_by_date)
        val btnEventRegister = view.findViewById<Button>(R.id.btn_event_register)
        val btnEventManager = view.findViewById<Button>(R.id.btn_event_manager)
        val btnWeb = view.findViewById<Button>(R.id.btn_web)
        val btnCustomerService = view.findViewById<Button>(R.id.btn_customer_service)
        val btnPqr = view.findViewById<Button>(R.id.btn_pqr)

        // Pair<Button, optionString> (usa los mismos strings que tu MainActivity)
        buttons = listOf(
            btnProfile to "profile",
            btnEventExplorer to "event_explorer",
            btnGenericSearch to "generic_search",
            btnFilterByDate to "filter_by_date",
            btnEventRegister to "event_register",
            btnEventManager to "event_manager",
            btnWeb to "web",
            btnCustomerService to "customer_service",
            btnPqr to "pqr"
        )

        // Listeners
        buttons.forEach { (btn, option) ->
            btn.setOnClickListener {
                selectButton(btn)
                listener?.onOptionClicked(option)
            }
        }

        // Opcional: marca por defecto el primero (o el que esté activo al abrir)
        selectButton(btnEventExplorer) // para coincidir con tu default en MainActivity
    }

    private fun selectButton(selected: Button) {
        buttons.forEach { (btn, _) ->
            val isSelected = btn == selected
            btn.isSelected = isSelected
            btn.setTypeface(null, if (isSelected) Typeface.BOLD else Typeface.NORMAL)
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }
}
