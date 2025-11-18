package com.adr57.netdemo.storage.database.dao;


import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.adr57.netdemo.storage.database.entities.Product;

import java.util.List;

@Dao
public interface ProductDao {
    @Query("SELECT * FROM products")
    List<Product> getAllProducts();

    @Query("SELECT * FROM products WHERE id = :productId")
    Product getProductById(int productId);

    @Query("SELECT * FROM products WHERE name = :productName")
    Product getProductByName(String productName);

    @Query("SELECT * FROM products WHERE price = :productPrice")
    Product getProductByPrice(double productPrice);

    @Query("SELECT * FROM products WHERE description = :productDescription")
    Product getProductByDescription(String productDescription);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertAllProduct(Product... products);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertProduct(Product product);

}
