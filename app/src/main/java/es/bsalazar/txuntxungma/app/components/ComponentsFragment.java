package es.bsalazar.txuntxungma.app.components;

import android.app.AlertDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.transition.Slide;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import es.bsalazar.txuntxungma.Injector;
import es.bsalazar.txuntxungma.R;
import es.bsalazar.txuntxungma.app.MainActivity;
import es.bsalazar.txuntxungma.app.base.BaseFragment;
import es.bsalazar.txuntxungma.data.remote.FirebaseResponse;
import es.bsalazar.txuntxungma.domain.entities.Auth;
import es.bsalazar.txuntxungma.domain.entities.Component;
import es.bsalazar.txuntxungma.utils.Constants;
import es.bsalazar.txuntxungma.utils.ShowState;

public class ComponentsFragment extends BaseFragment<ComponentsViewModel> implements SwipeRefreshLayout.OnRefreshListener, ComponentsAdapter.ComponentEditListener {

    @BindView(R.id.components_swipe)
    SwipeRefreshLayout swipe;
    @BindView(R.id.components_recycler)
    RecyclerView recyclerView;
    @BindView(R.id.empty_list)
    TextView empty;

    private boolean scheduleAnim = false;
    private ComponentsAdapter adapter;
    private String roleId = Auth.COMPONENT_ROLE;

    @Override
    public String provideTag() {
        return Constants.COMPONENTS_FRAGMENT;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Slide slide = new Slide();
            slide.setSlideEdge(Gravity.END);

            setEnterTransition(slide);
            setReenterTransition(slide);
            setReturnTransition(slide);
            setExitTransition(slide);

            setAllowEnterTransitionOverlap(false);
            setAllowReturnTransitionOverlap(false);
        }


        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_components, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initRecycler();
        swipe.setOnRefreshListener(this);
        viewModel.setDataSource(Injector.provideDataProvider(getContext()));

        viewModel.getComponentsFromDataSource();
        viewModel.getAuthData();
    }

    //refion Menu
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (roleId.equals(Auth.CEO_ROLE))
            inflater.inflate(R.menu.add_component_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getActivity().onBackPressed();
                return true;
            case R.id.action_add:
                showAddComponentDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    //endregion

    @Override
    public ComponentsViewModel setupViewModel() {
        return ViewModelProviders.of(this,
                Injector.provideComponentsViewModelFactory(getContext()))
                .get(ComponentsViewModel.class);
    }

    @Override
    public void observeViewModel() {
        viewModel.getEmptyList().observe(this, this::handleEmptyList);
        viewModel.getLoadingProgress().observe(this, this::handleLoading);
        viewModel.getComponents().observe(this, this::presentComponentsList);
        viewModel.getAuth().observe(this, this::handleAuth);

        viewModel.getAddComponentResponse().observe(this, this::addComponent);
        viewModel.getModifyComponentResponse().observe(this, this::modifyComponent);
        viewModel.getRemoveComponentResponse().observe(this, this::removeComponent);
    }

    private void handleEmptyList(ShowState showState) {
        empty.setVisibility(showState == ShowState.SHOW ? View.VISIBLE : View.GONE);
    }

    private void handleLoading(ShowState showState) {
        swipe.setRefreshing(showState == ShowState.SHOW);
    }

    private void initRecycler() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        adapter = new ComponentsAdapter();
        recyclerView.setAdapter(adapter);
        adapter.setComponentEditListener(this);
    }

    private void handleAuth(Auth auth) {
        if (auth != null && auth.getRoleID().equals(Auth.CEO_ROLE)) {
            roleId = Auth.CEO_ROLE;

            ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
                @Override
                public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                    return false;
                }

                @Override
                public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                    if (viewHolder.getAdapterPosition() == 0)
                        adapter.notifyItemChanged(0);
                    else
                        showRemoveConfirmDialog(viewHolder.getAdapterPosition());
                }
            });

            itemTouchHelper.attachToRecyclerView(recyclerView);
        }
        setHasOptionsMenu(true);
    }

    private void presentComponentsList(List<Component> components) {

        if (scheduleAnim) recyclerView.scheduleLayoutAnimation();
        scheduleAnim = false;
        adapter.setComponents(components);
    }

    private void addComponent(FirebaseResponse<Component> response) {
        adapter.addComponent(response.getIndex(), response.getResponse());
    }

    private void modifyComponent(FirebaseResponse<Component> response) {
        adapter.modifyComponent(response.getIndex(), response.getResponse());
    }

    private void removeComponent(FirebaseResponse<Component> response) {
        adapter.removeComponent(response.getIndex(), response.getResponse());
    }

    @Override
    public void onRefresh() {
        scheduleAnim = true;
        viewModel.getComponentsFromDataSource();
    }

    @Override
    public void onEditComponent(Component component) {
        if (roleId.equals(Auth.CEO_ROLE))
            showModifyComponentDialog(component);
    }

    //region Dialogs

    private void showAddComponentDialog() {
        // Create EditText
        View layout = getLayoutInflater().inflate(R.layout.simple_edit_text, null);

        new AlertDialog.Builder(getContext())
                .setMessage(getString(R.string.msg_new_component))
                .setView(layout)
                .setPositiveButton(getString(R.string.action_add), (dialog, whichButton) -> {
                    viewModel.saveComponent(((EditText) layout.findViewById(R.id.edit_text)).getText().toString());
                })
                .setNegativeButton(getString(R.string.cancel), (dialog, whichButton) -> {
                    //NOTHING TO DO
                })
                .show();

        layout.findViewById(R.id.edit_text).requestFocus();
    }

    private void showModifyComponentDialog(Component component) {
        // Create EditText
        View layout = getLayoutInflater().inflate(R.layout.simple_edit_text, null);

        new AlertDialog.Builder(getContext())
                .setMessage(getString(R.string.msg_modify_component))
                .setView(layout)
                .setPositiveButton(getString(R.string.acccept), (dialog, whichButton) -> {
                    viewModel.updateComponent(new Component(component.getId(),
                            ((EditText) layout.findViewById(R.id.edit_text)).getText().toString()));
                })
                .setNegativeButton(getString(R.string.cancel), (dialog, whichButton) -> {
                    //NOTHING TO DO
                })
                .show();

        layout.findViewById(R.id.edit_text).requestFocus();
        ((EditText) layout.findViewById(R.id.edit_text)).setText(component.getName());
    }

    private void showRemoveConfirmDialog(int itemPosition) {
        final AlertDialog alertDialog = new AlertDialog.Builder(getContext())
                .setTitle(getString(R.string.remove_confirm_dialog_title))
                .setMessage(getString(R.string.remove_confirm_dialog_message))
                .setPositiveButton(getString(R.string.continue_text), (dialogInterface, i) -> {
                    viewModel.deleteComponent(adapter.getItem(itemPosition).getId());
                })
                .setNegativeButton(getString(R.string.cancel), (dialogInterface, i) -> {
                    adapter.notifyItemChanged(itemPosition);
                }).create();

        alertDialog.setCancelable(false);
        alertDialog.show();
    }
    //endregion
}
