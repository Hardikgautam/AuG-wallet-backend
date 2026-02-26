package com.prac_icsd2.serviceI;

import java.util.List;

import com.prac_icsd2.model.Address;

public interface AddressService {
	public Address saveAddress(Address add);
	public Address getAddressByAddressId(int addressId);
	 List<Address> findByAddressline2IsNull();
	  List<Address> findByAddressline2IsNotNull();
}
