package com.app.adec.screens

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.app.adec.R

class ProfileScreen : Fragment() {

    // UI
    private lateinit var ivAvatar: ImageView
    private lateinit var btnChangePhoto: Button
    private lateinit var etName: EditText
    private lateinit var etEmail: EditText
    private lateinit var btnSave: Button

    // Preferencias
    private val prefs by lazy {
        requireContext().getSharedPreferences("profile_prefs", 0)
    }

    // Contract para seleccionar imagen
    private val pickImage = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        if (uri != null) {
            // Mostrar en la UI
            ivAvatar.setImageURI(uri)
            // Guardar URI en prefs
            prefs.edit().putString(KEY_AVATAR_URI, uri.toString()).apply()
        } else {
            Toast.makeText(requireContext(), "No se seleccionó imagen", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.screen_profile, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Vincular vistas
        ivAvatar = view.findViewById(R.id.ivAvatar)
        btnChangePhoto = view.findViewById(R.id.btnChangePhoto)
        etName = view.findViewById(R.id.etName)
        etEmail = view.findViewById(R.id.etEmail)
        btnSave = view.findViewById(R.id.btnSaveProfile)

        // Cargar datos ya guardados
        loadProfile()

        // Cambiar foto
        btnChangePhoto.setOnClickListener {
            pickImage.launch("image/*")
        }

        // Guardar perfil
        btnSave.setOnClickListener {
            val name = etName.text?.toString()?.trim().orEmpty()
            val email = etEmail.text?.toString()?.trim().orEmpty()

            // Validaciones simples
            if (name.isBlank()) {
                etName.error = "Escribe tu nombre"
                return@setOnClickListener
            }
            if (email.isBlank()) {
                etEmail.error = "Escribe tu correo"
                return@setOnClickListener
            }

            prefs.edit()
                .putString(KEY_NAME, name)
                .putString(KEY_EMAIL, email)
                .apply()

            Toast.makeText(requireContext(), "Perfil guardado con éxito", Toast.LENGTH_SHORT).show()
        }
    }

    private fun loadProfile() {
        val savedName = prefs.getString(KEY_NAME, "").orEmpty()
        val savedEmail = prefs.getString(KEY_EMAIL, "").orEmpty()
        val avatarUri = prefs.getString(KEY_AVATAR_URI, null)

        etName.setText(savedName)
        etEmail.setText(savedEmail)

        if (!avatarUri.isNullOrBlank()) {
            runCatching { ivAvatar.setImageURI(Uri.parse(avatarUri)) }
        }
    }

    companion object {
        private const val KEY_NAME = "key_name"
        private const val KEY_EMAIL = "key_email"
        private const val KEY_AVATAR_URI = "key_avatar_uri"
    }
}
