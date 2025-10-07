package com.app.adec.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.math.BigDecimal
import java.time.LocalDateTime

@Parcelize
data class Event(
    val id: Int,
    val artist: String,
    val category: String,
    val title: String,
    val location: String,
    val datetime: LocalDateTime,
    val description: String,
    var logoUri: String? = null,
    var logoResId: Int? = null,
    var imageUri: String? = null,
    var imageResId: Int? = null,
    var ticketValue: BigDecimal,
    var deleted: Boolean = false
) : Parcelable