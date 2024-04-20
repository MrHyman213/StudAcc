package org.example.StudAcc.repository.organization.employee;

import org.example.StudAcc.model.organization.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {

    Optional<Employee> findByName(String name);
}
