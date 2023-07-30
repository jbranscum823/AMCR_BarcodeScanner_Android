package com.breezegamestudios.amcrbarcodescanner;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {

    private List<Item> items;
    private List<Item> filteredItems; // Filtered items

    private OnItemClickListener listener;

    public ItemAdapter(List<Item> items) {
        this.items = items;
        this.filteredItems = new ArrayList<>(items); // Initialize filteredItems here
    }

    public void filter(String text) {
        filteredItems.clear();
        if(text.isEmpty()){
            filteredItems.addAll(items);
        } else{
            text = text.toLowerCase();
            for(Item item: items){
                if((item.getName() != null && item.getName().toLowerCase().contains(text)) ||
                        (item.getSerialNumber() != null && item.getSerialNumber().toLowerCase().contains(text)) ||
                        (item.getPartNumber() != null && item.getPartNumber().toLowerCase().contains(text)) ||
                        (item.getRepairOrderNumber() != null && item.getRepairOrderNumber().toLowerCase().contains(text))){
                    filteredItems.add(item);
                }
            }

        }
        Log.d("ItemAdapter", "Filtered items: " + filteredItems); // Add this line
        notifyDataSetChanged(); // Notify the adapter that the data set has changed
    }

    public void setItems(List<Item> items) {
        this.items = items;
        this.filteredItems = new ArrayList<>(items);
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
        Item item = filteredItems.get(position); // Use filteredItems here
        holder.bind(item, listener);
    }

    @Override
    public int getItemCount() {
        return filteredItems.size(); // Use filteredItems here
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

