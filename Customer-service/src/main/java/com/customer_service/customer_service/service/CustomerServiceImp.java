package com.customer_service.customer_service.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.customer_service.customer_service.dto.request.CustomerRequestDto;
import com.customer_service.customer_service.dto.response.CustomerResponseDto;
import com.customer_service.customer_service.model.CustomerModel;
import com.customer_service.customer_service.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Value;


@Service
public class CustomerServiceImp implements CustomerService {

	@Autowired
	private CustomerRepository custRepo;
	

	@Autowired
	private KafkaTemplate<String, Object> kafkaTemplate;
	
	
	
	@Value("${kafka.topic-name}")
	private String topicName;

	@Override
	public List<CustomerResponseDto> fetchAllCustomers() {
		List<CustomerResponseDto> customers = new ArrayList<>();
		try {
			List<CustomerModel> custModel = custRepo.findAll();

			if(!CollectionUtils.isEmpty(custModel)) {
				for(CustomerModel cust : custModel) {
					CustomerResponseDto customer = new CustomerResponseDto();
					customer.setCustomerNumber(cust.getCustomerNumber());
					customer.setCustomerName(cust.getCustomerName());
					customer.setAddressLine1(cust.getAddressLine1());
					customer.setAddressLine2(cust.getAddressLine2());
					customer.setCity(cust.getCity());
					customer.setContactFirstName(cust.getContactFirstName());
					customer.setContactLastName(cust.getContactLastName());
					customer.setCountry(cust.getCountry());
					customer.setCreditLimit(cust.getCreditLimit());
					customer.setPhone(cust.getPhone());
					customer.setPostalCode(cust.getPostalCode());
					customer.setSalesRepEmployeeNumber(cust.getSalesRepEmployeeNumber());
					customer.setState(cust.getState());
					customers.add(customer);
				}
			}


		}catch(Exception e) {
			System.out.println(e.getMessage());
		}

		return customers;
	}


	@Override
	public CustomerResponseDto createNewCustomer(CustomerRequestDto custDto) {
		CustomerResponseDto resDto = new CustomerResponseDto();
		try {
//			CustomerModel model = new CustomerModel();
//			model.setAddressLine1(custDto.getAddressLine1());
//			model.setAddressLine2(custDto.getAddressLine2());
//			model.setCity(custDto.getCity());
//			model.setContactFirstName(custDto.getContactFirstName());
//			model.setContactLastName(custDto.getContactLastName());
//			model.setCountry(custDto.getCountry());
//			model.setCustomerName(custDto.getCustomerName());
//			model.setCustomerNumber(801);
//			model.setCreditLimit(0);
//			model.setPhone(custDto.getPhone());
//			model.setPostalCode(custDto.getPostalCode());
//			model.setSalesRepEmployeeNumber(0);
//			model.setState(custDto.getState());
//			CustomerModel modelRes = custRepo.save(model);
			CustomerModel modelRes = new CustomerModel();
			if(modelRes!=null) {
				String message = " Created new customer : ";
				notifyToCustomerUsingKafka(message);
				
			}
		}catch(Exception e) {
			System.out.println(e.getMessage());
		}
		return resDto;
	}
	
	public void notifyToCustomerUsingKafka(String message) {
		kafkaTemplate.send(topicName,message,message);
	}
}
