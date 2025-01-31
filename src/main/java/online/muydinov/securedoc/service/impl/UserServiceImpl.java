package online.muydinov.securedoc.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import online.muydinov.securedoc.cache.CacheStore;
import online.muydinov.securedoc.domain.RequestContext;
import online.muydinov.securedoc.dto.User;
import online.muydinov.securedoc.entity.ConfirmationEntity;
import online.muydinov.securedoc.entity.CredentialEntity;
import online.muydinov.securedoc.entity.RoleEntity;
import online.muydinov.securedoc.entity.UserEntity;
import online.muydinov.securedoc.enumeration.Authority;
import online.muydinov.securedoc.enumeration.EventType;
import online.muydinov.securedoc.enumeration.LoginType;
import online.muydinov.securedoc.event.UserEvent;
import online.muydinov.securedoc.exception.ApiException;
import online.muydinov.securedoc.repository.ConfirmationRepository;
import online.muydinov.securedoc.repository.CredentialRepository;
import online.muydinov.securedoc.repository.RoleRepository;
import online.muydinov.securedoc.repository.UserRepository;
import online.muydinov.securedoc.service.UserService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;

import static online.muydinov.securedoc.utils.UserUtils.createUserEntity;
import static online.muydinov.securedoc.utils.UserUtils.fromUserEntity;

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
    private final CacheStore<String, Integer> userCache;
    private final ApplicationEventPublisher publisher;

    @Override
    public void createUser(String firstName, String lastName, String email, String password) {
        var userEntity = userRepository.save(createNewUser(firstName, lastName, email));
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

    @Override
    public void verifyAccountKey(String key) {
        var confirmationEntity = getUserConfirmation(key);
        var userEntity = getUserEntityByEmail(confirmationEntity.getUserEntity().getEmail());
        userEntity.setEnabled(true);
        userRepository.save(userEntity);
        confirmationRepository.delete(confirmationEntity);
    }

    @Override
    public void updateLoginAttempt(String email, LoginType loginType) {
        var userEntity = getUserEntityByEmail(email);
        RequestContext.setUserId(userEntity.getId());
        switch (loginType) {
            case LOGIN_ATTEMPT -> {
                if (userCache.get(email) == null) {
                    userEntity.setLoginAttempts(0);
                    userEntity.setAccountNonLocked(true);
                }
                userEntity.setLoginAttempts(userEntity.getLoginAttempts() + 1);
                userCache.put(email, userEntity.getLoginAttempts());
                if (userCache.get(email) > 5) {
                    userEntity.setAccountNonLocked(false);
                }
            }
            case LOGIN_SUCCESS -> {
                userEntity.setAccountNonLocked(true);
                userEntity.setLoginAttempts(0);
                userEntity.setLastLogin(LocalDateTime.now());
                userCache.evict(email);
            }
        }
    }

    @Override
    public User getUserByUserId(String userId) {
        var userEntity = userRepository.findUserByUserId(userId).orElseThrow(() -> new ApiException("User not found"));
        return fromUserEntity(userEntity, userEntity.getRole(), getUserCredentialById(userEntity.getId()));
    }

    @Override
    public User getUserByEmail(String email) {
        UserEntity userEntity = getUserEntityByEmail(email);
        return fromUserEntity(userEntity, userEntity.getRole(), getUserCredentialById(userEntity.getId()));
    }

    @Override
    public CredentialEntity getUserCredentialById(Long userId) {
        var credentialById = credentialRepository.getCredentialByUserEntityId(userId);
        return credentialById.orElseThrow(() -> new ApiException("Unable to find user credential"));
    }

    private UserEntity getUserEntityByEmail(String email) {
        var userByEmail = userRepository.findByEmailIgnoreCase(email);
        return userByEmail.orElseThrow(() -> new ApiException("User not found"));
    }

    private ConfirmationEntity getUserConfirmation(String key) {
        return confirmationRepository.findByKey(key).orElseThrow(() -> new ApiException("Confirmation key not found"));
    }

    private UserEntity createNewUser(String firstName, String lastName, String email) {
        var role = getRoleName(Authority.USER.name());
        return createUserEntity(firstName, lastName, email, role);
    }
}
