package com.customer.repository;

import com.customer.CustomerRepository;
import com.customer.document.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

@DataMongoTest
@RunWith(SpringRunner.class)
public class CustomerRepositoryTest {

    @Autowired
    CustomerRepository customerRepository;

    List < Customer > customerList = Arrays.asList(
            new Customer(null, "jose", "12345678", 20),
            new Customer(null, "victor", "12345679", 25),
            new Customer(null, "dani", "12345678", 20),
            new Customer(null, "leo", "12345679", 7),
            new Customer(null, "jujuba", "12345679", 7)
    );

    @BeforeEach
    public void setUp() {
        customerRepository
                .deleteAll()
                .thenMany(Flux.fromIterable(customerList))
                .flatMap(customerRepository::save)
                .doOnNext((customer -> {
                    System.out.println("inserted Item:" + customer);
                })).blockLast();

    }

    @Test
    public void getAllCustomers() {

        StepVerifier.create(customerRepository.findAll())
                .expectSubscription()
                .expectNextCount(5)
                .verifyComplete();
    }

}