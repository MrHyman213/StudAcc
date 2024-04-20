package org.example.StudAcc.repository.student;

import org.example.StudAcc.model.student.Education;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EducationRepository extends JpaRepository<Education, Integer> {

    Optional<Education> findByName(String name);
}
