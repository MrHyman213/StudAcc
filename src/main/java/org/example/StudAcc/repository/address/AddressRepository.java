package org.example.StudAcc.repository.address;

import org.apache.catalina.filters.AddDefaultCharsetFilter;
import org.example.StudAcc.model.address.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AddressRepository extends JpaRepository<Address, Integer> {

}
