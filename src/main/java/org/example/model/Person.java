package org.example.model;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


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
