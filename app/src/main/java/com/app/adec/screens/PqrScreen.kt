package com.app.adec.screens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.app.adec.R

class PqrScreen : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.screen_pqr, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val et: EditText = view.findViewById(R.id.etPqr)
        val btn: Button = view.findViewById(R.id.btnSendPqr)

        btn.setOnClickListener {
            val text = et.text.toString().trim()
            if (text.isBlank()) {
                Toast.makeText(requireContext(), "Escribe tu solicitud.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            // Simulación de envío
            Toast.makeText(requireContext(), "✅ PQR enviada. ¡Gracias!", Toast.LENGTH_LONG).show()
            et.setText("")
        }
    }
}
