package es.bsalazar.txuntxungma.app.releases

import android.arch.lifecycle.MutableLiveData
import es.bsalazar.txuntxungma.app.base.BaseViewModel
import es.bsalazar.txuntxungma.data.DataSource
import es.bsalazar.txuntxungma.data.remote.FirebaseResponse
import es.bsalazar.txuntxungma.data.remote.FirestoreSource
import es.bsalazar.txuntxungma.domain.entities.*
import es.bsalazar.txuntxungma.utils.ShowState

class ReleasesViewModel(dataSource: DataSource) : BaseViewModel(dataSource) {

    val authLiveData = object : MutableLiveData<Auth>() {}
    val releasesLiveData = object : MutableLiveData<List<Release>>() {}


    val addReleaseLiveData = object : MutableLiveData<FirebaseResponse<Release>>() {}
    val modifyReleaseLiveData = object : MutableLiveData<FirebaseResponse<Release>>() {}
    val deleteReleaseLiveData = object : MutableLiveData<FirebaseResponse<Release>>() {}

    fun getLoginData() {
        dataSource.getLoginData(object : DataCallback<Auth, BaseError> {
            override fun onSuccess(response: Auth?) {
                authLiveData.value = response
            }

            override fun onFailure(errorBase: BaseError?) {
                authLiveData.value = null
            }
        })
    }

    fun getReleases(){
        var init = true
        loadingProgress.value = ShowState.SHOW
        dataSource.getReleases(object : FirestoreSource.OnCollectionChangedListener<Release>{
            override fun onCollectionChange(collection: MutableList<Release>?) {
                if(init){
                    releasesLiveData.value = collection
                    loadingProgress.value = ShowState.HIDE
                    init = false
                }
            }

            override fun onDocumentAdded(index: Int, document: Release) {
                if(!init) addReleaseLiveData.value = FirebaseResponse(index, document)
            }

            override fun onDocumentChanged(index: Int, document: Release) {
                if(!init) modifyReleaseLiveData.value = FirebaseResponse(index, document)
            }

            override fun onDocumentRemoved(index: Int, document: Release) {
                if(!init) deleteReleaseLiveData.value = FirebaseResponse(index, document)
            }
        })
    }

    fun saveRelease(release: Release) = dataSource.saveRelease(release) {}

    fun modifyRelease(release: Release) = dataSource.updateRelease(release) {}

    fun deleteRelease(release: Release) = dataSource.deleteRelease(release.id)
}