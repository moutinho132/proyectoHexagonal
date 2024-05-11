package com.martzatech.vdhg.crmprojectback.domains.security.services;

import com.martzatech.vdhg.crmprojectback.application.constants.CommonConstants;
import com.martzatech.vdhg.crmprojectback.application.enums.UserTypeEnum;
import com.martzatech.vdhg.crmprojectback.application.exceptions.BusinessRuleException;
import com.martzatech.vdhg.crmprojectback.application.exceptions.FieldIsDuplicatedException;
import com.martzatech.vdhg.crmprojectback.application.exceptions.not_found_exceptions.NotFoundException;
import com.martzatech.vdhg.crmprojectback.application.exceptions.not_found_exceptions.UserNotFoundException;
import com.martzatech.vdhg.crmprojectback.domains.customers.specifications.UserSpecification;
import com.martzatech.vdhg.crmprojectback.domains.security.entities.UserEntity;
import com.martzatech.vdhg.crmprojectback.domains.security.mappers.UserMapper;
import com.martzatech.vdhg.crmprojectback.domains.security.models.CustomerAssociate;
import com.martzatech.vdhg.crmprojectback.domains.security.models.User;
import com.martzatech.vdhg.crmprojectback.domains.security.models.UserNew;
import com.martzatech.vdhg.crmprojectback.domains.security.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.martzatech.vdhg.crmprojectback.application.helper.SpecificationHelper.WhiteTypeAssociated;

@AllArgsConstructor
@Slf4j
@Service
public class UserService {

    public static final int DEPARTMENT = 36;
    private UserRepository repository;
    private UserMapper mapper;

    public User save(final User model) {
        if (Objects.requireNonNullElse(model.getId(), 0) != 0) {
            existsById(model.getId());
        } else {
            existsByEmail(model.getEmail());
        }

        return mapper.entityToModel(repository.save(mapper.modelToEntity(model)));
    }


    public Long count(final UserSpecification specification) {
        return repository.count(specification);
    }

    public List<User> findAll() {
        return mapper.entitiesToModelList(repository
                .findAll(Sort.by(CommonConstants.EMAIL_FIELD))
                .stream().filter(entity -> entity.getTypeUser().equals(UserTypeEnum.CRM.name()))
                .toList());//TODO : colocar Enum
    }

    public User findById(final Integer id) {
        log.info("findById User Id:{}",id);
        return mapper.entityToModel(repository.findById(id).orElseThrow(() -> new UserNotFoundException(id)));
    }

    public User findByIdCustomer(final Integer idCustomer) {
        UserEntity user = null;
        Optional<UserEntity> userOptional = repository.findByIdCustomer(idCustomer);
        if (userOptional.isPresent()) {
            user = userOptional.get();
        }
        return mapper.entityToModel(user);
    }

    public User findByIdAssociated(final Integer idAssociate) {
        UserEntity user = null;
        Optional<UserEntity> userOptional = repository.findByIdAssociated(idAssociate);
        if (userOptional.isPresent()) {
            user = userOptional.get();
        }
        return mapper.entityToModel(user);
    }

    public User findByEmail(final String email) {
        log.info("FinBy Email User :{}",email);
        return mapper.entityToModel(repository.findByEmail(email).orElseThrow(() -> new BusinessRuleException("User not found, verify your email or password")));
    }

    public User findByEmailMobile(final String email) {
        log.info("findByEmailMobile email:"+email);
        return mapper.entityToModel(repository.findByEmailMobile(email)
                .orElseThrow(() -> new BusinessRuleException("User not found, verify your email or password")));
    }

    public UserNew findByEmailUserMobil(final String email) {
        return mapper.entityToModelNew(repository.findByEmail(email).orElseThrow(NotFoundException::new));
        //return mapper.entityToModelNew(repository.findByEmail(email));

    }

    public List<User> findByRole(final Integer roleId) {
        return repository.findByRole(roleId).stream()
                .map(entity ->
                        User.builder()
                                .id(entity.getId())
                                .title(entity.getTitle())
                                .name(entity.getName())
                                .surname(entity.getSurname())
                                .email(entity.getEmail())
                                .address(entity.getAddress())
                                .mobile(entity.getMobile())
                                .build()
                ).toList();
    }

    @Transactional
    public void deleteById(final Integer id) {
        existsById(id);

        repository.findById(id).ifPresent(entity -> repository.save(entity.withRoles(new ArrayList<>())));

        repository.deleteById(id);
    }

    @Transactional
    public void deleteByStatusAndId(final Integer id) {
        existsById(id);

        repository.findById(id).ifPresent(entity -> repository.save(entity.withRoles(new ArrayList<>())));

        repository.deleteUserByStatus(id);
    }

    @Transactional
    public void activeUser(final Integer id) {
        existsById(id);
        repository.activeUser(id);
    }

    public void existsById(final Integer id) {
        if (!repository.existsById(id)) {
            throw new UserNotFoundException(id);
        }
    }

    private void existsByEmail(final String email) {
        if (repository.existsByEmailIgnoreCase(email)) {
            throw new FieldIsDuplicatedException(CommonConstants.EMAIL_FIELD);
        }
    }

    private static final Specification<CustomerAssociate> WITH_OUT_CONVERTED = (root, query, criteriaBuilder) ->
            criteriaBuilder.notEqual(
                    root.<Integer>get("typeUser"), UserTypeEnum.CUSTOMER
            );

    public List<CustomerAssociate> findAllCustomerAssociate(final UserSpecification specification,
                                                            final Pageable pageable) {
        return mapper.entitiesToModelList(repository
                        .findAll(WhiteTypeAssociated(UserTypeEnum.CUSTOMER.name(), specification)
                                .and(specification), pageable)
                        .toList()).stream()
                .map(user -> CustomerAssociate
                        .builder()
                        .id(user.getId())
                        .title(user.getTitle())
                        .name(user.getName())
                        .surname(user.getSurname())
                        .mobile(user.getMobile())
                        .address(user.getAddress())
                        .email(user.getEmail())
                        .creationUser(user.getCreationUser())
                        .creationTime(user.getCreationTime())
                        .nationality(user.getNationality())
                        .status(user.getStatus())
                        .typeUser(user.getTypeUser())
                        .lastLogin(user.getLastLogin())
                        .build()
                ).collect(Collectors.toList());
    }
}
