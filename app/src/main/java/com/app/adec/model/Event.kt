package com.app.adec.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.math.BigDecimal
import java.time.LocalDateTime

@Parcelize
data class Event(
    var id: Int,
    var artist: String,
    var category: String,
    var title: String,
    var location: String,
    var datetime: LocalDateTime,
    var description: String,
    var logoUri: String? = null,
    var imageUri: String? = null,
    var ticketValue: BigDecimal,
    var deleted: Boolean = false
) : Parcelable