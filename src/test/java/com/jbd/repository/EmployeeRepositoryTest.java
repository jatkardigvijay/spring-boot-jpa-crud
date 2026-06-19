package com.jbd.repository;

import com.jbd.entity.Employee;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class EmployeeRepositoryTest {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Test
    void save_andFindById_returnsPersistedEmployee() {
        Employee saved = employeeRepository.save(new Employee(0, "Charlie", 28));

        Optional<Employee> found = employeeRepository.findById(saved.getEmployeeId());

        assertThat(found).isPresent();
        assertThat(found.get().getEmployeeName()).isEqualTo("Charlie");
        assertThat(found.get().getEmployeeAge()).isEqualTo(28);
    }

    @Test
    void findById_returnsEmpty_whenEmployeeDoesNotExist() {
        Optional<Employee> found = employeeRepository.findById(Integer.MAX_VALUE);

        assertThat(found).isEmpty();
    }

    @Test
    void deleteById_removesEmployee() {
        Employee saved = employeeRepository.save(new Employee(0, "Dave", 35));
        int savedId = saved.getEmployeeId();

        employeeRepository.deleteById(savedId);

        assertThat(employeeRepository.findById(savedId)).isEmpty();
    }
}