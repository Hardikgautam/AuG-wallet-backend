package com.prac_icsd2.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.prac_icsd2.model.Address;

public interface AddressRepo extends JpaRepository<Address,Integer> {

	  List<Address> findByAddressline2IsNull();
	  List<Address> findByAddressline2IsNotNull();
}
