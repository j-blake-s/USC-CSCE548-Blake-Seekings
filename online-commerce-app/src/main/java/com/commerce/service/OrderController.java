package com.commerce.service;

import io.javalin.http.Context;

import java.sql.SQLException;

import com.commerce.dao.DataProvider;
import com.commerce.business.BusinessManager;
import com.commerce.model.Order;

public class OrderController {
    private static BusinessManager bm = new BusinessManager(new DataProvider());

    public static void getAll(Context ctx) throws SQLException {
        ctx.json(bm.getAllOrders());
    }

    public static void getOne(Context ctx) throws SQLException {
        int id = Integer.parseInt(ctx.pathParam("id"));
        Order o = bm.getOrderWithItems(id); // Use the new method
        
        if (o != null) {
            ctx.json(o);
        } else {
            ctx.status(404).result("Order not found");
        }
    }

    public static void create(Context ctx) throws SQLException {
        Order o = ctx.bodyAsClass(Order.class);
        o = bm.saveOrder(o);
        ctx.status(201).result("Order Created").json(o.getOrderId());
    }

    public static void update(Context ctx) throws SQLException {
        Order o = ctx.bodyAsClass(Order.class);
        o.setOrderId(Integer.parseInt(ctx.pathParam("id")));
        bm.saveOrder(o);
        ctx.status(200).result("");
    }
    public static void deleteOne(Context ctx) throws SQLException {
        bm.deleteOrder(Integer.parseInt(ctx.pathParam("id")));
        ctx.status(200).result("");
    }

}