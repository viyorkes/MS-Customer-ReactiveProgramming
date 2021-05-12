package com.customer.initialize;

import com.customer.document.Customer;
import com.customer.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.Arrays;
import java.util.List;

@Component
public class CustomerDataInitializer implements CommandLineRunner {

    @Autowired
    CustomerRepository customerRepository;

    @Override
    public void run(String...args) throws Exception {

        initialDataSetup();

    }

    public List <Customer> data() {

        return Arrays.asList(new Customer(null, "jose", "12345678", 20),
                new Customer(null, "victor", "99999999", 25),
                new Customer(null, "dani", "12345678", 20),
                new Customer(null, "leo", "12345679", 7),
                new Customer("ABC", "jujuba", "12345679", 7));
    }

    private void initialDataSetup() {

        customerRepository.deleteAll()
                .thenMany(Flux.fromIterable(data()))
                .flatMap(customerRepository::save)
                .thenMany(customerRepository.findAll())
                .subscribe((customer -> {
                    System.out.println("Insert Item from Command Line Runner" + customer);
                }));

    }
}