package htl.steyr.mygrover.repository;

import htl.steyr.mygrover.model.Brand;
import htl.steyr.mygrover.model.Customer;
import htl.steyr.mygrover.model.Model;
import htl.steyr.mygrover.model.Rental;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface RentalRepository extends JpaRepository<Rental, Long> {
    List<Rental> findAllByActiveIsTrue();
}
