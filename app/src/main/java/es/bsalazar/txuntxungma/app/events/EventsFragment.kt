package es.bsalazar.txuntxungma.app.events

import android.annotation.TargetApi
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.text.TextUtils
import android.transition.Slide
import android.view.*
import android.widget.EditText
import android.widget.TextClock
import android.widget.TextView
import android.widget.TimePicker
import es.bsalazar.txuntxungma.*
import es.bsalazar.txuntxungma.app.base.BaseFragment
import es.bsalazar.txuntxungma.data.remote.FirebaseResponse
import es.bsalazar.txuntxungma.domain.entities.Auth
import es.bsalazar.txuntxungma.domain.entities.Event
import es.bsalazar.txuntxungma.utils.ShowState
import kotlinx.android.synthetic.main.fragment_events.*
import kotlinx.android.synthetic.main.fragment_rates.*
import java.text.SimpleDateFormat
import java.util.*

class EventsFragment : BaseFragment<EventsViewModel>(), EventsAdapter.OnEditEvent {

    private val DISPLAY_HOUR_FORMAT = SimpleDateFormat("HH:mm", Locale.getDefault())
    private val DISPLAY_DAY_FORMAT = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    private val SAVE_DATE_FORMAT = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())

    private var scheduleAnim = false
    private var adapter: EventsAdapter? = null
    private var roleId = Auth.COMPONENT_ROLE

    override fun provideTag(): String = "EVENTS_FRAGMENT"

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
        return inflater.inflate(R.layout.fragment_events, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecycler()
        viewModel.getLoginData()
        viewModel.getEvents()

        events_swipe.setOnRefreshListener {
            scheduleAnim = true
            viewModel.getEvents()
        }
    }


    //region Menu
    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        if (roleId.equals(Auth.CEO_ROLE))
            inflater!!.inflate(R.menu.event_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId) {
            android.R.id.home -> {
                activity!!.onBackPressed()
                return true
            }
            R.id.action_add -> {
                showNewEventDialog()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }
    //endregion

    override fun setupViewModel(): EventsViewModel =
            ViewModelProviders.of(this,
                    Injector.provideEventsViewModelFactory(context))
                    .get(EventsViewModel::class.java)

    override fun observeViewModel() {
        viewModel.loadingProgress.nonNull().observe(this) { auth -> this.handleLoading(auth) }
        viewModel.authLiveData.nonNull().observe(this) { auth -> this.handleAuth(auth) }
        viewModel.eventsLiveData.nonNull().observe(this) { auth -> this.presentEvents(auth) }

        viewModel.addEventLiveData.nonNull().observe(this) { addRateResponse -> addEvent(addRateResponse) }
        viewModel.modifyEventLiveData.nonNull().observe(this) { updateRateResponse -> modifyEvent(updateRateResponse) }
        viewModel.deleteEventLiveData.nonNull().observe(this) { deleteRateResponse -> deleteEvent(deleteRateResponse) }
    }

    private fun initRecycler() {
        events_recycler.setHasFixedSize(true)
        events_recycler.layoutManager = LinearLayoutManager(mContext)
        adapter = EventsAdapter()
        events_recycler.adapter = adapter
        adapter?.onEditEvent = this
    }

    private fun presentEvents(eventList: List<Event>) {
        if (scheduleAnim) events_recycler.scheduleLayoutAnimation()
        scheduleAnim = false
        adapter?.setEvents(eventList)
    }

    private fun handleLoading(showState: ShowState) {
        events_swipe.isRefreshing = showState == ShowState.SHOW
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
            itemTouchHelper.attachToRecyclerView(events_recycler)
        }

        setHasOptionsMenu(true)
    }


    fun addEvent(response: FirebaseResponse<Event>) =
            adapter?.addEvent(response.index, response.response)

    fun modifyEvent(response: FirebaseResponse<Event>) =
            adapter?.modifyEvent(response.index, response.response)

    fun deleteEvent(response: FirebaseResponse<Event>) =
            adapter?.removeEvent(response.index, response.response)


    fun showNewEventDialog() {
        val layout = layoutInflater.inflate(R.layout.dialog_view_event, null)

        val date_edit = layout.findViewById<TextView>(R.id.event_date)
        val hour_edit = layout.findViewById<TextView>(R.id.event_hour)
        val name_edit = layout.findViewById<EditText>(R.id.edit_event_name)
        val description_edit = layout.findViewById<EditText>(R.id.edit_event_description)

        AlertDialog.Builder(context)
                .setView(layout)
                .setPositiveButton(context?.getString(R.string.add)) { _, _ ->
                    if (!TextUtils.isEmpty(date_edit.text.toString()) &&
                            !TextUtils.isEmpty(name_edit.text.toString())) {

                        val date = SAVE_DATE_FORMAT.parse("${date_edit.text} ${hour_edit.text}").time

                        val newEvent = Event(
                                date,
                                name_edit.text.toString(),
                                description_edit.text.toString())

                        viewModel.saveEvent(newEvent)

                    } else
                        showToast(getString(R.string.incomplete_fields))
                }
                .setNegativeButton(context?.getString(R.string.cancel)) { _, _ ->
                    //NOTHING TO DO
                }.show()

        date_edit.setOnClickListener { showDatePicker(date_edit) }

        hour_edit.setOnClickListener {
            showTimePicker(TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                val builder = StringBuilder()
                builder.append(parseTime(hourOfDay)).append(":").append(parseTime(minute))
                hour_edit.text = builder.toString()
            })
        }
    }

    override fun onEditEvent(event: Event) {
        if (roleId == Auth.CEO_ROLE) {
            val layout = layoutInflater.inflate(R.layout.dialog_view_event, null)

            val date_edit = layout.findViewById<TextView>(R.id.event_date)
            val hour_edit = layout.findViewById<TextView>(R.id.event_hour)
            val name_edit = layout.findViewById<EditText>(R.id.edit_event_name)
            val description_edit = layout.findViewById<EditText>(R.id.edit_event_description)

            AlertDialog.Builder(context)
                    .setView(layout)
                    .setPositiveButton(context?.getString(R.string.modify)) { _, _ ->

                        val date = SAVE_DATE_FORMAT.parse("${date_edit.text} ${hour_edit.text}").time

                        val newEvent = Event(
                                date,
                                name_edit.text.toString(),
                                description_edit.text.toString())
                        newEvent.id = event.id

                        if (!newEvent.equals(event))
                            viewModel.modifyEvent(newEvent)
                    }
                    .setNegativeButton(context?.getString(R.string.cancel)) { _, _ ->
                        //NOTHING TO DO
                    }.show()

            event.let {
                date_edit.text = DISPLAY_DAY_FORMAT.format(Date(event.date))
                hour_edit.text = DISPLAY_HOUR_FORMAT.format(Date(event.date))
                name_edit.setText(event.name)
                description_edit.setText(event.description)
            }

            date_edit.setOnClickListener { showDatePicker(date_edit) }

            hour_edit.setOnClickListener {
                showTimePicker(TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                    val builder = StringBuilder()
                    builder.append(parseTime(hourOfDay)).append(":").append(parseTime(minute))
                    hour_edit.text = builder.toString()
                })
            }
        }
    }

    //region Dialogsç

    private fun showRemoveConfirmDialog(itemPosition: Int) {
        val alertDialog = AlertDialog.Builder(context)
                .setTitle(getString(R.string.remove_confirm_dialog_title))
                .setMessage(getString(R.string.remove_confirm_dialog_message))
                .setPositiveButton(getString(R.string.continue_text)) { _, _ ->
                    adapter?.let {
                        viewModel.deleteEvent(it.getItem(itemPosition))
                    }
                }
                .setNegativeButton(getString(R.string.cancel)) { _, _ ->
                    adapter?.notifyItemChanged(itemPosition)
                }.create()

        alertDialog.setCancelable(false)
        alertDialog.show()
    }
    //endregion

    private fun showDatePicker(et: TextView) {
        // Use the current date as the default date in the picker
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        // Create a new instance of DatePickerDialog and return it
        DatePickerDialog(context, { _, i, i1, i2 ->
            val builder = StringBuilder()
            builder.append(parseTime(i2)).append("/").append(parseTime(i1 + 1)).append("/").append(i)
            et.text = builder.toString()
        }, year, month, day)
                .show()
    }


    private fun showTimePicker(listener: TimePickerDialog.OnTimeSetListener) {
        val c = Calendar.getInstance()
        val hour = c.get(Calendar.HOUR_OF_DAY)
        val minute = c.get(Calendar.MINUTE)
        TimePickerDialog(context, listener, hour, minute, true).show()
    }


    private fun parseTime(oldTime: Int): String =
            if (oldTime < 10) "0$oldTime" else oldTime.toString()
}