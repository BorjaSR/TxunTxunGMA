package es.bsalazar.txuntxungma.app.base.lists

import android.os.Bundle
import android.view.View
import es.bsalazar.txuntxungma.app.base.BaseFragment
import es.bsalazar.txuntxungma.data.remote.FirebaseResponse
import es.bsalazar.txuntxungma.nonNull
import es.bsalazar.txuntxungma.observe

open abstract class BaseListFragment<T, Q : BaseListViewModel<T>, R : BaseAdapter<T>> : BaseFragment<Q>(){

    val adapter: R by lazy { createAdapter() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel?.let { observeList() }
    }

    abstract fun createAdapter(): R

    private fun observeList(){
        viewModel.listLiveData.nonNull().observe(this) { list -> this.setItems(list) }
        viewModel.addItemLiveData.nonNull().observe(this) { addItemResponse -> addItem(addItemResponse) }
        viewModel.modifyItemLiveData.nonNull().observe(this) { updateItemResponse -> modifyItem(updateItemResponse) }
        viewModel.deleteItemLiveData.nonNull().observe(this) { deleteItemResponse -> deleteItem(deleteItemResponse) }
    }

    open fun setItems(list: List<T>){
        adapter.setItems(list)
    }

    private fun addItem(response: FirebaseResponse<T>) =
            adapter.addItem(response.index, response.response)

    private fun modifyItem(response: FirebaseResponse<T>) =
            adapter.modifyItem(response.index, response.response)

    private fun deleteItem(response: FirebaseResponse<T>) =
            adapter.removeItem(response.index, response.response)

    override fun observeViewModel() {  }
}