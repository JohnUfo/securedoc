package online.muydinov.securedoc.service;

import org.springframework.stereotype.Service;

public interface EmailService {
    void sendNewAccountEmail(String name, String email, String token);

    void sendPasswordResetEmail(String name, String email, String token);


}
