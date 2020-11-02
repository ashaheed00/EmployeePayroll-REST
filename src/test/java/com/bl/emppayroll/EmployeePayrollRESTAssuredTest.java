package com.bl.emppayroll;

import static org.junit.Assert.assertEquals;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Ignore;
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
		assertEquals(10, employeePayrollList.size());
	}

	private Response addEmployeeToJSONServer(EmployeePayrollData employeePayrollData) {
		String empJson = new Gson().toJson(employeePayrollData);
		RequestSpecification requestSpecification = RestAssured.given();
		requestSpecification.header("Content-Type", "application/json");
		requestSpecification.body(empJson);
		return requestSpecification.post("/employee_payroll");
	}

	@Ignore
	@Test
	public void givenNewEmployeeData_WhenAdded_ShouldMatchTheCountAndStatusCode() {
		EmployeePayrollData employeePayrollData = new EmployeePayrollData(7, "Kalyan", 1000000,
				Date.valueOf(LocalDate.now()), "M", 501);
		Response response = addEmployeeToJSONServer(employeePayrollData);
		assertEquals(201, response.statusCode());
	}

	@Test
	public void givenNewEmployeeList_WhenAdded_ShouldMatchTheCountAndStatusCodes() {
		List<EmployeePayrollData> employeePayrollDataList = new ArrayList<>() {
			{
				add(new EmployeePayrollData(7, "Kalyan", 1000000, Date.valueOf(LocalDate.now()), "M", 501));
				add(new EmployeePayrollData(8, "Rashmi", 1000000, Date.valueOf(LocalDate.now()), "F", 501));
				add(new EmployeePayrollData(9, "Sharad", 2000000, Date.valueOf(LocalDate.now()), "M", 501));
				add(new EmployeePayrollData(10, "Nancy", 1500000, Date.valueOf(LocalDate.now()), "F", 501));
			}
		};
		for (EmployeePayrollData employeePayrollData : employeePayrollDataList) {
			Response response = addEmployeeToJSONServer(employeePayrollData);
			assertEquals(201, response.statusCode());
		}
	}
}
