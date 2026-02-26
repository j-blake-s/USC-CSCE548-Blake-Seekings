package com.commerce.service;
import io.javalin.http.Context;

import java.sql.SQLException;
import java.util.List;

import com.commerce.dao.DataProvider;
import com.commerce.business.BusinessManager;
import com.commerce.model.Customer;

public class CustomerController {
    private static BusinessManager bm = new BusinessManager(new DataProvider());


    public static void getAll(Context ctx)  throws SQLException {
        List<Customer> customers = bm.getAllCustomers();
        ctx.json(customers); // Automatically converts list to JSON
    }

    public static void getOne(Context ctx) throws SQLException{
        int id = Integer.parseInt(ctx.pathParam("id"));
        Customer c = bm.getCustomerById(id);
        if (c != null) {
            ctx.json(c);
        } else {
            ctx.status(404).result("Customer not found");
        }
    }

    public static void create(Context ctx) throws SQLException {
        Customer c = ctx.bodyAsClass(Customer.class); // Turns JSON body into Object
        c = bm.saveCustomer(c);
        ctx.header("HX-Refresh", "true"); // Trigger the reload
        ctx.status(201).result("Customer Created").json(c.getCustomerId());
    }

    public static void update(Context ctx) throws SQLException {
        Customer c = ctx.bodyAsClass(Customer.class);
        c.setCustomerId(Integer.parseInt(ctx.pathParam("id")));
        bm.saveCustomer(c);
        ctx.header("HX-Refresh","true");
        ctx.status(200).result(""); // Success, No Content
    }
    public static void deleteOne(Context ctx) throws SQLException {
        bm.deleteCustomer(Integer.parseInt(ctx.pathParam("id")));
        ctx.header("HX-Refresh","true");
        ctx.status(200).result("");
    }
}