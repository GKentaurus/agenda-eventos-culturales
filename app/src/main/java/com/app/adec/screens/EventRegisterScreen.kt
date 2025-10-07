package com.app.adec.screens

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
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
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import com.app.adec.R
import com.app.adec.data.GLOBALEvents
import com.app.adec.model.Event
import com.google.android.material.textfield.TextInputEditText
import java.io.File
import java.io.FileOutputStream
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.UUID

class EventRegisterScreen : Fragment() {

    private val newID = GLOBALEvents.nextEventID()

    private lateinit var imageViewMain: ImageView
    private lateinit var imageViewLogo: ImageView
    private lateinit var artistField: EditText
    private lateinit var categoryField: EditText
    private lateinit var titleField: EditText
    private lateinit var descriptionField: EditText
    private lateinit var priceField: EditText
    private lateinit var locationField: EditText
    private var selectedDateTime: LocalDateTime? = null
    private lateinit var saveButton: Button
    private lateinit var cancelButton: Button
    private var uriMain: Uri? = null
    private var uriLogo: Uri? = null

    private var event: Event? = null

    // Registrador para la imagen principal
    private val pickMainImage = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { tempUri: Uri? ->
        tempUri?.let {
            // Concede permiso persistente para el URI temporal (importante)
            requireContext().contentResolver.takePersistableUriPermission(it, Intent.FLAG_GRANT_READ_URI_PERMISSION)
            // Copia el archivo y actualiza la UI
            uriMain = copyUriToInternalStorage(it, "main_${UUID.randomUUID().toString().replace("-","_")}")
            imageViewMain.setImageURI(uriMain)
        }
    }

    // Registrador para el logo
    private val pickLogoImage = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { tempUri: Uri? ->
        tempUri?.let {
            // Concede permiso persistente para el URI temporal (importante)
            requireContext().contentResolver.takePersistableUriPermission(it, Intent.FLAG_GRANT_READ_URI_PERMISSION)
            // Copia el archivo y actualiza la UI
            uriLogo = copyUriToInternalStorage(it, "logo_${UUID.randomUUID().toString().replace("-","_")}")
            imageViewLogo.setImageURI(uriLogo)
        }
    }

    private fun copyUriToInternalStorage(uri: Uri, fileName: String): Uri? {
        return try {
            val inputStream = requireContext().contentResolver.openInputStream(uri)
            val file = File(requireContext().cacheDir, fileName)
            val outputStream = FileOutputStream(file)
            inputStream?.copyTo(outputStream)
            inputStream?.close()
            outputStream.close()
            // Devuelve el URI del archivo copiado
            file.toUri()
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(requireContext(), "Failed to copy image", Toast.LENGTH_SHORT).show()
            null
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Recupera el Event desde los argumentos
        event = arguments?.getParcelable("event_data")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.screen_event_register, container, false)

        // Image views
        imageViewMain = view.findViewById(R.id.mainImagePreview)
        imageViewLogo = view.findViewById(R.id.logoImagePreview)

        // Text fields
        artistField = view.findViewById(R.id.artist)
        categoryField = view.findViewById(R.id.category)
        titleField = view.findViewById(R.id.title)
        descriptionField = view.findViewById(R.id.description)
        locationField = view.findViewById(R.id.location)
        priceField = view.findViewById(R.id.price)

        // Buttons
        saveButton = view.findViewById(R.id.submit_button)
        cancelButton = view.findViewById(R.id.cancel_button)

        // Launch image pickers when the buttons are clicked
        view.findViewById<Button>(R.id.loadMainImage).setOnClickListener {
            pickMainImage.launch("image/*")
        }
        view.findViewById<Button>(R.id.loadLogoImage).setOnClickListener {
            pickLogoImage.launch("image/*")
        }

        saveButton.setOnClickListener { onSaveClicked() }
        cancelButton.setOnClickListener { requireActivity().onBackPressedDispatcher.onBackPressed() }

        this.loadEvent()

        return view
    }


