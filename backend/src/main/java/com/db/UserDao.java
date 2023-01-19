package com.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UserDao {


    public void add() throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/vue","root","test1234");
        PreparedStatement ps = conn.prepareStatement(
                "insert into users(id,name,password) values(?,?,?)"
        );
        ps.setString(1,"0");
        ps.setString(2,"ohjing");
        ps.setString(3,"1234");

        int status = ps.executeUpdate();
        ps.close();
        conn.close();

    }
}
