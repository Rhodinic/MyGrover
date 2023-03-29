package htl.steyr.mygrover.repository;

import htl.steyr.mygrover.model.Brand;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BrandRepository extends JpaRepository<Brand, Long> {
    Brand findFirstByOrderByIdDesc();
}
