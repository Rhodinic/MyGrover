package htl.steyr.mygrover.controller;

import htl.steyr.mygrover.TokenHandler;
import htl.steyr.mygrover.model.Customer;
import htl.steyr.mygrover.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@CrossOrigin
@org.springframework.web.bind.annotation.RestController
@RequestMapping("/customer")
public class CustomerRestController {

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    TokenHandler tokenHandler;

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Void> createCustomer(@RequestBody Customer customer, @RequestHeader("authorization") String token){

        customerRepository.save(customer);

        return ResponseEntity.ok(null);
    }

    @PostMapping("/update")
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Void> updateCustomer(@RequestBody Customer customer, @RequestHeader("authorization") String token){
        // If the customer does not exist
        if(customerRepository.findById(customer.getId()).isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        Customer updatedCustomer = customerRepository.findById(customer.getId()).get();
        updatedCustomer.setName(customer.getName());
        updatedCustomer.setEmail(customer.getEmail());

        customerRepository.save(updatedCustomer);

        return ResponseEntity.ok(null);
    }

    @DeleteMapping("/delete/{id}")
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long id, @RequestHeader("authorization") String token){
        // If the customer does not exist
        if(customerRepository.findById(id).isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        customerRepository.deleteById(id);

        return ResponseEntity.ok(null);
    }

    @GetMapping(value = {"/load", "/load/{id}"})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<List<Customer>> loadCustomer(@PathVariable(required = false) Long id, @RequestHeader("authorization") String token){
        if(id != null){
            // If the customer does not exist
            if(customerRepository.findById(id).isEmpty()){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }

            List<Customer> result = new ArrayList<>(Collections.emptyList());

            result.add(customerRepository.findById(id).get());

            return ResponseEntity.ok(result);
        }else{
            return ResponseEntity.ok(customerRepository.findAll());
        }
    }
}
