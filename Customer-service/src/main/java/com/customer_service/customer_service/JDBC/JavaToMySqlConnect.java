package com.customer_service.customer_service.JDBC;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class JavaToMySqlConnect {

	public static void main(String[] args) throws Exception {
		//Class.forName("com.mysql.cj.jdbc.driver");
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		try {
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/classicmodels", "root","Remember@978");
			st = conn.createStatement();
			int a = 2;
			switch(a) {
			case 1 :
				rs = st.executeQuery("SELECT * FROM classicmodels.customers");
				while (rs.next()) {
					System.out.println(rs.getString("customerName") + " ===== " + rs.getString("country"));
				}
				break;
			case 2 :
				int r = st.executeUpdate("UPDATE classicmodels.customers SET customers.country = \"USA\" where customers.customerNumber = 112;");
				System.out.println(r);
				break;
			case 3 :
				
				break;
			}
		}catch(Exception ex) {
			throw new Exception(ex.getMessage());
		}finally {
			if(rs!=null) {
				rs.close();
			}
			st.close();
			conn.close();
		}
	}

}
