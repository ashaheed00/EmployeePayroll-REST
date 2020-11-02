package com.bl.emppayroll;

import static org.junit.Assert.assertEquals;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.bl.emppayroll.service.EmployeePayrollService;
import com.google.gson.Gson;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class EmployeePayrollRESTAssuredTest {

	@Before
	public void setup() {
		RestAssured.baseURI = "http://localhost";
		RestAssured.port = 3000;
	}

	private List<EmployeePayrollData> getEmployeeList() {
		Response response = RestAssured.get("/employee_payroll");
		System.out.println("Employee Payroll Entries in JSON Server: \n" + response.toString());
		EmployeePayrollData[] empArrays = new Gson().fromJson(response.asString(), EmployeePayrollData[].class);
		return Arrays.asList(empArrays);
	}

	@Test
	public void givenEmployeeDataInJSONServer_WhenRetrieved_ShouldMatchTheCount() {
		List<EmployeePayrollData> employeePayrollList = getEmployeeList();
		assertEquals(7, employeePayrollList.size());
	}

	private Response addEmployeeToJSONServer(EmployeePayrollData employeePayrollData) {
		String empJson = new Gson().toJson(employeePayrollData);
		RequestSpecification requestSpecification = RestAssured.given();
		requestSpecification.header("Content-Type", "application/json");
		requestSpecification.body(empJson);
		return requestSpecification.post("/employee_payroll");
	}

	@Test
	public void givenNewEmployeeData_WhenAdded_ShouldMatchTheCountAndStatusCode() {
		List<EmployeePayrollData> employeePayrollList = getEmployeeList();
		EmployeePayrollService employeePayrollService = new EmployeePayrollService(employeePayrollList);
		EmployeePayrollData employeePayrollData = new EmployeePayrollData(7, "Kalyan", 1000000,
				Date.valueOf(LocalDate.now()), "M", 501);
		Response response = addEmployeeToJSONServer(employeePayrollData);
		assertEquals(201, response.statusCode());
	}
}
