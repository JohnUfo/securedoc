package online.muydinov.securedoc.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import online.muydinov.securedoc.entity.ConfirmationEntity;
import online.muydinov.securedoc.entity.CredentialEntity;
import online.muydinov.securedoc.entity.RoleEntity;
import online.muydinov.securedoc.entity.UserEntity;
import online.muydinov.securedoc.enumeration.Authority;
import online.muydinov.securedoc.enumeration.EventType;
import online.muydinov.securedoc.event.UserEvent;
import online.muydinov.securedoc.exception.ApiException;
import online.muydinov.securedoc.repository.ConfirmationRepository;
import online.muydinov.securedoc.repository.CredentialRepository;
import online.muydinov.securedoc.repository.RoleRepository;
import online.muydinov.securedoc.repository.UserRepository;
import online.muydinov.securedoc.service.UserService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.Map;

import static online.muydinov.securedoc.utils.UserUtils.createUserEntity;

@Service
@RequiredArgsConstructor
@Transactional(rollbackOn = Exception.class)
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final CredentialRepository credentialRepository;
    private final ConfirmationRepository confirmationRepository;
//    private final BCryptPasswordEncoder encoder;
    private final ApplicationEventPublisher publisher;

    @Override
    public void createUser(String firstName, String lastName, String email, String password) {
        var userEntity = userRepository.save(createNewUser(firstName,lastName,email,password));
        var credentialEntity = new CredentialEntity(userEntity, password);
        credentialRepository.save(credentialEntity);
        var confirmation = new ConfirmationEntity(userEntity);
        confirmationRepository.save(confirmation);
        publisher.publishEvent(new UserEvent(userEntity, EventType.REGISTRATION, Map.of("key", confirmation.getKey())));
    }

    @Override
    public RoleEntity getRoleName(String name) {
        var role = roleRepository.findByNameIgnoreCase(name);
        return role.orElseThrow(() -> new ApiException("Role not found"));
    }

    private UserEntity createNewUser(String firstName, String lastName, String email, String password) {
        var role = getRoleName(Authority.USER.name());
        return createUserEntity(firstName,lastName,email,role);
    }
}
