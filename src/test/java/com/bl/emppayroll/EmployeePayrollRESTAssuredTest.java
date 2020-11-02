package com.bl.emppayroll;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.google.gson.Gson;

import io.restassured.RestAssured;
import io.restassured.response.Response;

public class EmployeePayrollRESTAssuredTest {
	@Before
	public void setup() {
		RestAssured.baseURI = "http://localhost";
		RestAssured.port = 3000;
	}

	public List<EmployeePayrollData> getEmployeeList() {
		Response response = RestAssured.get("/employee_payroll");
		System.out.println("Employee Payroll Entries in JSON Server: \n" + response.toString());
		EmployeePayrollData[] empArrays = new Gson().fromJson(response.asString(), EmployeePayrollData[].class);
		return Arrays.asList(empArrays);
	}

	@Test
	public void givenEmployeeDataInJSONServer_WhenRetrieved_ShouldMatchTheCount() {
		List<EmployeePayrollData> employeePayrollList = getEmployeeList();
		assertEquals(6, employeePayrollList.size());
	}

}
