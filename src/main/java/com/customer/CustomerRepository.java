package com.customer;

import com.customer.document.Customer;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface CustomerRepository extends ReactiveMongoRepository<Customer, String> {
}
