create table person(
                       name varchar(255) not null,
                       age int4 not null,
                       salary float8 not null,
                       position varchar(255) not null,
                       dateOfBirth date
);

insert into person(name, age, salary, position, dateOfBirth)
VALUES('Vadym', 24, 200.0, 'trainee', '1998-09-27');

insert into person(name, age, salary, position, dateOfBirth)
VALUES('Andrey', 23, 1500.0, 'strong junior java developer', '1998-06-05');

insert into person(name, age, salary, position, dateOfBirth)
VALUES('Dima', 23, 200.0, 'NetWork Engineer', '1998-11-25');

insert into person(name, age, salary, position, dateOfBirth)
VALUES('Valik', 23, 5000.0, 'senior java developer', '1999-02-06');