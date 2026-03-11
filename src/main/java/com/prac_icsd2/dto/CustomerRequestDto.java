package com.prac_icsd2.dto;

import com.prac_icsd2.enums.Gender;
import com.prac_icsd2.enums.SubscriptionType;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerRequestDto {

//	@NotBlank(message = "First name should not be blank")
	@NotNull(message="First name should not be null")
	@Size(min = 2,max = 60,message = "Name should be between 2 to 60")
	public String firstName;
	
//	@NotBlank(message = "Last name should not be blank")
	@NotNull(message="Last name should not be null")
	private String lastName;
	
	//@NotBlank(message = "emailId name should not be blank")
	@NotNull(message="emailId name should not be null")
	@Email(message ="not valid email formatrrrrrrrrrrrrrr ")
	private String emailId;
	
	//@NotBlank(message = "contactNo name should not be blank")
	@NotNull(message="contactNo name should not be null")
	private String contactNo;
	
	private Gender gender;
	
	
	//@NotBlank(message = "password name should not be blank")
	@NotNull(message="password name should not be null")
	//@Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$")
	private String  password;
	//@NotBlank(message = "confirmPassword name should not be blank")
	
	//@NotBlank(message = "addressLine1 name should not be blank")
	@NotNull(message="addressLine1 name should not be null")
	private String addressLine1;
	//@NotBlank(message = "addressline2 name should not be blank")
	@NotNull(message="addressline2 name should not be null")
	private String addressLine2;
	//@NotBlank(message = "city name should not be blank")
	@NotNull(message="city name should not be null")
	private String city;
	//@NotBlank(message = "State name should not be blank")
	@NotNull(message="State name should not be null")
	private String state;
	//@NotBlank(message = "pincode name should not be blank")
	@NotNull(message="pincode name should not be null")
	private String pincode;
	
	
	private SubscriptionType subscriptionType;
	
	@Override
	public String toString() {
		return "CustomerRequestDto [firstName=" + firstName + ", lastName=" + lastName + ", emailId=" + emailId
				+ ", contactNo=" + contactNo + ", gender=" + gender + ", password=" + password +  ", addressLine1=" + addressLine1 + ", addressLine2=" + addressLine2 + ", city="
				+ city + ", State=" + state + ", pincode=" + pincode + "]";
	}

	
	
	
	}
