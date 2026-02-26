package com.commerce.service;

import io.javalin.http.Context;

import java.sql.SQLException;

import com.commerce.dao.DataProvider;
import com.commerce.business.BusinessManager;
import com.commerce.model.Shipment;

public class ShipmentController {
    private static BusinessManager bm = new BusinessManager(new DataProvider());

    public static void getAll(Context ctx) throws SQLException {
        ctx.json(bm.getAllShipments());
    }

    public static void getOne(Context ctx) throws SQLException {
        int id = Integer.parseInt(ctx.pathParam("id"));
        Shipment s = bm.getShipmentById(id);
        if (s != null) ctx.json(s);
        else ctx.status(404).result("Shipment not found");
    }

    public static void create(Context ctx) throws SQLException {
        Shipment s = ctx.bodyAsClass(Shipment.class);
        s = bm.saveShipment(s);
        ctx.status(201).result("Shipment Created").json(s.getShipmentId());
    }

    public static void update(Context ctx) throws SQLException {
        Shipment s = ctx.bodyAsClass(Shipment.class);
        s.setShipmentId(Integer.parseInt(ctx.pathParam("id")));
        bm.saveShipment(s);
        ctx.status(200).result("");
    }
    public static void deleteOne(Context ctx) throws SQLException {
        bm.deleteShipment(Integer.parseInt(ctx.pathParam("id")));
        ctx.status(200).result("");
    }
}