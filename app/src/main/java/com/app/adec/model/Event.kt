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
    var logoUri: String? = null,
    var logoResId: Int? = null,
    var imageUri: String? = null,
    var imageResId: Int? = null,
    var ticketValue: BigDecimal,
    var deleted: Boolean = false
) : Parcelable