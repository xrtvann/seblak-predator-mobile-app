package com.irvan.seblakpredator.adapter;

import com.irvan.seblakpredator.R;
import com.irvan.seblakpredator.model.CartItem;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {

    private List<CartItem> itemList;

    public CartAdapter(List<CartItem> itemList) {
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_keranjang, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CartItem item = itemList.get(position);

        // Set data sesuai item_keranjang.xml
        holder.tvCustomerName.setText(item.getCustomerName());
        holder.tvItemPrice.setText(item.getPrice());
        holder.tvItemNote.setText(item.getDescription());

        holder.checkBox.setChecked(item.isSelected());
        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            item.setSelected(isChecked);
        });

        // Hapus individu
        holder.btnDeleteItem.setOnClickListener(v -> {
            int pos = holder.getAdapterPosition();
            if (pos != RecyclerView.NO_POSITION) {
                itemList.remove(pos);
                notifyItemRemoved(pos);
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    // Pilih semua item
    public void selectAll(boolean select) {
        for (CartItem item : itemList) {
            item.setSelected(select);
        }
        notifyDataSetChanged();
    }

    // Hapus semua yang dicentang
    public void removeSelected() {
        Iterator<CartItem> iterator = itemList.iterator();
        while (iterator.hasNext()) {
            if (iterator.next().isSelected()) {
                iterator.remove();
            }
        }
        notifyDataSetChanged();
    }

    // Menambahkan method untuk mengambil item yang terpilih
    public List<CartItem> getSelectedItems() {
        List<CartItem> selectedItems = new ArrayList<>();
        for (CartItem item : itemList) {
            if (item.isSelected()) {  // Cek apakah item dipilih
                selectedItems.add(item);
            }
        }
        return selectedItems;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CheckBox checkBox;
        TextView tvCustomerName, tvItemPrice, tvItemNote;
        ImageView btnDeleteItem;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.checkBoxItem);
            tvCustomerName = itemView.findViewById(R.id.tvCustomerName);
            tvItemPrice = itemView.findViewById(R.id.tvItemPrice);
            tvItemNote = itemView.findViewById(R.id.tvItemNote);
            btnDeleteItem = itemView.findViewById(R.id.btnDeleteItem);
        }
    }
}
