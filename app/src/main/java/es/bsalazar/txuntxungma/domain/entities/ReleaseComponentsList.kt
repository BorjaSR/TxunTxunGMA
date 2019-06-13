package es.bsalazar.txuntxungma.domain.entities

import com.google.firebase.firestore.DocumentSnapshot
import java.util.HashMap

class ReleaseComponentsList {

    var id: String? = null
    var releaseId: String = ""
    var componentsList = ArrayList<String>()

    constructor(id: String, document: DocumentSnapshot) {
        val releaseComponentsList = document.toObject(this.javaClass)
        this.id = id
        this.releaseId = releaseComponentsList.releaseId
        this.componentsList = releaseComponentsList.componentsList
    }

    constructor(releaseId: String, componentsList: ArrayList<String> = ArrayList()) {
        this.releaseId = releaseId
        this.componentsList = componentsList
    }

    fun getMap(): HashMap<String, Any?> {
        val map = HashMap<String, Any?>()
        map["releaseId"] = this.releaseId
        map["componentsList"] = this.componentsList
        return map
    }

}