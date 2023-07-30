package com.breezegamestudios.amcrbarcodescanner;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {

    private List<Item> items;
    private OnItemClickListener listener;

    public ItemAdapter(List<Item> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_layout, parent, false);
        return new ItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        Item item = items.get(position);
        holder.bind(item, listener);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(Item item);
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder {

        TextView itemName;
        TextView itemSerialNumber;
        TextView itemPartNumber;
        TextView itemRepairOrderNumber;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            itemName = itemView.findViewById(R.id.textViewName);
            itemSerialNumber = itemView.findViewById(R.id.textViewSerialNumber);
            itemPartNumber = itemView.findViewById(R.id.textViewPartNumber);
            itemRepairOrderNumber = itemView.findViewById(R.id.textViewRepairOrderNumber);
        }

        public void bind(final Item item, final OnItemClickListener listener) {
            itemName.setText(item.getName());
            itemSerialNumber.setText("SN: " + item.getSerialNumber());
            itemPartNumber.setText("PN: "+ item.getPartNumber());
            itemRepairOrderNumber.setText("RO: " + item.getRepairOrderNumber());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onItemClick(item);
                    }
                }
            });
        }
    }
}
