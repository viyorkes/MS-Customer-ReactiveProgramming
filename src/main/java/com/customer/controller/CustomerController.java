package com.customer.controller;

import com.customer.document.Customer;
import com.customer.repository.CustomerRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import static com.customer.constants.CustomerConstants.CUSTOMER_END_POINT;

@RestController
@Slf4j
public class CustomerController {

    @Autowired
    CustomerRepository customerRepository;

    @GetMapping(CUSTOMER_END_POINT)
    public Flux < Customer > getAllItems() {
        return customerRepository.findAll();

    }

}
