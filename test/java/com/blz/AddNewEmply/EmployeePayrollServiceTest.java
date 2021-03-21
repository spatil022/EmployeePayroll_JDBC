package com.blz.AddNewEmply;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.blz.AddNewEmply.EmployeePayrollService.IOService;



public class EmployeePayrollServiceTest {
	static EmployeePayrollService employeePayrollService;

	@BeforeClass
	public static void initializeConstructor() {
		employeePayrollService = new EmployeePayrollService();
	}

	@Test
	public void printWelcomeMessage() {

		employeePayrollService.printWelcomeMessage();
	}

	@Test
	public void given3EmployeesWhenWrittenToFileShouldMatchEmployeeEntries() {
		EmployeePayrollData[] arrayOfEmps = { new EmployeePayrollData(1, "Jeff Bezos", 100000.0),
				new EmployeePayrollData(2, "Bill Gates", 200000.0),
				new EmployeePayrollData(3, "Mark Zuckerberg", 300000.0) };
		employeePayrollService = new EmployeePayrollService(Arrays.asList(arrayOfEmps));
		employeePayrollService.writeEmployeePayrollData(EmployeePayrollService.IOService.FILE_IO);
		employeePayrollService.printData(EmployeePayrollService.IOService.FILE_IO);
		long entries = employeePayrollService.countEntries(EmployeePayrollService.IOService.FILE_IO);
		Assert.assertEquals(3, entries);
	}

	@Test
	public void givenFileOnReadingFileShouldMatchEmployeeCount() {
		EmployeePayrollService employeePayrollService = new EmployeePayrollService();
		List<EmployeePayrollData> entries = employeePayrollService
				.readPayrollData(EmployeePayrollService.IOService.FILE_IO);
		
	}

	@Test
	public void givenEmployeePayrollinDB_whenRetrieved_ShouldMatch_Employee_Count() throws PayrollServiceException {
		List<EmployeePayrollData> employeePayrollData = employeePayrollService
				.readEmployeePayrollData(EmployeePayrollService.IOService.DB_IO);
		Assert.assertEquals(3, employeePayrollData.size());
	}

	@Test
	public void givenNewSalaryForEmployee_WhenUpdated_shouldSynchronizewithDataBase() throws PayrollServiceException {
		List<EmployeePayrollData> employeePayrollData = employeePayrollService
				.readEmployeePayrollData(EmployeePayrollService.IOService.DB_IO);
		employeePayrollService.updateEmployeeSalary("Teresa", 3000000.00);
		boolean result = employeePayrollService.checkEmployeePayrollInSyncWithDB("Teresa");
		Assert.assertTrue(result);
	}

	@Test
	public void givenDateRange_WhenRetrieved_ShouldMatchEmployeeCount() throws PayrollServiceException {
		EmployeePayrollService employeePayrollService = new EmployeePayrollService();
		employeePayrollService.readEmployeePayrollData(IOService.DB_IO);
		LocalDate startDate = LocalDate.of(2018, 01, 01);
		LocalDate endDate = LocalDate.now();
		List<EmployeePayrollData> employeePayrollData = employeePayrollService
				.readEmployeePayrollForDateRange(IOService.DB_IO, startDate, endDate);
		Assert.assertEquals(4, employeePayrollData.size());
	}

	@Test
	public void givenPayrollData_whenAverageSalaryRetrievedByGender_shouldReturnProperValue()
			throws PayrollServiceException {
		employeePayrollService.readEmployeePayrollData(IOService.DB_IO);
		Map<String, Double> averageSalaryByGender = employeePayrollService.readAverageSalaryByGender(IOService.DB_IO);
		Assert.assertTrue(averageSalaryByGender.get("M").equals(3000000.00) && averageSalaryByGender.get("F").equals(3000000.00));
	}

	@Test
	public void givenNeEmployee_whenAaddedShouldSyncWithTheDatabase() throws PayrollServiceException {
		employeePayrollService.readEmployeePayrollData(IOService.DB_IO);
		employeePayrollService.addEmployeeToPayroll("Mark", 5000000.00, LocalDate.now(), "M");
		boolean result = employeePayrollService.checkEmployeePayrollInSyncWithDB("Mark");
		Assert.assertTrue(result);
	}
}