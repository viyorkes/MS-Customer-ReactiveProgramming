package com.customer.repository;

import com.customer.document.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

@DataMongoTest
@RunWith(SpringRunner.class)
public class CustomerRepositoryTest {

    @Autowired
    CustomerRepository customerRepository;

    List <Customer> customerList = Arrays.asList(
            new Customer(null, "jose", "12345678", 20),
            new Customer(null, "victor", "99999999", 25),
            new Customer(null, "dani", "12345678", 20),
            new Customer(null, "leo", "12345679", 7),
            new Customer("ABC", "jujuba", "12345679", 7)
    );

    @BeforeEach
    public void setUp() {
        customerRepository
                .deleteAll()
                .thenMany(Flux.fromIterable(customerList))
                .flatMap(customerRepository::save)
                .doOnNext((customer -> {
                    System.out.println("inserted Customer:" + customer);
                })).blockLast();

    }

    @Test
    public void getAllCustomers() {
        StepVerifier.create(customerRepository.findAll())
                .expectSubscription()
                .expectNextCount(5)
                .verifyComplete();
    }

    @Test
    public void getCustomerById() {
        StepVerifier.create(customerRepository.findById("ABC"))
                .expectSubscription()
                .expectNextMatches((customer -> customer.getName().equals("jujuba")))
                .verifyComplete();
    }

    @Test
    public void findCustomerByName() {
        StepVerifier.create(customerRepository.findByName("jujuba").log("findCustomerByName"))
                .expectSubscription()
                .expectNextCount(1)
                .verifyComplete();

    }

    @Test
    public void saveCustomer() {

        Customer customer = new Customer("1", "Jose", "999999999", 26);

        Mono <Customer> saveCustomer = customerRepository.save(customer);
        StepVerifier.create(saveCustomer.log("saveCustomer"))
                .expectSubscription()
                .expectNextMatches(customer1 -> (customer1.getId() != null && customer1.getName().equals("Jose")))
                .verifyComplete();

    }

    @Test
    public void updateCustomer() {

        String newPhoneNumber = "8888888888";
        Mono <Customer> updateCustomer = customerRepository.findByName("victor").log()
                .map(customer -> {
                    customer.setPhoneNumber(newPhoneNumber);
                    return customer;
                })
                .flatMap(customer -> {
                    return customerRepository.save(customer);
                });

        StepVerifier.create(updateCustomer.log("updated"))
                .expectSubscription()
                .expectNextMatches(customer -> customer.getPhoneNumber().equals("8888888888"))
                .verifyComplete();

    }

    @Test
    public void deleteItemById() {

        Mono <Void> deleteCustomer = customerRepository.findById("ABC")
                .map(Customer::getId).log("get ID")
                .flatMap((id) -> {
                    return customerRepository.deleteById(id);
                });

        StepVerifier.create(deleteCustomer.log("ID deleted"))
                .expectSubscription()
                .verifyComplete();


        StepVerifier.create(customerRepository.findAll().log("updated list"))
                .expectSubscription()
                .expectNextCount(4)
                .verifyComplete();

    }

}