    private fun loadEvent() {
        event?.let { event ->
            // La lógica ahora es única para los URIs
            if (event.logoUri != null) {
                val loadedUri = event.logoUri!!.toUri()
                imageViewLogo.setImageURI(loadedUri)
                uriLogo = loadedUri
            }

            if (event.imageUri != null) {
                val loadedUri = event.imageUri!!.toUri()
                imageViewMain.setImageURI(loadedUri)
                uriMain = loadedUri
            }

            artistField.setText(event.artist)
            categoryField.setText(event.category)
            titleField.setText(event.title)
            descriptionField.setText(event.description)
            locationField.setText(event.location)
            priceField.setText(event.ticketValue.toString())
            selectedDateTime = event.datetime
            saveButton.setOnClickListener { onUpdateClicked(event) }
        }
    }



    private fun validateInputs(): Boolean {
        var valid = true

        fun EditText.requireText(): Boolean {
            return if (text.toString().trim().isEmpty()) {
                error = "Required"
                valid = false
                false
            } else true
        }

        // Text fields
        artistField.requireText()
        categoryField.requireText()
        titleField.requireText()
        descriptionField.requireText()
        priceField.requireText()

        // Images
        if (uriMain == null) {
            Toast.makeText(
                requireContext(),
                "Please select a main image", Toast.LENGTH_SHORT
            ).show()
            valid = false
        }
        if (uriLogo == null) {
            Toast.makeText(
                requireContext(),
                "Please select a logo image", Toast.LENGTH_SHORT
            ).show()
            valid = false
        }

        return valid
    }


    private fun onSaveClicked() {
        if (!validateInputs()) {
            return
        }

        val event = Event(
            id = newID,
            artist = artistField.text.toString().trim(),
            category = categoryField.text.toString().trim(),
            title = titleField.text.toString().trim(),
            location = locationField.text.toString().trim(),
            datetime = selectedDateTime
                ?: throw IllegalStateException("Event date/time is required"),
            description = descriptionField.text.toString().trim(),
            ticketValue = priceField.text.toString().trim().toBigDecimal(),
            imageUri = uriMain!!.toString(),
            logoUri = uriLogo!!.toString(),
        )

        GLOBALEvents.registerEvent(event)

        Toast.makeText(requireContext(), "Event saved", Toast.LENGTH_SHORT).show()
        requireActivity().onBackPressedDispatcher.onBackPressed()
    }

    private fun onUpdateClicked(event: Event) {
        if (!validateInputs()) {
            return
        }

        event.artist = artistField.text.toString().trim()
        event.category = categoryField.text.toString().trim()
        event.title = titleField.text.toString().trim()
        event.location = locationField.text.toString().trim()
        event.datetime = selectedDateTime?: throw IllegalStateException("Event date/time is required")
        event.description = descriptionField.text.toString().trim()
        event.ticketValue = priceField.text.toString().trim().toBigDecimal()
        event.imageUri = uriMain!!.toString()
        event.logoUri = uriLogo!!.toString()

        Toast.makeText(requireContext(), "Event Updated", Toast.LENGTH_SHORT).show()
        requireActivity().onBackPressedDispatcher.onBackPressed()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        uriMain?.let { outState.putString("uri_main", it.toString()) }
        uriLogo?.let { outState.putString("uri_logo", it.toString()) }
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        savedInstanceState?.let {
            uriMain = it.getString("uri_main")?.toUri()
            uriLogo = it.getString("uri_logo")?.toUri()
            uriMain?.let { imageViewMain.setImageURI(it) }
            uriLogo?.let { imageViewLogo.setImageURI(it) }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val dateEdit = view.findViewById<TextInputEditText>(R.id.eventDate)

        dateEdit.setOnClickListener {
            val cal = Calendar.getInstance()
            DatePickerDialog(
                requireContext(),
                { _, year, month, day ->
                    // open time picker after date chosen
                    TimePickerDialog(
                        requireContext(),
                        { _, hour, minute ->
                            // ✅ convert to LocalDateTime
                            selectedDateTime = LocalDateTime.of(year, month + 1, day, hour, minute)

                            // show a friendly string for the user
                            dateEdit.setText(
                                selectedDateTime!!.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
                            )
                        },
                        cal.get(Calendar.HOUR_OF_DAY),
                        cal.get(Calendar.MINUTE),
                        true
                    ).show()
                },
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
    }

    companion object {
        fun newInstance(event: Event): EventRegisterScreen {
            val fragment = EventRegisterScreen()
            val args = Bundle()
            args.putParcelable("event_data", event)
            fragment.arguments = args
            return fragment
        }
    }
}
