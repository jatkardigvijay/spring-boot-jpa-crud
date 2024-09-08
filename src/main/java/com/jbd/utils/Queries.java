package com.jbd.utils;

public class Queries {

	public static final String GET_ALL_EMPLOYEES = "select * from employees";
	public static final String DELETE_EMPLOYEE = "delete from employees where employee_Id = ?";
	public static final String INSERT_EMPLOYEE = "insert into employees values (?,?,?)";
	public static final String UPDATE_EMPLOYEE = "update employees set empName = ?, epAge = ? where empId = ?";
	public static final String GET_EMPLOYEE = "select * from employees where employee_Id = ?";

}