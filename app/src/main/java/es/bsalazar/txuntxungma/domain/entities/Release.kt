package es.bsalazar.txuntxungma.domain.entities

import com.google.firebase.firestore.DocumentSnapshot
import java.io.Serializable
import java.util.HashMap

class Release : Serializable {

    var id: String? = null
    var date: Long = 0
    var title: String? = null
    var description: String? = null

    constructor(id: String, document: DocumentSnapshot) {
        val event = document.toObject(this.javaClass)
        this.id = id
        this.description = event.description
        this.date = event.date
        this.title = event.title
    }

    constructor()

    constructor(date: Long, name: String, description: String) {
        this.description = description
        this.title = name
        this.date = date
    }

    fun getMap(): HashMap<String, Any?> {
        val map = HashMap<String, Any?>()
        map["date"] = this.date
        map["title"] = this.title
        map["description"] = this.description
        return map
    }

    fun equals(release: Release) : Boolean {
        return release.date == date &&
                release.title.equals(title) &&
                release.description.equals(description)
    }
}