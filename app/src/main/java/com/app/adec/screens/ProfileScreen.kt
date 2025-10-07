package com.app.adec.screens

import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts.OpenDocument
import androidx.core.content.edit
import androidx.fragment.app.Fragment
import com.app.adec.R
import java.io.File
import java.io.FileOutputStream

/**
 * Pantalla de Perfil
 * - Crea/edita datos del perfil (nombre, email, teléfono)
 * - Permite seleccionar una foto de perfil desde la galería
 * - Copia la imagen a almacenamiento interno y persiste los datos con SharedPreferences (KTX)
 */
class ProfileScreen : Fragment(R.layout.screen_profile) {

    private companion object {
        const val PREFS_NAME = "profile_prefs"
        const val KEY_NAME = "name"
        const val KEY_EMAIL = "email"
        const val KEY_PHONE = "phone"
        const val KEY_PHOTO = "photo_uri"
    }

    // Vistas (solo lectura)
    private lateinit var boxView: LinearLayout
    private lateinit var tvName: TextView
    private lateinit var tvEmail: TextView
    private lateinit var tvPhone: TextView
    private lateinit var imgAvatar: ImageView
    private lateinit var btnEdit: Button
    private lateinit var btnChangePhoto: Button

    // Vistas (formulario)
    private lateinit var boxForm: LinearLayout
    private lateinit var imgAvatarForm: ImageView
    private lateinit var btnChangePhotoForm: Button
    private lateinit var etName: EditText
    private lateinit var etEmail: EditText
    private lateinit var etPhone: EditText
    private lateinit var btnSave: Button
    private lateinit var btnCancel: Button

    private var currentPhotoUri: String? = null

    // Selector de imagen (usa SAF y copia local)
    private val pickImage = registerForActivityResult(OpenDocument()) { uri: Uri? ->
        uri ?: return@registerForActivityResult
        val localUri = copyToInternal(uri)
        if (localUri != null) {
            updateAvatar(localUri)
            currentPhotoUri = localUri.toString()
        } else {
            Toast.makeText(requireContext(), "No se pudo cargar la imagen", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Enlazar vistas
        boxView = view.findViewById(R.id.boxView)
        tvName = view.findViewById(R.id.tvName)
        tvEmail = view.findViewById(R.id.tvEmail)
        tvPhone = view.findViewById(R.id.tvPhone)
        imgAvatar = view.findViewById(R.id.imgAvatar)
        btnEdit = view.findViewById(R.id.btnEditProfile)
        btnChangePhoto = view.findViewById(R.id.btnChangePhoto)

        boxForm = view.findViewById(R.id.boxForm)
        imgAvatarForm = view.findViewById(R.id.imgAvatarForm)
        btnChangePhotoForm = view.findViewById(R.id.btnChangePhotoForm)
        etName = view.findViewById(R.id.etName)
        etEmail = view.findViewById(R.id.etEmail)
        etPhone = view.findViewById(R.id.etPhone)
        btnSave = view.findViewById(R.id.btnSaveProfile)
        btnCancel = view.findViewById(R.id.btnCancelEdit)

        // Cargar datos
        loadProfile()

        // Acciones
        val mime = arrayOf("image/*")
        btnChangePhoto.setOnClickListener { pickImage.launch(mime) }
        btnChangePhotoForm.setOnClickListener { pickImage.launch(mime) }
        imgAvatar.setOnClickListener { pickImage.launch(mime) }
        imgAvatarForm.setOnClickListener { pickImage.launch(mime) }

        btnEdit.setOnClickListener { switchToFormWithCurrentData() }
        btnSave.setOnClickListener { saveProfile() }
        btnCancel.setOnClickListener { switchToView() }
    }

    // --- Persistencia ---

    private fun loadProfile() {
        val prefs = requireContext().getSharedPreferences(PREFS_NAME, 0)
        val name = prefs.getString(KEY_NAME, null)
        val email = prefs.getString(KEY_EMAIL, null)
        val phone = prefs.getString(KEY_PHONE, null)
        val photo = prefs.getString(KEY_PHOTO, null)
        currentPhotoUri = photo

        if (photo?.isNotBlank() == true) {
            runCatching { updateAvatar(Uri.parse(photo)) }
                .onFailure {
                    imgAvatar.setImageResource(R.drawable.ic_launcher_foreground)
                    imgAvatarForm.setImageResource(R.drawable.ic_launcher_foreground)
                }
        } else {
            imgAvatar.setImageResource(R.drawable.ic_launcher_foreground)
            imgAvatarForm.setImageResource(R.drawable.ic_launcher_foreground)
        }

        if (name.isNullOrBlank() && email.isNullOrBlank() && phone.isNullOrBlank()) {
            etName.setText("")
            etEmail.setText("")
            etPhone.setText("")
            switchToForm()
        } else {
            tvName.text = name.orEmpty()
            tvEmail.text = email.orEmpty()
            tvPhone.text = phone.orEmpty()
            switchToView()
        }
    }

    private fun saveProfile() {
        val name = etName.text.toString().trim()
        val email = etEmail.text.toString().trim()
        val phone = etPhone.text.toString().trim()

        if (name.isBlank()) {
            etName.error = getString(R.string.err_required)
            etName.requestFocus(); return
        }
        if (email.isBlank()) {
            etEmail.error = getString(R.string.err_required)
            etEmail.requestFocus(); return
        }

        val prefs = requireContext().getSharedPreferences(PREFS_NAME, 0)
        prefs.edit {
            putString(KEY_NAME, name)
            putString(KEY_EMAIL, email)
            putString(KEY_PHONE, phone)
            putString(KEY_PHOTO, currentPhotoUri)
        }

        Toast.makeText(requireContext(), R.string.msg_saved_ok, Toast.LENGTH_SHORT).show()

        tvName.text = name
        tvEmail.text = email
        tvPhone.text = phone
        switchToView()
    }

    // --- UI Helpers ---

    private fun switchToFormWithCurrentData() {
        etName.setText(tvName.text)
        etEmail.setText(tvEmail.text)
        etPhone.setText(tvPhone.text)
        switchToForm()
    }

    private fun switchToForm() {
        boxView.visibility = View.GONE
        boxForm.visibility = View.VISIBLE
    }

    private fun switchToView() {
        boxForm.visibility = View.GONE
        boxView.visibility = View.VISIBLE
    }

    // --- Utilidades ---
    private fun updateAvatar(uri: Uri) {
        imgAvatar.setImageURI(uri)
        imgAvatarForm.setImageURI(uri)
    }

    private fun copyToInternal(source: Uri): Uri? {
        return try {
            val ctx = requireContext()
            val dir = File(ctx.filesDir, "images").apply { if (!exists()) mkdirs() }
            val file = File(dir, "avatar.jpg")

            ctx.contentResolver.openInputStream(source)?.use { input ->
                FileOutputStream(file).use { output -> input.copyTo(output) }
            }
            Uri.fromFile(file)
        } catch (e: Exception) {
            e.printStackTrace(); null
        }
    }
}
