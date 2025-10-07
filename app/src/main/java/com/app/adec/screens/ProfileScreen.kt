package com.app.adec.screens

import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.app.adec.R

class ProfileScreen : Fragment() {

    // --- Claves de SharedPreferences ---
    private val PREFS_NAME = "user_profile_prefs"
    private val K_NAME = "name"
    private val K_EMAIL = "email"
    private val K_PHONE = "phone"

    // --- Vistas (modo lectura) ---
    private lateinit var boxView: View
    private lateinit var tvName: TextView
    private lateinit var tvEmail: TextView
    private lateinit var tvPhone: TextView
    private lateinit var btnEditProfile: Button

    // --- Vistas (formulario) ---
    private lateinit var boxForm: View
    private lateinit var etName: EditText
    private lateinit var etEmail: EditText
    private lateinit var etPhone: EditText
    private lateinit var btnSaveProfile: Button
    private lateinit var btnCancelEdit: Button

    // --- Prefs ---
    private val prefs by lazy {
        requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // IMPORTANTE: usa el layout donde dejaste los ids tal cual
        return inflater.inflate(R.layout.screen_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // --- Referencias a vistas ---
        boxView = view.findViewById(R.id.boxView)
        tvName = view.findViewById(R.id.tvName)
        tvEmail = view.findViewById(R.id.tvEmail)   // << asegurarse que NO sea tvEmai
        tvPhone = view.findViewById(R.id.tvPhone)
        btnEditProfile = view.findViewById(R.id.btnEditProfile)

        boxForm = view.findViewById(R.id.boxForm)
        etName = view.findViewById(R.id.etName)
        etEmail = view.findViewById(R.id.etEmail)
        etPhone = view.findViewById(R.id.etPhone)
        btnSaveProfile = view.findViewById(R.id.btnSaveProfile)
        btnCancelEdit = view.findViewById(R.id.btnCancelEdit)

        // --- Listeners ---
        btnEditProfile.setOnClickListener { switchToEditMode(prefill = true) }
        btnCancelEdit.setOnClickListener {
            switchToViewMode()
        }
        btnSaveProfile.setOnClickListener {
            onSaveProfile()
        }

        // --- Cargar datos: si no hay nombre -> primer uso -> formulario ---
        loadProfileOrAskForIt()
    }

    // Carga datos guardados; si no hay, abre el formulario
    private fun loadProfileOrAskForIt() {
        val name = prefs.getString(K_NAME, "").orEmpty()
        val email = prefs.getString(K_EMAIL, "").orEmpty()
        val phone = prefs.getString(K_PHONE, "").orEmpty()

        if (name.isBlank()) {
            // Primer uso: mostrar formulario vacío y enfocar el nombre
            switchToEditMode(prefill = false)
        } else {
            // Mostrar modo lectura con los datos
            tvName.text = name
            tvEmail.text = if (email.isBlank()) "—" else email
            tvPhone.text = if (phone.isBlank()) "—" else phone
            switchToViewMode()
        }
    }

    // Guarda los datos del formulario
    private fun onSaveProfile() {
        val name = etName.text?.toString()?.trim().orEmpty()
        val email = etEmail.text?.toString()?.trim().orEmpty()
        val phone = etPhone.text?.toString()?.trim().orEmpty()

        // Validación mínima: nombre requerido
        if (name.isBlank()) {
            etName.error = getString(R.string.profile_error_name_required)
            etName.requestFocus()
            showKeyboard(etName)
            return
        }

        // (Opcional) Validación simple de correo
        if (email.isNotBlank() && !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.error = getString(R.string.profile_error_email_invalid)
            etEmail.requestFocus()
            showKeyboard(etEmail)
            return
        }

        // Guardar en SharedPreferences
        prefs.edit()
            .putString(K_NAME, name)
            .putString(K_EMAIL, email)
            .putString(K_PHONE, phone)
            .apply()

        // Reflejar en modo lectura
        tvName.text = name
        tvEmail.text = if (email.isBlank()) "—" else email
        tvPhone.text = if (phone.isBlank()) "—" else phone

        // Mensaje de éxito
        Toast.makeText(requireContext(), getString(R.string.profile_saved_ok), Toast.LENGTH_SHORT).show()

        // Cambiar a modo lectura
        switchToViewMode()
    }

    // Cambia a modo lectura
    private fun switchToViewMode() {
        hideKeyboard()
        boxView.isVisible = true
        boxForm.isVisible = false
    }

    // Cambia a modo edición; si prefill = true, trae datos guardados; si no, deja limpio
    private fun switchToEditMode(prefill: Boolean) {
        boxView.isVisible = false
        boxForm.isVisible = true

        if (prefill) {
            etName.setText(prefs.getString(K_NAME, ""))
            etEmail.setText(prefs.getString(K_EMAIL, ""))
            etPhone.setText(prefs.getString(K_PHONE, ""))
        } else {
            etName.setText("")
            etEmail.setText("")
            etPhone.setText("")
        }

        etName.requestFocus()
        showKeyboard(etName)
    }

    // Utilidades de teclado
    private fun showKeyboard(target: View) {
        target.post {
            val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(target, InputMethodManager.SHOW_IMPLICIT)
        }
    }

    private fun hideKeyboard() {
        val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val v = requireActivity().currentFocus
        if (v != null) imm.hideSoftInputFromWindow(v.windowToken, 0)
    }
}
