package com.example.hotel.repository.customer;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.hotel.entity.customer.CustomerRegistration;

public interface CustomerRegistrationRepository extends JpaRepository<CustomerRegistration, Long>{

	CustomerRegistration findByEmail(String email);
	
	@Query(value="select cr.*,v.number,v.verification_id,v.verification_type from customer_registration as cr"
			+ " join verification as v on v.customer_id = cr.customer_id"
			+ " order by cr.customer_id desc", nativeQuery =  true)
	List<Map<String, Object>> getAllCustomerDetails();
	
	@Query(value="select cr.*,v.number,v.verification_id,v.verification_type from customer_registration as cr"
			+ " join verification as v on v.customer_id = cr.customer_id"
			+ " where cr.customer_id = :customer_id", nativeQuery =  true)
	List<Map<String, Object>> getAllCustomerDetailsWithId(Long customer_id);
	
	@Query(value="select cr.customer_id,cr.name,cr.mobile_number from customer_registration as cr"
			+ " where cr.customer_id = :customer_id", nativeQuery = true)
	List<Map<String, Object>> getLoginDetailsResponse(Long customer_id);


	
	@Query(value=" select cu.customer_id as customerId,cu.address,cu.city_id as cityId,cu.country_id as countryId,cu.email,"
			+ " cu.gender,cu.mobile_number as mobileNumber,cu.name,cu.number,cu.verification_type as verificationType,cu.state_id as stateId,"
			+ " ci.city_name as cityName,c.country_name as countryName,s.state_name as stateName"
			+ " from customer_registration as cu"
			+ " join country as c on c.country_id=cu.country_id"
			+ " join city as ci on ci.city_id=cu.city_id"
			+ " join state as s on s.state_id=cu.state_id",nativeQuery = true)
	List<Map<String, Object>>getCustomerDetails();

	 List<CustomerRegistration> findAllByOrderByCustomerIdDesc();

}
