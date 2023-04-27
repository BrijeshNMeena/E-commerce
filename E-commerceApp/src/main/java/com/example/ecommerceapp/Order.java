package com.example.ecommerceapp;

import javafx.collections.ObservableList;

import java.sql.ResultSet;

public class Order {

    public static boolean placeOrder(Customer customer, Product product) {
        String groupOrderId = "SELECT max(group_order_id) +1 id FROM orders;";

        DbConnection conn = new DbConnection();
        try{
            ResultSet rs = conn.getQueryTable(groupOrderId);
            if(rs.next()) {
                String orderQuery = "INSERT INTO orders(group_order_id, customer_id, product_id) " +
                        "VALUES("+rs.getInt("id")+", "+customer.getId()+", "+product.getId()+");";
                return conn.updateDatabase(orderQuery) != 0;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    public static int placeMultipleOrder(Customer customer, ObservableList<Product> productList) {
        String groupOrderId = "SELECT max(group_order_id) +1 id FROM orders;";

        DbConnection conn = new DbConnection();
        try{
            ResultSet rs = conn.getQueryTable(groupOrderId);
            int count = 0;
            if(rs.next()) {
                for(Product product : productList) {
                    String orderQuery = "INSERT INTO orders(group_order_id, customer_id, product_id) " +
                            "VALUES(" + rs.getInt("id") + ", " + customer.getId() + ", " + product.getId() + ");";
                    count += conn.updateDatabase(orderQuery);
                }
                return count;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return 0;
    }
}
