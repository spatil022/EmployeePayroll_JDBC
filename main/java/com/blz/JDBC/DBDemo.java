package com.blz.JDBC;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.util.Enumeration;


public class DBDemo {
	public static void printWelcomeMessage() {
		System.out.println("Welcome to Employee Payroll JDBC ");

	}

	public static void main(String[] args) {
		String jdbcURL = "jdbc:mysql://localhost:3306/service_payroll?useSSl=false";
		String userName = "root";
		String password = "2000";
		Connection connection;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			System.out.println("Driver loaded!");
		} catch (ClassNotFoundException e) {
			throw new IllegalStateException("Cannot find the driver in the classpath");
		}
		listDrivers();
		try {
			System.out.println("connecting tothe database:" + jdbcURL);
			connection = DriverManager.getConnection(jdbcURL, userName, password);
			System.out.println("connection is successful!!!!" + connection);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void listDrivers() {
		Enumeration<Driver> driverList = DriverManager.getDrivers();
		while (driverList.hasMoreElements()) {
			Driver driverClass = driverList.nextElement();
			System.out.println(driverClass.getClass().getName());
		}
	}
}