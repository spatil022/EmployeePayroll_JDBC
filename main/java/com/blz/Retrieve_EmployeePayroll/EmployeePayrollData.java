package com.blz.Retrieve_EmployeePayroll;

import java.time.LocalDate;

public class EmployeePayrollData {

	private int id;
	private String name;
	private double salary;
	private LocalDate start;

	/* Constructor */
	public EmployeePayrollData(int id, String name, double salary) {
		this.id = id;
		this.name = name;
		this.salary = salary;
	}

	/* Constructor */
	public EmployeePayrollData(int id, String name, double salary, LocalDate start) {
		this(id,name,salary);
		this.start = start;
	}

	@Override
	public String toString() {
		return "EmployeePayrollData [ID=" + id + ", Name=" + name + ", Salary=" + salary + ", Start=" + start + "]";
	}

}
