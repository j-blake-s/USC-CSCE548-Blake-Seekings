package com.commerce.service;

import io.javalin.http.Context;

import java.sql.SQLException;

import com.commerce.dao.DataProvider;
import com.commerce.business.BusinessManager;
import com.commerce.model.OrderItem;

public class OrderItemController {
    private static BusinessManager bm = new BusinessManager(new DataProvider());

    public static void getAll(Context ctx) throws SQLException {
        ctx.json(bm.getAllOrderItems());
    }

    public static void getOne(Context ctx) throws SQLException  {
        int id = Integer.parseInt(ctx.pathParam("id"));
        OrderItem oi = bm.getOrderItemById(id);
        if (oi != null) ctx.json(oi);
        else ctx.status(404).result("Order Item not found");
    }

    public static void create(Context ctx) throws SQLException  {
        OrderItem oi = ctx.bodyAsClass(OrderItem.class);
        oi = bm.saveOrderItem(oi);
        ctx.header("HX-Refresh", "true"); // Trigger the reload
        ctx.status(201).result("Order Item Added").json(oi.getOrderItemId());
    }

    public static void update(Context ctx) throws SQLException {
        OrderItem oi = ctx.bodyAsClass(OrderItem.class);
        oi.setOrderItemId(Integer.parseInt(ctx.pathParam("id")));
        bm.saveOrderItem(oi);
        ctx.header("HX-Refresh","true");
        ctx.status(200).result("");
    }
    public static void deleteOne(Context ctx) throws SQLException {
        bm.deleteOrderItem(Integer.parseInt(ctx.pathParam("id")));
        ctx.header("HX-Refresh","true");
        ctx.status(200).result("");
    }

}