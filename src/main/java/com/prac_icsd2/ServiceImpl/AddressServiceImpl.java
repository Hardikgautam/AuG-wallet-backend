package com.prac_icsd2.ServiceImpl;


import java.util.List;

import org.springframework.stereotype.Service;

import com.prac_icsd2.model.Address;
import com.prac_icsd2.repo.AddressRepo;
import com.prac_icsd2.serviceI.AddressService;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Service
@Data
@Slf4j
public class AddressServiceImpl implements AddressService{

	private final AddressRepo addressRepo;
	@Override
	public Address saveAddress(Address add) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Address getAddressByAddressId(int addressId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Address> findByAddressline2IsNull() {
		// TODO Auto-generated method stub
		return addressRepo.findByAddressline2IsNull();
	}

	@Override
	public List<Address> findByAddressline2IsNotNull() {
		// TODO Auto-generated method stub
		return addressRepo.findByAddressline2IsNotNull();
	}


}
