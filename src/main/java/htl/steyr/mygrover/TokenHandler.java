package htl.steyr.mygrover;

import htl.steyr.mygrover.model.User;
import htl.steyr.mygrover.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

@Component
public class TokenHandler {

    @Autowired
    UserRepository userRepository;

    public String generateToken(User user)  {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update((user.getId() + user.getEmail() + System.currentTimeMillis()).getBytes());
            byte[] digiest = messageDigest.digest();

            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < digiest.length; i++) {
                hexString.append(Integer.toHexString(0xFF & digiest[i]));
            }

            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isTokenValid(String token){
        User result = userRepository.findFirstByToken(token);

        if(result == null){
            return false;
        }

        return new Date(System.currentTimeMillis()).getTime() - result.getTokenCreatedAt().getTime() <= 600000;
    }
}
