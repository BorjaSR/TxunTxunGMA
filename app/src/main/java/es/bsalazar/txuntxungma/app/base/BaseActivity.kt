package es.bsalazar.txuntxungma.app.base

import android.os.Bundle
import android.support.design.widget.BaseTransientBottomBar
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.widget.Toast

import butterknife.ButterKnife
import butterknife.Unbinder

abstract class BaseActivity<T : BaseViewModel> : AppCompatActivity() {

    lateinit var viewModel: T
    private lateinit var unbinder: Unbinder

    abstract val view: Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(view)
        unbinder = ButterKnife.bind(this)

        viewModel = setupViewModel()
        observeViewModel()
    }

    override fun onDestroy() {
        super.onDestroy()
        unbinder.unbind()
    }

    abstract fun setupViewModel(): T

    abstract fun observeViewModel()

    fun showSnackbar(msg: String) {
        if (window.currentFocus != null)
            Snackbar.make(window.currentFocus!!, msg, BaseTransientBottomBar.LENGTH_SHORT).show()
    }

    fun getTagFromActualFragment(): String? {
        val fragments = supportFragmentManager.fragments
        return if (fragments.size > 0) fragments[fragments.size - 1].tag else ""
    }

    fun showToast(msg: String) = Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}
