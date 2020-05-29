package com.project.LoginService.LoginService.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.project.LoginService.LoginService.entity.CustomerEntity;
import com.project.LoginService.LoginService.pojo.CustomerPojo;
import com.project.LoginService.LoginService.service.CustomerService;

import io.swagger.annotations.Api;

@CrossOrigin
@RestController
//@RequestMapping("rewards")
@Api
public class CustomerController {
	
	static Logger LOG = Logger.getLogger(CustomerController.class.getClass());
	
	@Autowired
	
	CustomerService customerService;
	ResponseEntity responseEntity;
	
	// Rest EndPoint to validate Customer
	@GetMapping("validate")
	
	ResponseEntity<?> validateCustomer(@RequestHeader("Authorization") String data) {
		
		CustomerPojo customerPojo = new CustomerPojo();
		
	try {
			
		
		LOG.info("Entered the end point \'validate'");
		String token[] = data.split(":");
		customerPojo.setUsername(token[0]);
		customerPojo.setPassword(token[1]);
		customerService.validateCustomer(customerPojo);
		LOG.info("exited the end Point \'validate'");
		responseEntity = new ResponseEntity<String>("Successfully validated",HttpStatus.CREATED);
		}
		catch (Exception e) {
			responseEntity = new ResponseEntity<String>("Username or Password is incorrect", HttpStatus.NOT_FOUND);
			
		}
		return responseEntity;
	}
	/*@GetMapping("validate")
	
	CustomerPojo validateCustomer(@RequestHeader("Authorization") String data)
	{
		
		
		LOG.info("Entered the end point \'validate'");
		LOG.info("exited the end Point \'validate'");
		String token[] = data.split(":");
		CustomerPojo customerPojo = new CustomerPojo();
		customerPojo.setUsername(token[0]);
		customerPojo.setPassword(token[1]);
		return customerService.validateCustomer(customerPojo);
		
	}*/
	
	
	
	//End Point to update the reward points
	@PutMapping("/update")
	@HystrixCommand(fallbackMethod = "fallBackRewards")
	public Boolean updateRewards(@RequestBody CustomerEntity customerEntity) throws InterruptedException{
	
		LOG.info("entered the end point \'/update'");
		LOG.info("exited the end point\'update'");
        //Thread.sleep(3000);
		return customerService.updateRewards(customerEntity);
	}
	
	public Boolean fallBackRewards(CustomerEntity customerEntity) {
		LOG.info("FallBack is invoked. Some service is down or not responding");
		return false;
	}
	}