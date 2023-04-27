package com.example.ecommerceapp;

import java.sql.ResultSet;

public class Login {

    public Customer customerLogin(String userName, String passwaord) {
        String loginQuery = "SELECT * FROM customers_details WHERE email = '"+userName+"' AND password = '"+passwaord+"';";
        DbConnection conn = new DbConnection();
        ResultSet rs = conn.getQueryTable(loginQuery);
        try{
            if(rs.next()){
                return new Customer(rs.getInt("id"), rs.getString("name"),
                        rs.getString("mobile"), rs.getString("email"),
                        rs.getString("address"));
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        Login login = new Login();
        Customer customer = login.customerLogin("brijesh@gmail.com", "123zxc");
        System.out.println("Welcome " + customer.getName());
    }
}
