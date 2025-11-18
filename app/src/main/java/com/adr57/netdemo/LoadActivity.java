package com.adr57.netdemo;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.adr57.netdemo.adapter.ProductAdapter;
import com.adr57.netdemo.model.Product;
import com.adr57.netdemo.network.ApiCallback;
import com.adr57.netdemo.network.ApiRepository;
import com.adr57.netdemo.storage.database.AppDatabase;

import java.util.ArrayList;
import java.util.List;

public class LoadActivity extends AppCompatActivity implements ProductAdapter.onItemClickListener {

    private Toolbar toolbar;
    private RecyclerView recyclerViewProducts;
    private ProductAdapter productAdapter;
    private ArrayList<Product> products;
    private ApiRepository apiRepository;
    private AppDatabase database;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_load);
        mapping();
        setupToolbar();
        setupDatabaseAndApi();
        setupView();
        getProducts();

    }

    private void setupView() {
        recyclerViewProducts.setLayoutManager(new LinearLayoutManager(this));
        products = new ArrayList<>();
        productAdapter = new ProductAdapter(products, this);
        recyclerViewProducts.setAdapter(productAdapter);
    }

    private void setupDatabaseAndApi() {
        database = AppDatabase.getDatabase(this);
        apiRepository = new ApiRepository();
    }

    private void getProducts(){
        apiRepository.getProducts(new ApiCallback<List<Product>>() {
            @Override
            public void onSuccess(List<Product> result) {
                products.clear();
                products.addAll(result);
                productAdapter.notifyDataSetChanged();
                new Handler().postDelayed(() -> {
                    com.adr57.netdemo.storage.database.entities.Product[] parr =
                            result.stream().map(product -> product.toEntity())
                                    .toArray(com.adr57.netdemo.storage.database.entities.Product[]::new);
                    database.databaseWriteExecutor.execute(() -> {
                        database.productDao().insertAllProduct(parr);
                    });
                }, 500);
            }

            @Override
            public void onError(String error) {

            }
        });
    }

    private void setupToolbar() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void mapping(){
        toolbar = findViewById(R.id.toolbar);
        recyclerViewProducts = findViewById(R.id.recyclerViewProducts);
    }

    @Override
    public void onItemClick(Product product) {

    }
}