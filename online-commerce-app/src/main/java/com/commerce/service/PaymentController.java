package com.commerce.service;

import io.javalin.http.Context;

import java.sql.SQLException;

import com.commerce.dao.DataProvider;
import com.commerce.business.BusinessManager;
import com.commerce.model.Payment;

public class PaymentController {
    private static BusinessManager bm = new BusinessManager(new DataProvider());

    public static void getAll(Context ctx) throws SQLException {
        ctx.json(bm.getAllPayments());
    }

    public static void getOne(Context ctx) throws SQLException {
        int id = Integer.parseInt(ctx.pathParam("id"));
        Payment p = bm.getPaymentById(id);
        if (p != null) ctx.json(p);
        else ctx.status(404).result("Payment not found");
    }

    public static void create(Context ctx) throws SQLException {
        Payment p = ctx.bodyAsClass(Payment.class);
        p = bm.savePayment(p);
        ctx.status(201).result("Payment Processed").json(p.getPaymentId());
    }

    public static void update(Context ctx) throws SQLException {
        Payment p = ctx.bodyAsClass(Payment.class);
        p.setPaymentId(Integer.parseInt(ctx.pathParam("id")));
        bm.savePayment(p);
        ctx.status(204);
    }

    public static void deleteOne(Context ctx) throws SQLException {
        bm.deletePayment(Integer.parseInt(ctx.pathParam("id")));
        ctx.status(204);
    }

}