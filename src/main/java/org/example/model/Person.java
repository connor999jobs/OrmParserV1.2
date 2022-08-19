package org.example.model;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@TableName(name = "person")
public class Person {
    private String name;
    private Integer age;
    private Double salary;
    private String position;
    private LocalDate dateOfBirth;
}
