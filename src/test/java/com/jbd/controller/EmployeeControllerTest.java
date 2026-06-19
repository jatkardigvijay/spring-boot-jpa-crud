package com.jbd.controller;

import com.jbd.config.Response;
import com.jbd.entity.Employee;
import com.jbd.exception.JbdException;
import com.jbd.service.EmployeeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EmployeeControllerTest {

    @Mock
    private EmployeeService employeeService;

    @InjectMocks
    private EmployeeController employeeController;

    @Test
    void getAllEmployees_returnsOkWithData_whenEmployeesExist() throws Exception {
        List<Employee> employees = Arrays.asList(
                new Employee(1, "Alice", 30),
                new Employee(2, "Bob", 25)
        );
        when(employeeService.getAllEmployees()).thenReturn(employees);

        ResponseEntity<Response> result = employeeController.getAllEmployees();

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().getMessage()).isEqualTo("success");
        assertThat(result.getBody().getData()).isEqualTo(employees);
    }

    @Test
    void getAllEmployees_throwsJbdException_whenListIsEmpty() {
        when(employeeService.getAllEmployees()).thenReturn(Collections.emptyList());

        assertThatThrownBy(() -> employeeController.getAllEmployees())
                .isInstanceOf(JbdException.class)
                .hasMessage("No data found");
    }

    @Test
    void getAllEmployees_throwsJbdException_whenListIsNull() {
        when(employeeService.getAllEmployees()).thenReturn(null);

        assertThatThrownBy(() -> employeeController.getAllEmployees())
                .isInstanceOf(JbdException.class)
                .hasMessage("No data found");
    }
}