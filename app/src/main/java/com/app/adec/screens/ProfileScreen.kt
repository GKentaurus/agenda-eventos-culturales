package com.app.adec.screens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.app.adec.R

// Este fragmento representa la sección de perfil.
class ProfileScreen : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Infla el layout para este fragmento
        return inflater.inflate(R.layout.screen_profile, container, false)
    }
}