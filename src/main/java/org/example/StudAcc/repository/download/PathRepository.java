package org.example.StudAcc.repository.download;

import org.example.StudAcc.model.download.Path;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PathRepository extends JpaRepository<Path, Integer> {

    Optional<Path> findByPath(String path);
}
