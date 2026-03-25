package com.example.shoppingmanager.dal;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.shoppingmanager.entities.Account;
import com.example.shoppingmanager.entities.Category;
import com.example.shoppingmanager.entities.Order;
import com.example.shoppingmanager.entities.OrderDetail;
import com.example.shoppingmanager.entities.Product;

@Database(entities = {
        Account.class,
        Category.class,
        Product.class,
        Order.class,
        OrderDetail.class
}, version = 1)
public abstract class AppDB extends RoomDatabase {
    public abstract DAO dao();

    private static AppDB INSTANCE;

    public static AppDB getInstance(Context context){
        if(INSTANCE == null){
            INSTANCE = Room.databaseBuilder(
                    context.getApplicationContext(),
                    AppDB.class,
                    "ShoppingDB"
            ).allowMainThreadQueries().build();
        }
        return INSTANCE;
    }
}