package com.app.adec.model

import java.math.BigDecimal
import java.time.LocalDateTime
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Event(
    var id : Int,
    var artist: String,
    var category: String,
    var title: String,
    var location : String,
    var datetime: LocalDateTime,
    var description: String,
    var logo : Int,
    var image : Int,
    var ticketValue : BigDecimal,
    var deleted: Boolean
) : Parcelable