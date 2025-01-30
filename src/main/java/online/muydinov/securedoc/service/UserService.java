package online.muydinov.securedoc.service;

import online.muydinov.securedoc.entity.RoleEntity;
import online.muydinov.securedoc.enumeration.LoginType;

public interface UserService {
    void createUser(String firstName, String lastName, String email, String password);
    RoleEntity getRoleName(String name);
    void verifyAccountKey(String key);
    void updateLoginAttempt(String email, LoginType loginType);
}
