package com.customer.controller;

import com.customer.constants.CustomerConstants;
import com.customer.document.Customer;
import com.customer.initialize.CustomerDataInitializer;
import com.customer.repository.CustomerRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static com.customer.constants.CustomerConstants.CUSTOMER_END_POINT;

@RestController
@Slf4j
@Profile("!teste")
public class CustomerController {

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    CustomerDataInitializer customerDataInitializer;

    @GetMapping("/customers")
    public Flux <Customer> getAllCustomers() {
        return customerRepository.deleteAll()
                .thenMany(Flux.fromIterable(customerDataInitializer.data()))
                .flatMap(customerRepository::save);

    }

    @GetMapping(CUSTOMER_END_POINT + "/{id}")
    public Mono<ResponseEntity<Customer>> getOneCustomer(@PathVariable String id){
        return customerRepository.findById(id)
                .map((customer)-> new ResponseEntity<>(customer, HttpStatus.OK))
                .defaultIfEmpty((new ResponseEntity<>(HttpStatus.NOT_FOUND)));
    }



}
