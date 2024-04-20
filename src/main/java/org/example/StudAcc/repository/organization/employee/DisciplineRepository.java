package org.example.StudAcc.repository.organization.employee;

import org.example.StudAcc.model.organization.Discipline;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DisciplineRepository extends JpaRepository<Discipline, Integer> {
    Optional<Discipline> findByName(String name);
}
