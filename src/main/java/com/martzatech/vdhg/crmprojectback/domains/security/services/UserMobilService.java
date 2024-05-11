package com.martzatech.vdhg.crmprojectback.domains.security.services;

import com.martzatech.vdhg.crmprojectback.application.exceptions.BusinessRuleException;
import com.martzatech.vdhg.crmprojectback.application.exceptions.not_found_exceptions.UserNotFoundException;
import com.martzatech.vdhg.crmprojectback.domains.security.mappers.UserMapper;
import com.martzatech.vdhg.crmprojectback.domains.security.mappers.UserMobilMapper;
import com.martzatech.vdhg.crmprojectback.domains.security.models.User;
import com.martzatech.vdhg.crmprojectback.domains.security.models.UserMobil;
import com.martzatech.vdhg.crmprojectback.domains.security.models.UserNew;
import com.martzatech.vdhg.crmprojectback.domains.security.repositories.UserMobilRepository;
import com.martzatech.vdhg.crmprojectback.infrastructure.configs.Jwt;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;

@AllArgsConstructor
@Slf4j
@Service
public class UserMobilService {

    private UserMobilRepository repository;
    private UserMobilMapper mapper;
    private UserService userService;
    private UserMapper userMapper;
    private Jwt jwt;

    @Transactional
    public UserMobil save(final UserMobil model) {
        if (Objects.requireNonNullElse(model.getId(), 0) != 0) {
            existsById(model.getId());
        }
        return mapper.entityToModel(repository.save(mapper.modelToEntity(model)));
    }

    public UserMobil findById(final Integer id) {
        return mapper.entityToModel(repository.findById(id).orElseThrow(() -> new UserNotFoundException(id)));
    }

    public void existsById(final Integer id) {
        if (!repository.existsById(id)) {
            throw new UserNotFoundException(id);
        }
    }

    public UserMobil findByUsername(final String email) {
        User user = userService.findByEmail(email);
        return mapper.entityToModel(repository.findByUser(userMapper.modelToEntity(user)));
    }

    public UserMobil findByUsernameMobile(final String email) {
        log.info("findByUsernameMobile in email : "+email);
        User user = userService.findByEmailMobile(email);
        return mapper.entityToModel(repository.findByUser(userMapper.modelToEntity(user)));
    }

    public void deleteById(final Integer id){
        existsById(id);
        repository.deleteById(id);
    }

    public UserNew verifyPassword(String userEmail, String password) {
        UserNew userNew = null;
        UserMobil userMobil = findByUsernameMobile(userEmail);
        if(Objects.nonNull(userMobil)){
            if (userMobil.getPassword().equals(password)) {
                log.info("Password Valid");
                userNew = buildUserNewMobil(userEmail, userMobil);
            }
        }

        if(Objects.isNull(userNew)){
            log.error("Password not valid : User not found, verify your email :  "+userEmail+" or password: "+password);
            throw new BusinessRuleException("User not found, verify your email.");
        }

        return userNew;
    }

    private UserNew buildUserNewMobil(String userEmail, UserMobil userMobil) {
        return UserNew.builder()
                .name(userMobil.getUser().getName())
                .mobile(userMobil.getUser().getMobile())
                .email(userEmail).id(userMobil.getId())
                .typeUser(userMobil.getUser().getTypeUser())
                .surname(userMobil.getUser().getSurname())
                .token(jwt.create(String.valueOf(userMobil.getId()),userMobil.getUser().getName(), userEmail)).build();
    }



}
