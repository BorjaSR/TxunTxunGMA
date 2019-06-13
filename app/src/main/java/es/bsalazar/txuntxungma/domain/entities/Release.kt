package es.bsalazar.txuntxungma.domain.entities

import com.google.firebase.firestore.DocumentSnapshot
import java.io.Serializable
import java.util.HashMap

class  Release : Serializable {

    var id: String? = null
    var date: Long = 0
    var title: String? = null
    var description: String? = null
    var hasList: Boolean = false
    var componentList: ArrayList<String> = ArrayList()

    constructor(id: String, document: DocumentSnapshot) {
        val release = document.toObject(this.javaClass)
        this.id = id
        this.description = release.description
        this.date = release.date
        this.title = release.title
        this.hasList = release.hasList
        this.componentList = componentList
    }

    constructor(date: Long, name: String, description: String, hasList: Boolean = false, componentList: ArrayList<String> = ArrayList()) {
        this.description = description
        this.title = name
        this.date = date
        this.hasList = hasList
        this.componentList = componentList
    }

    constructor()

    fun getMap(): HashMap<String, Any?> {
        val map = HashMap<String, Any?>()
        map["date"] = this.date
        map["title"] = this.title
        map["description"] = this.description
        map["hasList"] = this.hasList
        map["componentList"] = this.componentList
        return map
    }

    fun equals(release: Release) : Boolean {
        return release.date == date &&
                release.title.equals(title) &&
                release.description.equals(description) &&
                release.hasList == hasList
    }
}