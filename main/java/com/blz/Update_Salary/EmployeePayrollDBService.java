package com.blz.Update_Salary;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


public class EmployeePayrollDBService {
	private PreparedStatement employeePayrollDataStatement;
	private static EmployeePayrollDBService employeePayrollDBService;
	private EmployeePayrollDBService(){

	}

	private Connection getConnection() throws SQLException {
		String jdbcURL = "jdbc:mysql://localhost:3306/service_payroll?useSSl=false";
		String userName = "root";
		String password = "2000";
		Connection connection;
		System.out.println("connecting tothe database:" + jdbcURL);
		connection = DriverManager.getConnection(jdbcURL, userName, password);
		System.out.println("connection is successful!!!!" + connection);
		return connection;
	}

	public static EmployeePayrollDBService getInstance() {
		if(employeePayrollDBService == null) 
			employeePayrollDBService = new EmployeePayrollDBService();
		return employeePayrollDBService;
	}

	public List<EmployeePayrollData> getEmployeePayrollData(String name) {
		List<EmployeePayrollData> employeePayrollList = null;
		if(this.employeePayrollDataStatement == null)
			this.prepareStatementForEmployeeData();
		try {
			employeePayrollDataStatement.setString(1,name);
			ResultSet resultSet = employeePayrollDataStatement.executeQuery();
			employeePayrollList = this.getEmployeePayrollData(resultSet);
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return employeePayrollList;
	}

	private List<EmployeePayrollData> getEmployeePayrollData(ResultSet result) {
		List<EmployeePayrollData> employeePayrollList = new ArrayList<>();
		try {
			while (result.next()) {
				int id = result.getInt("id");
				String name = result.getString("name");
				Double salary = result.getDouble("salary");
				LocalDate startDate = result.getDate("start").toLocalDate();
				employeePayrollList.add(new EmployeePayrollData(id, name, salary, startDate));
				return employeePayrollList;
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	private void prepareStatementForEmployeeData() {
		try {
			Connection connection = this.getConnection();
			String sql = "select * from employee_payroll where name = ?";
			employeePayrollDataStatement = connection.prepareStatement(sql);
		}catch(SQLException e) {
			e.printStackTrace();
		}

	}

	public List<EmployeePayrollData> readData() throws PayrollServiceException {
		String sql = "select * from employee_payroll";
		List<EmployeePayrollData> employeePayrollList = new ArrayList<>();
			try {
				Connection connection = this.getConnection();
				Statement statement = connection.createStatement();
				ResultSet result = statement.executeQuery(sql);
				while(result.next()) {
					int id = result.getInt("id");
					String name = result.getString("name");
					Double salary = result.getDouble("salary");
					LocalDate startDate = result.getDate("start").toLocalDate();
					employeePayrollList.add(new EmployeePayrollData(id,name,salary,startDate));
				}
			} catch (SQLException e) {
				throw new PayrollServiceException(e.getMessage(),PayrollServiceException.ExceptionType.RETRIEVAL_PROBLEM);
		
		}
		return employeePayrollList;
	}


	public int updateEmployeeData(String name, double salary) throws PayrollServiceException {
		return this.updateEmployeeDataUsingStatement(name, salary);
	}

	private int updateEmployeeDataUsingStatement(String name, double salary) throws PayrollServiceException {
		String sql = String.format("update employee_payroll set salary = %.2f where name = '%s';", salary, name);
		try {
			Connection connection = this.getConnection();
			Statement statement = connection.createStatement();
			return statement.executeUpdate(sql);
		} catch (SQLException e) {
			throw new PayrollServiceException(e.getMessage(), PayrollServiceException.ExceptionType.UPDATE_PROBLEM);
		}
	}
}