package com.jbd.entity;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Employee {

    int employeeId;
    String employeeName;
    int employeeAge;
}