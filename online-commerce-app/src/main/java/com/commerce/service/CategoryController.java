package com.commerce.service;

import io.javalin.http.Context;

import java.sql.SQLException;

import com.commerce.dao.DataProvider;
import com.commerce.business.BusinessManager;
import com.commerce.model.Category;

public class CategoryController {
    private static BusinessManager bm = new BusinessManager(new DataProvider());

    public static void getAll(Context ctx) throws SQLException {
        ctx.json(bm.getAllCategories());
    }

    public static void getOne(Context ctx) throws SQLException {
        int id = Integer.parseInt(ctx.pathParam("id"));
        Category cat = bm.getCategoryById(id);
        if (cat != null) ctx.json(cat);
        else ctx.status(404).result("Category not found");
    }

    public static void create(Context ctx) throws SQLException {
        Category cat = ctx.bodyAsClass(Category.class);
        cat = bm.saveCategory(cat);
        ctx.header("HX-Refresh", "true"); // Trigger the reload
        ctx.status(201).result("Category Created").json(cat.getCategoryId());;
    }

    public static void update(Context ctx) throws SQLException {
        Category cat = ctx.bodyAsClass(Category.class);
        cat.setCategoryId(Integer.parseInt(ctx.pathParam("id")));
        bm.saveCategory(cat);
        ctx.header("HX-Refresh","true");
        ctx.status(200).result("");
    }
    
    public static void deleteOne(Context ctx) throws SQLException {
        bm.deleteCategory(Integer.parseInt(ctx.pathParam("id")));
        ctx.header("HX-Refresh","true");
        ctx.status(200);
    }
}