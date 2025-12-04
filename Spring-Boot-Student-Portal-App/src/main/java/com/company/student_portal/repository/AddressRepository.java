package com.company.student_portal.repository;

import com.company.student_portal.domain.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {
    void deleteAddressesById(Long id);
}
