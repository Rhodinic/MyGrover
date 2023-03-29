package htl.steyr.mygrover.controller;

import htl.steyr.mygrover.TokenHandler;
import htl.steyr.mygrover.model.User;
import htl.steyr.mygrover.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Date;

@CrossOrigin
@org.springframework.web.bind.annotation.RestController
public class LoginRestController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    TokenHandler tokenHandler;

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<String> login(@RequestBody User user) {
        User result = userRepository.findFirstByEmailAndPassword(user.getEmail(), user.getPassword());

        String token = tokenHandler.generateToken(result);

        result.setToken(token);
        result.setTokenCreatedAt(new Date(System.currentTimeMillis()));

        userRepository.save(result);

        return ResponseEntity.status(HttpStatus.OK).body("\"" + token + "\"");
    }
}
