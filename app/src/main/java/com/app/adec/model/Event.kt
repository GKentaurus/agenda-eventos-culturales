package com.app.adec.model

import java.math.BigDecimal
import java.time.LocalDateTime
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Event(
    val id : Int,
    val artist: String,
    val category: String,
    val title: String,
    val location : String,
    val datetime: LocalDateTime,
    val description: String,
    val logo : Int,
    val image : Int,
    val ticketValue : BigDecimal
) : Parcelable