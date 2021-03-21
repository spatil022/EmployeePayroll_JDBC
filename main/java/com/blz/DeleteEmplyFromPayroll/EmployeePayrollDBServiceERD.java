package com.blz.DeleteEmplyFromPayroll;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;

public class EmployeePayrollDBServiceERD {
	private static EmployeePayrollDBServiceERD employeePayrollDBServiceERD;

	private EmployeePayrollDBServiceERD() {
	}

	public static EmployeePayrollDBServiceERD getInstance() {
		if (employeePayrollDBServiceERD == null) {
			employeePayrollDBServiceERD = new EmployeePayrollDBServiceERD();
		}
		return employeePayrollDBServiceERD;
	}

	@SuppressWarnings("finally")
	public EmployeePayrollData addEmployeeToPayroll(String name, double salary, LocalDate startDate, String gender)
			throws PayrollServiceException {
		int employeeId = -1;
		Connection connection = null;
		EmployeePayrollData employeePayrollData = null;
		try {
			connection = EmployeePayrollDBService.getConnection();
			connection.setAutoCommit(false);
		} catch (SQLException e) {
			throw new PayrollServiceException(e.getMessage(), PayrollServiceException.ExceptionType.CONNECTION_PROBLEM);
		}
		try (Statement statement = connection.createStatement()) {
			String sql = String.format(
					"insert into employee_payroll (name,gender,salary,start)" + "values ('%s', '%s', '%s', '%s')", name,
					gender, salary, Date.valueOf(startDate));
			int rowAffected = statement.executeUpdate(sql, statement.RETURN_GENERATED_KEYS);
			if (rowAffected == 1) {
				ResultSet resultSet = statement.getGeneratedKeys();
				if (resultSet.next())
					employeeId = resultSet.getInt(1);
			}
		} catch (SQLException e) {
			try {
				connection.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			throw new PayrollServiceException(e.getMessage(), PayrollServiceException.ExceptionType.INSERTION_PROBLEM);
		}

		try (Statement statement = connection.createStatement()) {
			double deductions = salary * 0.2;
			double taxablePay = salary - deductions;
			double tax = taxablePay * 0.1;
			double netPay = salary - tax;
			String sql = String.format(
					"insert into payroll_details (employee_id, basic_pay, deductions, taxable_pay, tax, net_pay) values "
							+ "('%s', '%s', '%s', '%s', '%s', '%s')",
					employeeId, salary, deductions, taxablePay, tax, netPay);
			int rowAffected = statement.executeUpdate(sql);
			if (rowAffected == 1) {
				employeePayrollData = new EmployeePayrollData(employeeId, name, salary, startDate);
			}
		} catch (SQLException e) {
			try {
				connection.rollback();
				return employeePayrollData;
			} catch (SQLException exception) {
				exception.printStackTrace();
			}
			throw new PayrollServiceException(e.getMessage(), PayrollServiceException.ExceptionType.INSERTION_PROBLEM);
		}
		try (Statement statement = connection.createStatement()) {
			int dept_id = 116;
			String dept_name = "Finance";
			String sql = String.format("insert into department (dept_id,dept_name) values('%s','%s')", dept_id,
					dept_name);
			int rowAffected = statement.executeUpdate(sql);
			if (rowAffected == 0)
				throw new PayrollServiceException("insertion into deptartment table is unsuccessful !!!",
						PayrollServiceException.ExceptionType.INSERTION_PROBLEM);
		} catch (PayrollServiceException e1) {
			System.out.println(e1);
			try {
				connection.rollback();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} catch (SQLException e) {
			try {
				connection.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			throw new PayrollServiceException(e.getMessage(), PayrollServiceException.ExceptionType.INSERTION_PROBLEM);
		}

		try (Statement statement = connection.createStatement()) {
			int dept_id1 = 105;
			String sql1 = String.format("insert into employee_department (employee_id,dept_id) values('%s','%s')",
					employeeId, dept_id1);
			statement.executeUpdate(sql1);
			int dept_id = 101;
			String sql = String.format("insert into employee_department (employee_id,dept_id) values('%s','%s')",
					employeeId, dept_id);
			int rowAffected1 = statement.executeUpdate(sql);
			if (rowAffected1 == 1) {
				employeePayrollData = new EmployeePayrollData(employeeId, name, salary, startDate);
			}
			if (rowAffected1 == 0)
				throw new PayrollServiceException("insertion into employee_dept table is unsuccessful !!!",
						PayrollServiceException.ExceptionType.INSERTION_PROBLEM);
		} catch (PayrollServiceException e1) {
			System.out.println(e1);
			try {
				connection.rollback();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} catch (SQLException e) {
			try {
				connection.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			throw new PayrollServiceException("insertion into employee_dept table is unsuccessful !!!",
					PayrollServiceException.ExceptionType.INSERTION_PROBLEM);
		}

		try {
			connection.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (connection != null)
				try {
					connection.close();
				} catch (SQLException e) {
					throw new PayrollServiceException(e.getMessage(),
							PayrollServiceException.ExceptionType.CONNECTION_PROBLEM);
				}
		}
		return employeePayrollData;
	}
	
	public int removeEmployee(String name) {
		try (Connection connection = EmployeePayrollDBService.getConnection();) {
			String sql = "update employee_payroll set is_active=? where name=?";
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setBoolean(1, false);
			preparedStatement.setString(2, name);
			int status = preparedStatement.executeUpdate();
			return status;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

}