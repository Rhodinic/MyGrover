package htl.steyr.mygrover.repository;

import htl.steyr.mygrover.model.Rental;
import htl.steyr.mygrover.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findFirstByEmailAndPassword(String email, String password);

    User findFirstByToken(String token);
}
