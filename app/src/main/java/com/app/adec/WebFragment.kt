package com.app.adec

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import androidx.activity.addCallback
import androidx.fragment.app.Fragment

class WebFragment : Fragment() {

    private lateinit var web: WebView
    private lateinit var progress: ProgressBar
    private lateinit var urlBar: EditText
    private lateinit var btnGo: Button

    // URL inicial
    private val startUrl = "https://www.poli.edu.co/eventos/date/2025-09-20~2026-09-20"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_web, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        web = view.findViewById(R.id.web)
        progress = view.findViewById(R.id.progress)
        urlBar = view.findViewById(R.id.urlBar)
        btnGo = view.findViewById(R.id.btnGo)

        // Configuración del WebView
        with(web.settings) {
            javaScriptEnabled = true
            domStorageEnabled = true
            cacheMode = WebSettings.LOAD_DEFAULT
            setSupportZoom(true)
            builtInZoomControls = true
            displayZoomControls = false
            userAgentString = "$userAgentString Mobile Safari/537.36"
        }

        // Mantener navegación interna y actualizar la barra de dirección
        web.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView, url: String) {
                // cuando termina de cargar, actualizamos la barra
                urlBar.setText(url)
            }
        }

        // Progreso de carga
        web.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                progress.visibility = if (newProgress in 1..99) View.VISIBLE else View.GONE
                progress.progress = newProgress
            }
        }

        if (savedInstanceState == null) {
            web.loadUrl(startUrl)
            urlBar.setText(startUrl)
        } else {
            web.restoreState(savedInstanceState)
            // tras restaurar, intenta mostrar la URL actual
            urlBar.setText(web.url ?: startUrl)
        }

        // Acción "Ir" con botón
        btnGo.setOnClickListener { loadFromBar() }

        // Acción "Go/Done" desde el teclado
        urlBar.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_GO || actionId == EditorInfo.IME_ACTION_DONE) {
                loadFromBar()
                true
            } else false
        }

        // Back del sistema: primero historial del WebView
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            if (web.canGoBack()) {
                web.goBack()
            } else {
                isEnabled = false
                requireActivity().onBackPressedDispatcher.onBackPressed()
            }
        }
    }

    private fun loadFromBar() {
        var url = urlBar.text.toString().trim()
        if (url.isNotEmpty()) {
            if (!url.startsWith("http://") && !url.startsWith("https://")) {
                url = "https://$url"
            }
            web.loadUrl(url)
            // ocultar teclado
            val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(urlBar.windowToken, 0)
            urlBar.clearFocus()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        web.saveState(outState)
    }

    override fun onDestroyView() {
        web.stopLoading()
        web.webViewClient = WebViewClient()   // evita null en algunos SDK
        web.webChromeClient = null
        web.destroy()
        super.onDestroyView()
    }
}
