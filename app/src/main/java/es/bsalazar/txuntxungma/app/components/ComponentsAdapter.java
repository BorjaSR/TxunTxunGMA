package es.bsalazar.txuntxungma.app.components;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.bsalazar.txuntxungma.R;
import es.bsalazar.txuntxungma.domain.entities.Component;

public class ComponentsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int HEADER_VIEW_TYPE = 0;
    private final int COMPONENT_VIEW_TYPE = 1;

    public interface ComponentEditListener {
        void onEditComponent(Component component);
    }

    private List<Component> components = new ArrayList<>();
    private ComponentEditListener componentEditListener;

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == HEADER_VIEW_TYPE) {
            View v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.header_component_list, parent, false);
            return new HeaderItemViewHolder(v);
        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.item_component, parent, false);
            return new ComponentItemViewHolder(v);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ComponentItemViewHolder) {
            ((ComponentItemViewHolder) holder).component_name.setText(components.get(holder.getAdapterPosition()).getName());

            ((ComponentItemViewHolder) holder).container.setOnLongClickListener(v -> {
                if (componentEditListener != null){
                    componentEditListener.onEditComponent(components.get(holder.getAdapterPosition()));
                    return true;
                }
                return false;
            });
        }
    }

    @Override
    public int getItemCount() {
        return components.size();
    }

    @Override
    public int getItemViewType(int position) {
        return components.get(position).getName() == null ? HEADER_VIEW_TYPE : COMPONENT_VIEW_TYPE;
    }

    void setComponents(List<Component> components) {
        this.components = components;
        this.components.add(0, new Component());
        notifyDataSetChanged();
    }

    void addComponent(int index, Component component) {
        index++;
        if (!containComponent(component)) {
            components.add(index, component);
            notifyItemInserted(index);
        }
    }

    void modifyComponent(int index, Component component) {
        index++;
        if (index < components.size()) {
            components.set(index, component);
            notifyItemChanged(index);
        }
    }

    void removeComponent(int index, Component component) {
        index++;
        if (index < components.size() && containComponent(component)) {
            components.remove(index);
            notifyItemRemoved(index);
        }
    }

    private boolean containComponent(Component component) {
        for (Component component1 : components)
            if (component.getId().equals(component1.getId()))
                return true;
        return false;
    }

    Component getItem(int position) {
        return components.get(position);
    }

    void setComponentEditListener(ComponentEditListener componentEditListener) {
        this.componentEditListener = componentEditListener;
    }

    /**
     * ViewHolder representation of a Component
     */
    class ComponentItemViewHolder extends RecyclerView.ViewHolder {

        //region Views
        @BindView(R.id.component_container)
        LinearLayout container;
        @BindView(R.id.component_name)
        TextView component_name;
        //endregion

        ComponentItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    /**
     * ViewHolder representation of a Header
     */
    class HeaderItemViewHolder extends RecyclerView.ViewHolder {

        HeaderItemViewHolder(View itemView) {
            super(itemView);
        }
    }
}
