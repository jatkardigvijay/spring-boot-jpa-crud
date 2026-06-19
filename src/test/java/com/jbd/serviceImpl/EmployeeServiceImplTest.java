package com.jbd.serviceImpl;

import com.jbd.entity.Employee;
import com.jbd.repository.EmployeeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceImplTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    @Test
    void getAllEmployees_returnsEmployeeList_whenDataExists() {
        List<Employee> employees = Arrays.asList(
                new Employee(1, "Alice", 30),
                new Employee(2, "Bob", 25)
        );
        when(employeeRepository.findAll()).thenReturn(employees);

        List<Employee> result = employeeService.getAllEmployees();

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getEmployeeName()).isEqualTo("Alice");
        assertThat(result.get(1).getEmployeeAge()).isEqualTo(25);
        verify(employeeRepository).findAll();
    }

    @Test
    void getAllEmployees_returnsEmptyList_whenNoData() {
        when(employeeRepository.findAll()).thenReturn(Collections.emptyList());

        List<Employee> result = employeeService.getAllEmployees();

        assertThat(result).isEmpty();
        verify(employeeRepository).findAll();
    }
}