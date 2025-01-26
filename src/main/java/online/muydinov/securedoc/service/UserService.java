package online.muydinov.securedoc.service;

import online.muydinov.securedoc.entity.RoleEntity;

public interface UserService {
    void createUser(String firstName, String lastName, String email, String password);
    RoleEntity getRoleName(String name);
}
