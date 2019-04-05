package es.bsalazar.txuntxungma.domain.entities

import com.google.firebase.firestore.DocumentSnapshot
import java.io.Serializable
import java.util.HashMap

class Event() : Serializable {

    var id: String? = null
    var date: Long = 0
    var name: String? = null
    var description: String? = null
    var alarmActivated: Boolean = false
    var compacted = true;

    constructor(id: String, document: DocumentSnapshot) : this() {
        val event = document.toObject(this.javaClass)
        this.id = id
        this.description = event.description
        this.date = event.date
        this.name = event.name
    }

    constructor(date: Long, name: String, description: String) : this() {
        this.description = description
        this.name = name
        this.date = date
    }

    fun getMap(): HashMap<String, Any?>{
        val map = HashMap<String, Any?>()
        map["date"] = this.date
        map["name"] = this.name
        map["description"] = this.description
        return map
    }

    fun equals(event: Event) : Boolean {
        return event.date == date &&
                event.name.equals(name) &&
                event.description.equals(description)
    }
}