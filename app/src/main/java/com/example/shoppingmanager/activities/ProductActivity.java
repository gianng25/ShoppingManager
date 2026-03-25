package com.example.shoppingmanager.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.shoppingmanager.R;
import com.example.shoppingmanager.dal.AppDB;
import com.example.shoppingmanager.entities.Order;
import com.example.shoppingmanager.entities.OrderDetail;
import com.example.shoppingmanager.entities.Product;

import java.util.ArrayList;
import java.util.List;

public class ProductActivity extends AppCompatActivity {

    ListView lvProducts;
    AppDB db;
    List<Product> productList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        lvProducts = findViewById(R.id.lvProducts);
        db = AppDB.getInstance(this);

        Button btnGoCart = findViewById(R.id.btnGoCart);
        btnGoCart.setOnClickListener(v ->
                startActivity(new Intent(this, CartActivity.class)));

        loadProducts();
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Nếu vừa login xong và có sản phẩm chờ thêm
        SharedPreferences sp = getSharedPreferences("LOGIN", MODE_PRIVATE);
        boolean isLogin = sp.getBoolean("isLogin", false);
        int pendingProductId = sp.getInt("pendingProductId", -1);

        if (isLogin && pendingProductId != -1) {
            addProductToCart(pendingProductId);

            sp.edit().remove("pendingProductId").apply();

            Toast.makeText(this, "Đã thêm sản phẩm sau khi đăng nhập", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadProducts() {
        productList = db.dao().getAllProducts();

        List<String> names = new ArrayList<>();
        for (Product p : productList) {
            names.add(p.name + " - " + p.price);
        }

        lvProducts.setAdapter(new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                names
        ));

        lvProducts.setOnItemClickListener((parent, view, position, id) -> {
            Product p = productList.get(position);

            SharedPreferences sp = getSharedPreferences("LOGIN", MODE_PRIVATE);
            boolean isLogin = sp.getBoolean("isLogin", false);

            if (!isLogin) {
                // lưu sản phẩm đang chọn rồi chuyển login
                sp.edit().putInt("pendingProductId", p.id).apply();

                Toast.makeText(this, "Bạn cần đăng nhập trước", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, LoginActivity.class));
                return;
            }

            addProductToCart(p.id);
            Toast.makeText(this, "Đã thêm vào giỏ hàng", Toast.LENGTH_SHORT).show();
        });
    }

    private void addProductToCart(int productId) {
        SharedPreferences sp = getSharedPreferences("LOGIN", MODE_PRIVATE);
        String username = sp.getString("username", "");

        Product p = db.dao().getProductById(productId);
        if (p == null) return;

        Order order = db.dao().getPendingOrder(username);

        if (order == null) {
            order = new Order();
            order.username = username;
            order.orderDate = System.currentTimeMillis();
            order.status = "Pending";
            long oid = db.dao().insertOrder(order);
            order.id = (int) oid;
        }

        OrderDetail detail = db.dao().getOrderDetail(order.id, p.id);

        if (detail == null) {
            detail = new OrderDetail();
            detail.orderId = order.id;
            detail.productId = p.id;
            detail.quantity = 1;
            detail.unitPrice = p.price;
            db.dao().insertOrderDetail(detail);
        } else {
            db.dao().updateQuantity(detail.id, detail.quantity + 1);
        }
    }
}