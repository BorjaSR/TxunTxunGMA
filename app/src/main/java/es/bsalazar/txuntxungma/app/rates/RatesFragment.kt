package es.bsalazar.txuntxungma.app.rates

import android.annotation.TargetApi
import android.app.AlertDialog
import android.arch.lifecycle.ViewModelProviders
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.transition.Slide
import android.view.*
import android.widget.EditText
import es.bsalazar.txuntxungma.Injector
import es.bsalazar.txuntxungma.R
import es.bsalazar.txuntxungma.app.base.BaseFragment
import es.bsalazar.txuntxungma.app.base.lists.BaseAdapter
import es.bsalazar.txuntxungma.app.base.lists.BaseListFragment
import es.bsalazar.txuntxungma.domain.entities.Auth
import es.bsalazar.txuntxungma.domain.entities.Rate
import es.bsalazar.txuntxungma.data.remote.FirebaseResponse
import es.bsalazar.txuntxungma.nonNull
import es.bsalazar.txuntxungma.observe
import es.bsalazar.txuntxungma.utils.Constants
import es.bsalazar.txuntxungma.utils.ShowState
import kotlinx.android.synthetic.main.fragment_rates.*

class RatesFragment : BaseListFragment<Rate, RatesViewModel, RatesAdapter>(), RatesAdapter.OnEditRate {

    private var scheduleAnim = false
    private var roleId: String = Auth.COMPONENT_ROLE

    override fun provideTag(): String = Constants.RATES_FRAGMENT

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            val slide = Slide()
            slide.slideEdge = Gravity.END

            enterTransition = slide
            reenterTransition = slide
            returnTransition = slide
            exitTransition = slide

            allowEnterTransitionOverlap = false
            allowReturnTransitionOverlap = false
        }

        setHasOptionsMenu(true)
        (activity as AppCompatActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_rates, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecycler()

        viewModel.getRates()
        viewModel.getLoginData()

        rates_swipe.setOnRefreshListener {
            scheduleAnim = true
            viewModel.getRates()
        }
    }

    //region Menu
    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        if (roleId.equals(Auth.CEO_ROLE))
            inflater!!.inflate(R.menu.rates_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId) {
            android.R.id.home -> {
                activity!!.onBackPressed()
                return true
            }
            R.id.action_add -> {
                showAddRate()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }
    //endregion

    override fun createAdapter() = RatesAdapter()

    override fun setupViewModel(): RatesViewModel =
            ViewModelProviders.of(this,
                    Injector.provideRatesViewModelFactory(mContext))
                    .get(RatesViewModel::class.java)


    override fun observeViewModel() {
        viewModel.authLiveData.nonNull().observe(this) { auth -> this.handleAuth(auth) }
        viewModel.loadingProgress.nonNull().observe(this) { showState -> this.handleLoading(showState) }
    }

    private fun initRecycler() {
        rates_recycler.setHasFixedSize(true)
        rates_recycler.layoutManager = LinearLayoutManager(mContext)
        rates_recycler.adapter = adapter
        adapter.onEditRate = this
    }

    override fun setItems(list: List<Rate>) {
        if (scheduleAnim) rates_recycler.scheduleLayoutAnimation()
        scheduleAnim = false
        super.setItems(list)
    }

    private fun handleLoading(showState: ShowState) {
        rates_swipe.isRefreshing = showState == ShowState.SHOW
    }

    private fun handleAuth(auth: Auth) {
        if (auth.roleID.equals(Auth.CEO_ROLE)) {
            roleId = Auth.CEO_ROLE

            val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
                override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                    return false
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    showRemoveConfirmDialog(viewHolder.adapterPosition)
                }
            })

            itemTouchHelper.attachToRecyclerView(rates_recycler)
        }
        setHasOptionsMenu(true)
    }

    override fun onEditRate(rate: Rate) {
        if (roleId.equals(Auth.CEO_ROLE))
            showModifyRate(rate)
    }

    //region Dialogs

    private fun showAddRate() {
        // Create EditText
        val layout = layoutInflater.inflate(R.layout.dialog_view_rate, null)

        AlertDialog.Builder(context)
                .setView(layout)
                .setPositiveButton(getString(R.string.add)) { _, _ ->
                    viewModel.saveRate(Rate(
                            layout.findViewById<EditText>(R.id.edit_rate_name).text.toString(),
                            layout.findViewById<EditText>(R.id.edit_rate_amount).text.toString().toDouble()
                    ))
                }
                .setNegativeButton(getString(R.string.cancel)) { _, _ ->
                    //NOTHING TO DO
                }
                .show()

        layout.findViewById<View>(R.id.edit_rate_name).requestFocus()
    }

    private fun showModifyRate(rate: Rate) {
        // Create EditText
        val layout = layoutInflater.inflate(R.layout.dialog_view_rate, null)
        val description_edit = layout.findViewById<EditText>(R.id.edit_rate_name)
        val amount_edit = layout.findViewById<EditText>(R.id.edit_rate_amount)

        description_edit.setText(rate.description)
        amount_edit.setText(rate.amount.toString())

        AlertDialog.Builder(context)
                .setView(layout)
                .setPositiveButton(getString(R.string.modify)) { _, _ ->
                    rate.description = description_edit.text.toString()
                    rate.amount = amount_edit.text.toString().toDouble()

                    viewModel.modifyRate(rate)
                }
                .setNegativeButton(getString(R.string.cancel)) { _, _ ->
                    //NOTHING TO DO
                }
                .show()

        layout.findViewById<View>(R.id.edit_rate_name).requestFocus()
    }

    private fun showRemoveConfirmDialog(itemPosition: Int) {
        val alertDialog = AlertDialog.Builder(context)
                .setTitle(getString(R.string.remove_confirm_dialog_title))
                .setMessage(getString(R.string.remove_confirm_dialog_message))
                .setPositiveButton(getString(R.string.continue_text)) { _, _ ->
                    adapter.let {
                        viewModel.deleteRate(it.getItem(itemPosition))
                    }
                }
                .setNegativeButton(getString(R.string.cancel)) { _, _ ->
                    adapter.notifyItemChanged(itemPosition)
                }.create()

        alertDialog.setCancelable(false)
        alertDialog.show()
    }

    //endregion
}
