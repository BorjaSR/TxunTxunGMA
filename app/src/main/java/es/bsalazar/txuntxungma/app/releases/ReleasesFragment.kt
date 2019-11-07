package es.bsalazar.txuntxungma.app.releases

import android.annotation.TargetApi
import android.app.AlertDialog
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.util.Pair
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.text.TextUtils
import android.transition.Slide
import android.view.*
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import es.bsalazar.txuntxungma.R
import es.bsalazar.txuntxungma.Injector
import es.bsalazar.txuntxungma.app.MainActivity
import es.bsalazar.txuntxungma.app.base.BaseFragment
import es.bsalazar.txuntxungma.app.releases.detail.ReleaseDetailActivity
import es.bsalazar.txuntxungma.data.remote.FirebaseResponse
import es.bsalazar.txuntxungma.domain.entities.Auth
import es.bsalazar.txuntxungma.domain.entities.Release
import es.bsalazar.txuntxungma.nonNull
import es.bsalazar.txuntxungma.observe
import es.bsalazar.txuntxungma.utils.Constants
import es.bsalazar.txuntxungma.utils.ShowState
import kotlinx.android.synthetic.main.fragment_releases.*

class ReleasesFragment : BaseFragment<ReleasesViewModel>(), ReleasesAdapter.OnEditRelease {

    private var scheduleAnim = false
    private var adapter: ReleasesAdapter? = null
    private var roleId = Auth.COMPONENT_ROLE

    override fun provideTag(): String = "RELEASES_FRAGMENT"

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
        return inflater.inflate(R.layout.fragment_releases, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecycler()
        viewModel.getLoginData()
        viewModel.getReleases()

        releases_swipe.setOnRefreshListener {
            scheduleAnim = true
            viewModel.getReleases()
        }
    }


    //region Menu
    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        if (roleId.equals(Auth.CEO_ROLE))
            inflater!!.inflate(R.menu.releases_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId) {
            android.R.id.home -> {
                activity!!.onBackPressed()
                return true
            }
            R.id.action_add -> {
                showNewReleaseDialog()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }
    //endregion

    override fun setupViewModel(): ReleasesViewModel =
            ViewModelProviders.of(this,
                    Injector.provideReleasesViewModelFactory(context))
                    .get(ReleasesViewModel::class.java)

    override fun observeViewModel() {
        viewModel.authLiveData.nonNull().observe(this, ::handleAuth)
        viewModel.releasesLiveData.nonNull().observe(this, ::presentReleases)
        viewModel.loadingProgress.nonNull().observe(this, ::handleLoading)

        viewModel.addReleaseLiveData.nonNull().observe(this) { addReleaseResponse -> addRelease(addReleaseResponse) }
        viewModel.modifyReleaseLiveData.nonNull().observe(this) { updateReleaseResponse -> modifyRelease(updateReleaseResponse) }
        viewModel.deleteReleaseLiveData.nonNull().observe(this) { deleteReleaseResponse -> deleteRelease(deleteReleaseResponse) }
    }

    private fun initRecycler() {
        releases_recycler.setHasFixedSize(true)
        releases_recycler.layoutManager = LinearLayoutManager(mContext)
        adapter = ReleasesAdapter()
        releases_recycler.adapter = adapter
        adapter?.onEditRelease = this
    }

    private fun presentReleases(releaseList: List<Release>) {
        if (scheduleAnim) releases_recycler.scheduleLayoutAnimation()
        scheduleAnim = false
        adapter?.setReleases(releaseList)
    }

    private fun handleLoading(showState: ShowState) {
        releases_swipe.isRefreshing = showState == ShowState.SHOW
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
            itemTouchHelper.attachToRecyclerView(releases_recycler)
        }

        setHasOptionsMenu(true)
    }

    fun addRelease(response: FirebaseResponse<Release>) =
            adapter?.addRelease(response.index, response.response)

    fun modifyRelease(response: FirebaseResponse<Release>) =
            adapter?.modifyRelease(response.index, response.response)

    fun deleteRelease(response: FirebaseResponse<Release>) =
            adapter?.removeRelease(response.index, response.response)

