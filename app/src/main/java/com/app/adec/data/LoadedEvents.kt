package com.app.adec.data

import com.app.adec.R
import com.app.adec.model.Event
import java.math.BigDecimal
import java.time.LocalDateTime

class LoadedEvents {
    private val events: ArrayList<Event> = ArrayList<Event>(3)

    constructor() : super() {
        this.events.addAll(
            listOf(
                Event(
                    id = 0,
                    artist = "Imagine Dragons",
                    category = "CONCIERTO",
                    title = "Loom World Tour",
                    location = "Bogotá, Cundinamarca",
                    datetime = LocalDateTime.of(2025, 10, 17, 17, 0, 0),
                    description = """«The LOOM World Tour» es la quinta gira mundial de conciertos en
                    curso de la banda estadounidense de pop rock Imagine Dragons, creada para apoyar
                    y promocionar el lanzamiento de su sexto álbum de estudio, «LOOM».""",
                    logoResId = R.drawable.logo_0001_imagine_dragons,
                    imageResId = R.drawable.main_0001_imagine_dragons,
                    ticketValue = BigDecimal(300000)
                ),
                Event(
                    id = 1,
                    artist = "Jorge Duque Linares",
                    category = "CONFERENCIA",
                    title = "Cómo ser carismático sin morir en el intento",
                    location = "Bogotá, Cundinamarca",
                    datetime = LocalDateTime.now().plusDays(30).minusHours(2),
                    description = """Aprenda a como captar la atención de las personas y dar a conocer
                    |su idea, con herramientas psicosociales demostradas.""".trimMargin(),
                    logoResId = R.drawable.logo_0002_jorge_duque_linares,
                    imageResId = R.drawable.main_0002_jorge_duque_linares,
                    ticketValue = BigDecimal(150000),
                ),
                Event(
                    id = 2,
                    artist = "Linkin Park",
                    category = "CONCIERTO",
                    title = "From Zero World Tour",
                    location = "Bogotá, Cundinamarca",
                    datetime = LocalDateTime.of(2025, 10, 25, 19, 0, 0),
                    description = """From Zero World Tour es una gira de conciertos en curso de la banda
                    |de rock estadounidense Linkin Park, en apoyo de su octavo álbum de estudio From
                    |Zero, que fue publicado el 15 de noviembre de 2024. La gira fue anunciada el 5
                    |de septiembre de 2024, tras el lanzamiento del primer sencillo del álbum,
                    |«The Emptiness Machine».""".trimMargin(),
                    logoResId = R.drawable.logo_0003_linkin_park,
                    imageResId = R.drawable.main_0003_linkin_park,
                    ticketValue = BigDecimal(370000)
                ),
            )
        )
    }

    fun registerEvent(event: Event) {
        this.events.add(event)
    }

    fun getEvents(showDeleted: Boolean = false): List<Event> {
        return this.events.filter { !it.deleted or showDeleted }.toList()
    }

    fun editEvent(event: Event) {
        this.events[event.id] = event
    }

    fun deleteEvent(eventId: Int) {
        this.events[eventId].deleted = true
    }

    fun nextEventID(): Int {
        return this.events.last().id + 1
    }
}

val GLOBALEvents = LoadedEvents()