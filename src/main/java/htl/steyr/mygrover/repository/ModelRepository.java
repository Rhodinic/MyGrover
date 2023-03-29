package htl.steyr.mygrover.repository;

import htl.steyr.mygrover.model.Brand;
import htl.steyr.mygrover.model.Model;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface ModelRepository extends JpaRepository<Model, Long> {
    List<Model> findAllByBrand_Id(Long brand_id);

    @Query(value = "SELECT * FROM model m WHERE m.id not IN(SELECT model_id FROM rental WHERE rental.active = TRUE AND ((?1 >= from_date AND ?1 <= to_date) OR (?2 >= from_date AND ?2 <= to_date) OR (?1 <= from_date AND ?2 >= to_date)))",
            nativeQuery = true)
    List<Model> findAvailableModels(Date fromDate, Date toDate);

    @Query(value = "SELECT * FROM model m WHERE m.id IN(SELECT model_id FROM rental WHERE (?1 >= from_date AND ?1 <= to_date) OR (?2 >= from_date AND ?2 <= to_date) OR (?1 <= from_date AND ?2 >= to_date)) AND m.id = ?3",
            nativeQuery = true)
    Optional<Model> findModelRentedInTimePeriod(Date fromDate, Date toDate, Long modelId);

    Model findFirstByOrderByIdDesc();
}
