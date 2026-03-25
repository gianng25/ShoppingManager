package com.example.shoppingmanager.activities;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.shoppingmanager.R;
import com.example.shoppingmanager.dal.AppDB;
import com.example.shoppingmanager.entities.CartView;

import java.util.List;

public class InvoiceActivity extends AppCompatActivity {

    TextView tvInvoice;
    AppDB db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice);

        tvInvoice = findViewById(R.id.tvInvoice);
        db = AppDB.getInstance(this);

        int orderId = getIntent().getIntExtra("orderId", -1);

        List<CartView> list = db.dao().getCartView(orderId);
        Double total = db.dao().getOrderTotal(orderId);

        StringBuilder sb = new StringBuilder();
        sb.append("HÓA ĐƠN\n\n");

        for (CartView c : list) {
            sb.append("Tên SP: ").append(c.productName).append("\n")
                    .append("Số lượng: ").append(c.quantity).append("\n")
                    .append("Đơn giá: ").append(c.unitPrice).append("\n")
                    .append("Thành tiền: ").append(c.total).append("\n\n");
        }

        sb.append("Tổng cộng: ").append(total == null ? 0 : total);
        tvInvoice.setText(sb.toString());
    }
}