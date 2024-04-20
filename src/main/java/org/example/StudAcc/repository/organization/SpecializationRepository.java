package org.example.StudAcc.repository.organization;

import org.example.StudAcc.model.organization.Organization;
import org.example.StudAcc.model.organization.Specialization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SpecializationRepository extends JpaRepository<Specialization, Integer> {

    Optional<Specialization> findByName(String name);
}