    //region Release Adapter implementation
    override fun onEditRelease(release: Release) {
        showEditReleaseDialog(release)
    }

    override fun onViewReleaseDetail(release: Release, view: View) {
        animateIntent(release, view)
    }

    override fun onSignUp(release: Release) {
        // Create EditText
        val layout = layoutInflater.inflate(R.layout.simple_edit_text, null)
        val editText = layout.findViewById<EditText>(R.id.edit_text)
        editText.hint = "Nombre"

        AlertDialog.Builder(context)
                .setMessage(getString(R.string.msg_sign_up_release))
                .setView(layout)
                .setPositiveButton(getString(R.string.action_sign_up)) { _, _ ->
                    release.componentList.add(editText.text.toString())
                    viewModel.modifyRelease(release)
                }
                .setNegativeButton(getString(R.string.cancel), null)
                .show()

        layout.findViewById<EditText>(R.id.edit_text).requestFocus()
    }

    //endregion

    private fun showRemoveConfirmDialog(itemPosition: Int) {
        val alertDialog = AlertDialog.Builder(context)
                .setTitle(getString(R.string.remove_confirm_dialog_title))
                .setMessage(getString(R.string.remove_confirm_dialog_message))
                .setPositiveButton(getString(R.string.continue_text)) { _, _ ->
                    adapter?.let {
                        viewModel.deleteRelease(it.getItem(itemPosition))
                    }
                }
                .setNegativeButton(getString(R.string.cancel)) { _, _ ->
                    adapter?.notifyItemChanged(itemPosition)
                }.create()

        alertDialog.setCancelable(false)
        alertDialog.show()
    }

    private fun showNewReleaseDialog() {

        val layout = layoutInflater.inflate(R.layout.dialog_view_release, null)

        val title = layout.findViewById<TextView>(R.id.release_title)
        val text = layout.findViewById<TextView>(R.id.release_text)
        val checkBoxComponentsList = layout.findViewById<CheckBox>(R.id.check_include_list)

        AlertDialog.Builder(context)
                .setView(layout)
                .setPositiveButton(context?.getString(R.string.add)) { _, _ ->
                    if (!TextUtils.isEmpty(title.text.toString()) &&
                            !TextUtils.isEmpty(text.text.toString())) {

                        val newRelease = Release(
                                System.currentTimeMillis(),
                                title.text.toString(),
                                text.text.toString(),
                                checkBoxComponentsList.isChecked)

                        viewModel.saveRelease(newRelease)

                    } else
                        showToast(getString(R.string.incomplete_fields))
                }
                .setNegativeButton(context?.getString(R.string.cancel)) { _, _ ->
                    //NOTHING TO DO
                }.show()
    }

    private fun showEditReleaseDialog(release: Release) {

        val layout = layoutInflater.inflate(R.layout.dialog_view_release, null)

        val title = layout.findViewById<TextView>(R.id.release_title)
        val text = layout.findViewById<TextView>(R.id.release_text)

        AlertDialog.Builder(context)
                .setView(layout)
                .setPositiveButton(context?.getString(R.string.modify)) { _, _ ->
                    if (!TextUtils.isEmpty(title.text.toString()) &&
                            !TextUtils.isEmpty(text.text.toString())) {

                        release.title = title.text.toString()
                        release.description = text.text.toString()

                        viewModel.modifyRelease(release)

                    } else
                        showToast(getString(R.string.incomplete_fields))
                }
                .setNegativeButton(context?.getString(R.string.cancel)) { _, _ ->
                    //NOTHING TO DO
                }.show()

        title.text = release.title
        text.text = release.description
    }


    fun animateIntent(release: Release, vararg sharedViews: View) {

        // Ordinary Intent for launching a new activity
        val intent = Intent(activity, ReleaseDetailActivity::class.java)
        intent.putExtra(Constants.EXTRA_KEY_RELEASE, release)

        val multipleShared = ActivityOptionsCompat.makeSceneTransitionAnimation(activity as MainActivity,
                Pair.create(sharedViews[0], getString(R.string.transitionName_cardBackground))
        )

        //Start the Intent
        ActivityCompat.startActivity(activity as MainActivity, intent, multipleShared.toBundle())
    }
}