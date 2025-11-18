package com.adr57.netdemo.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.adr57.netdemo.R;
import com.adr57.netdemo.model.Product;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {

    private ArrayList<Product> products;

    private onItemClickListener listener;

    public interface onItemClickListener {
        void onItemClick(Product product);
    }


    public ProductAdapter(ArrayList<Product> products, onItemClickListener listener) {
        this.products = products != null ? products : new ArrayList<>();
        this.listener = listener;
    }

    @NonNull
    @Override
    public ProductAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ProductAdapter.ViewHolder holder, int position) {
        Product product = products.get(position);
        holder.tvName.setText(product.getName());
        holder.tvDescription.setText(product.getDescription());
        holder.tvPrice.setText("$"+product.getPrice());
        Glide.with(holder.itemView.getContext())
                .load(product.getProduct_image())
                .into(holder.imgAvatar);

        holder.itemView.setOnClickListener(v -> {
          if (listener != null){
              listener.onItemClick(product);
          }
        });
    }

    public void setProducts(ArrayList<Product> products){
        this.products = products;
    }

    @Override
    public int getItemCount() {
        return products!=null ? products.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView imgAvatar;
        private TextView tvName, tvDescription, tvPrice;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgAvatar = itemView.findViewById(R.id.imgAvatar);
            tvName = itemView.findViewById(R.id.tvName);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            tvPrice = itemView.findViewById(R.id.tvPrice);
        }
    }
}
