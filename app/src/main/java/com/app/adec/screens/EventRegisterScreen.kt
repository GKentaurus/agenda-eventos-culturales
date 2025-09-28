package com.app.adec.screens

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.activity.result.contract.ActivityResultContracts
import com.app.adec.R
import com.app.adec.data.GLOBALEvents
import com.app.adec.model.Event
import androidx.core.net.toUri
import com.google.android.material.textfield.TextInputEditText
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Calendar

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

    private val pickMainImage = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            uriMain = it
            imageViewMain.setImageURI(it)
        }
    }

    private val pickLogoImage = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            uriLogo = it
            imageViewLogo.setImageURI(it)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.screen_event_register, container, false)

        // Image views
        imageViewMain = view.findViewById(R.id.imageView1)
        imageViewLogo = view.findViewById(R.id.imageView2)

        // Text fields
        artistField = view.findViewById(R.id.artist)
        categoryField = view.findViewById(R.id.category)
        titleField = view.findViewById(R.id.title)
        descriptionField = view.findViewById(R.id.description)
        priceField = view.findViewById(R.id.price)

        // Buttons
        saveButton = view.findViewById(R.id.submit_button)
        cancelButton = view.findViewById(R.id.cancel_button)

        // Launch image pickers when the buttons are clicked
        view.findViewById<Button>(R.id.buttonSelect1).setOnClickListener {
            pickMainImage.launch("image/*")
        }
        view.findViewById<Button>(R.id.buttonSelect2).setOnClickListener {
            pickLogoImage.launch("image/*")
        }

        saveButton.setOnClickListener { onSaveClicked() }
        cancelButton.setOnClickListener { requireActivity().onBackPressedDispatcher.onBackPressed() }

        return view
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
            Toast.makeText(requireContext(),
                "Please select a main image", Toast.LENGTH_SHORT).show()
            valid = false
        }
        if (uriLogo == null) {
            Toast.makeText(requireContext(),
                "Please select a logo image", Toast.LENGTH_SHORT).show()
            valid = false
        }

        return valid
    }


    private fun onSaveClicked() {
        if (!validateInputs()){
            return
        }

        val event = Event(
            id = newID,
            artist = artistField.text.toString().trim(),
            category = categoryField.text.toString().trim(),
            title = titleField.text.toString().trim(),
            location = locationField.text.toString().trim(),
            datetime = selectedDateTime?: throw IllegalStateException("Event date/time is required"),
            description = descriptionField.text.toString().trim(),
            ticketValue = priceField.text.toString().trim().toBigDecimal(),
            imageUri = uriMain!!.toString(),
            logoUri = uriLogo!!.toString(),
        )

        GLOBALEvents.registerEvent(event)

        Toast.makeText(requireContext(), "Event saved", Toast.LENGTH_SHORT).show()
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
            uriMain = it.getString("uri_main")?.let { s -> s.toUri() }
            uriLogo = it.getString("uri_logo")?.let { s -> s.toUri() }
            uriMain?.let { imageViewMain.setImageURI(it) }
            uriLogo?.let { imageViewLogo.setImageURI(it) }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val dateEdit = view.findViewById<TextInputEditText>(R.id.event_date)

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
}
