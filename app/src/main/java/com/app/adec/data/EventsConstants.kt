package com.app.adec.data

import com.app.adec.R
import com.app.adec.model.Event
import java.math.BigDecimal
import java.time.LocalDateTime

class EventsConstants {
    val EVENTS : List<Event> = listOf(
        Event(
            1,
            "Imagine Dragons",
            "Concierto",
            "Loom World Tour",
            "Bogotá, Cundinamarca",
            LocalDateTime.of(2025,10,17,17,0,0),
            "«The LOOM World Tour» es la quinta gira mundial de conciertos en curso de la banda estadounidense de pop rock Imagine Dragons, creada para apoyar y promocionar el lanzamiento de su sexto álbum de estudio, «LOOM».",
            R.drawable.logo_0001_imagine_dragons,
            R.drawable.main_0001_imagine_dragons,
            BigDecimal(300000)
        ),
        Event(
            2,
            "Jorge Duque Linares",
            "Conferencia",
            "Cómo ser carismático sin morir en el intento",
            "Bogotá, Cundinamarca",
            LocalDateTime.now().plusDays(30).minusHours(2),
            "Aprenda a como captar la atención de las personas y dar a conocer su idea, con herramientas psicosociales demostradas.",
            R.drawable.logo_0002_jorge_duque_linares,
            R.drawable.main_0002_jorge_duque_linares,
            BigDecimal(150000)
        ),
        Event(
            3,
            "Linkin Park",
            "Concierto",
            "From Zero World Tour",
            "Bogotá, Cundinamarca",
            LocalDateTime.of(2025,10,25,19,0,0),
            "From Zero World Tour es una gira de conciertos en curso de la banda de rock estadounidense Linkin Park, en apoyo de su octavo álbum de estudio From Zero, que fue publicado el 15 de noviembre de 2024. La gira fue anunciada el 5 de septiembre de 2024, tras el lanzamiento del primer sencillo del álbum, «The Emptiness Machine».",
            R.drawable.logo_0003_linkin_park,
            R.drawable.main_0003_linkin_park,
            BigDecimal(370000)
        ),
    )
}