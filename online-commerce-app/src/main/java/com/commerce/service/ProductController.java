package com.commerce.service;
import io.javalin.http.Context;

import java.sql.SQLException;

import com.commerce.dao.DataProvider;
import com.commerce.business.BusinessManager;
import com.commerce.model.Product;

public class ProductController {
    private static BusinessManager bm = new BusinessManager(new DataProvider());

    public static void getAll(Context ctx) throws SQLException {
        ctx.json(bm.getAllProducts());
    }

    public static void getOne(Context ctx) throws SQLException  {
        int id = Integer.parseInt(ctx.pathParam("id"));
        Product p = bm.getProductById(id);
        if (p != null) ctx.json(p);
        else ctx.status(404).result("Product not found");
    }

    public static void create(Context ctx) throws SQLException  {
        Product p = ctx.bodyAsClass(Product.class);
        p = bm.saveProduct(p);
        ctx.header("HX-Refresh", "true"); // Trigger the reload
        ctx.status(201).result("Product Created").json(p.getProductId());
    }
    
    public static void update(Context ctx) throws SQLException {
        Product p = ctx.bodyAsClass(Product.class);
        p.setProductId(Integer.parseInt(ctx.pathParam("id")));
        bm.saveProduct(p);
        ctx.header("HX-Refresh","true");
        ctx.status(200).result("");
    }
    public static void deleteOne(Context ctx) throws SQLException {
        bm.deleteProduct(Integer.parseInt(ctx.pathParam("id")));
        ctx.header("HX-Refresh","true");
        ctx.status(200).result("");
    }

}