package org.example.StudAcc.repository.student;

import org.example.StudAcc.model.student.OrderNumber;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderNumberRepository extends JpaRepository<OrderNumber, Integer> {

    Optional<OrderNumber> findByName(String name);
}
