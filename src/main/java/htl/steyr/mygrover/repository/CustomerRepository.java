package htl.steyr.mygrover.repository;

import htl.steyr.mygrover.model.Brand;
import htl.steyr.mygrover.model.Customer;
import htl.steyr.mygrover.model.Model;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    Customer findFirstByOrderByIdDesc();
}
