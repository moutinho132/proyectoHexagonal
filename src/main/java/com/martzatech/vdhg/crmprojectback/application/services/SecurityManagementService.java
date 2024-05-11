package com.martzatech.vdhg.crmprojectback.application.services;

import com.martzatech.vdhg.crmprojectback.application.constants.CommonConstants;
import com.martzatech.vdhg.crmprojectback.application.enums.DeletedStatusEnum;
import com.martzatech.vdhg.crmprojectback.application.enums.UserStatusEnum;
import com.martzatech.vdhg.crmprojectback.application.enums.UserTypeEnum;
import com.martzatech.vdhg.crmprojectback.application.exceptions.BusinessRuleException;
import com.martzatech.vdhg.crmprojectback.application.exceptions.FieldIsMissingException;
import com.martzatech.vdhg.crmprojectback.domains.customers.specifications.UserSpecification;
import com.martzatech.vdhg.crmprojectback.domains.security.mappers.UserMapper;
import com.martzatech.vdhg.crmprojectback.domains.security.models.*;
import com.martzatech.vdhg.crmprojectback.domains.security.services.*;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.martzatech.vdhg.crmprojectback.application.helper.SpecificationHelper.WhiteTypeAssociated;

@AllArgsConstructor
@Slf4j
@Service
public class SecurityManagementService {

    public static final String DEPARTMENTS_CREATE_ALL = "DEPARTMENTS_CREATE_ALL";
    private final AccessGroupService accessGroupService;
    private final UserService userService;
    private final UserMobilService mobilService;
    private final RoleService roleService;
    private final DepartmentService departmentService;
    private final SubsidiaryService subsidiaryService;
    private final UserMapper userMapper;

    public Department saveDepartment(final Department model) {
        return departmentService.save(buildDepartment(model));
    }

    private Department buildDepartment(final Department model) {
        validDepartment(model);

        final Department department = Objects.nonNull(model.getId())
                ? departmentService.findById(model.getId())
                : model;
        return department
                .withName(model.getName())
                .withDescription(model.getDescription())
                .withSubsidiary(buildSubsidiary(model.getSubsidiary()))
                .withCreationTime(
                        Objects.nonNull(department.getCreationTime())
                                ? department.getCreationTime()
                                : LocalDateTime.now()
                )
                .withModificationTime(
                        Objects.isNull(model.getId())
                                ? null
                                : LocalDateTime.now()
                )
                .withStatus(
                        Objects.isNull(model.getStatus())
                                ? DeletedStatus.builder().id(DeletedStatusEnum.ACTIVE.getId()).build()
                                : model.getStatus()
                )
                .withCreationUser(
                        Objects.nonNull(department.getCreationUser())
                                ? department.getCreationUser()
                                : findCurrentUser()
                )
                .withModificationUser(
                        Objects.isNull(model.getId())
                                ? null
                                : findCurrentUser()
                );
    }

    private Subsidiary buildSubsidiary(final Subsidiary model) {
        if (!Objects.isNull(model)) {
            if (Objects.isNull(model.getId())) {
                if (StringUtils.isNoneEmpty(model.getName())) {
                    return subsidiaryService.findByName(model.getName()).orElse(subsidiaryService.save(model));
                }
            }
            return subsidiaryService.findById(model.getId());
        }
        throw new FieldIsMissingException(CommonConstants.SUBSIDIARY_FIELD);
    }

    public Role saveRole(final Role model) {
        validateRole(model);
        return roleService.save(buildRole(model));
    }

    public UserMobil findById(final Integer id) {
        return mobilService.findById(id);
    }

    private Role buildRole(final Role model) {
        final Role role = Objects.nonNull(model.getId())
                ? roleService.findById(model.getId())
                : model;
        return role
                .withName(model.getName())
                .withDescription(model.getDescription())
                .withDepartment(model.getDepartment())
                .withRole(model.getRole())
                .withPermissions(
                        Objects.isNull(model.getId()) && Objects.nonNull(model.getRole())
                                ? roleService.findById(model.getRole().getId()).getPermissions()
                                : model.getPermissions())
                .withCreationTime(
                        Objects.nonNull(model.getCreationTime())
                                ? model.getCreationTime()
                                : LocalDateTime.now())
                .withModificationTime(
                        Objects.isNull(model.getId())
                                ? null
                                : LocalDateTime.now()
                )
                .withStatus(
                        Objects.isNull(model.getStatus())
                                ? DeletedStatus.builder().id(DeletedStatusEnum.ACTIVE.getId()).build()
                                : model.getStatus()
                )
                .withCreationUser(
                        Objects.nonNull(model.getCreationUser())
                                ? model.getCreationUser()
                                : findCurrentUser()
                )
                .withModificationUser(
                        Objects.isNull(model.getId())
                                ? null
                                : findCurrentUser()
                );
    }

    private void validateRole(final Role model) {
        validDepartment(model.getDepartment());
        validClonedRole(model.getRole());
    }

    private void validClonedRole(final Role role) {
        if (Objects.nonNull(role) && Objects.nonNull(role.getId())) {
            roleService.existsById(role.getId());
        }
    }

    public boolean canCreatePermission(final User currentUser) {
        return currentUser.getRoles()
                .stream()
                .flatMap(role -> role.getPermissions().stream())
                .anyMatch(permission -> DEPARTMENTS_CREATE_ALL.equalsIgnoreCase(permission.getName()));
    }

    private void validDepartment(final Department department) {
        if (Objects.isNull(department)) {
            throw new FieldIsMissingException(CommonConstants.DEPARTMENT_FIELD);
        }
        User currentUser = findCurrentUser();
        if (!canCreatePermission(currentUser)) {
            throw new BusinessRuleException(
                    String.format("Current user with id %s, cannot update this department.", currentUser.getId())
            );
        }


    }
    public UserMobil findByEmail(final String userName){
       return mobilService.findByUsername(userName);
    }

    public Department findDepartmentById(final Integer id) {
        final Department department = departmentService.findById(id);
        return department
                .withUsers(
                        CollectionUtils.isEmpty(department.getRoles())
                                ? null
                                : department.getRoles().stream()
                                .flatMap(role -> userService.findByRole(role.getId()).stream())
                                .toList()
                );
    }

    public Role findRoleById(final Integer id) {
        final Role role = roleService.findById(id);

        final List<Permission> allPrivilegesOfTheDepartment = accessGroupService.findAll()
                .stream()
                .flatMap(ag -> ag.getAccesses().stream())
                .flatMap(a -> a.getPrivileges().stream())
                .flatMap(p -> p.getPermissions().stream())
                .toList();

        return role
                .withPermissions(
                        allPrivilegesOfTheDepartment
                                .stream()
                                .map(p ->
                                        p.withChecked
                                                (
                                                        !CollectionUtils.isEmpty(role.getPermissions())
                                                                && role.getPermissions().stream().anyMatch(i -> i.getId().intValue() == p.getId())
                                                )
                                )
                                .toList()
                )
                .withUsers(
                        userService.findByRole(id)
                );
    }

    public User findUserById(final Integer id) {
        return userService.findById(id);
    }

    public User saveUser(final User model) {
        validateUser(model);
        return userService.save(buildUser(model));
    }




    private void validateUser(final User model) {
        validateRoles(model.getRoles());
    }

    private void validateRoles(final List<Role> roles) {
        if (CollectionUtils.isEmpty(roles) || roles.stream().map(Role::getId).anyMatch(Objects::isNull)) {
            throw new FieldIsMissingException(CommonConstants.ROLES_FIELD);
        }
        roles.stream().map(Role::getId).forEach(roleService::existsById);
    }

    private User buildUser(final User model) {
        final User user = Objects.nonNull(model.getId())
                ? userService.findById(model.getId())
                : model;
        return user
                .withTitle(model.getTitle())
                .withName(model.getName())
                .withSurname(model.getSurname())
                .withEmail(model.getEmail())
                .withMobile(model.getMobile())
                .withAddress(model.getAddress())
                .withNationality(model.getNationality())
                .withRoles(model.getRoles())
                .withTypeUser(Objects.nonNull(model.getTypeUser()) ? model.getTypeUser() : "CRM")
                .withStatus(
                        Objects.isNull(model.getStatus())
                                ? UserStatus.builder().id(UserStatusEnum.ACTIVE.getId()).build()
                                : model.getStatus()
                )
                .withCreationTime(
                        Objects.nonNull(model.getCreationTime())
                                ? model.getCreationTime()
                                : LocalDateTime.now())
                .withModificationTime(
                        Objects.isNull(model.getId())
                                ? null
                                : LocalDateTime.now()
                )
                .withCreationUser(
                        Objects.nonNull(model.getCreationUser())
                                ? model.getCreationUser()
                                : findCurrentUser()
                )
                .withModificationUser(
                        Objects.isNull(model.getId())
                                ? null
                                : findCurrentUser()
                );
    }

    public List<Department> findAllDepartments() {
        return departmentService.findAll().stream()
                .map(department ->
                        department.withUsers(
                                CollectionUtils.isEmpty(department.getRoles())
                                        ? null
                                        : department.getRoles().stream()
                                        .flatMap(role -> userService.findByRole(role.getId()).stream())
                                        .toList()
                        )
                )
                .toList();
    }

    public List<Role> findAllRoles() {
        return roleService.findAll()
                .stream()
                .map(role -> role.withUsers(userService.findByRole(role.getId())))
                .toList();
    }

    public List<User> findAllUsers() {
        return userService.findAll()
                .stream()
                .collect(Collectors.toList());
    }

    public List<AccessGroup> findAllAccessGroup() {
        return accessGroupService.findAll();
    }

    public void deleteDepartmentById(final Integer id) {
        departmentService.deleteById(id);
    }

    public void deleteDepartmentByStatusAndId(final Integer id) {
        departmentService.deleteDepartmentByStatus(id);
    }

    public void deleteRoleById(final Integer id) {
        roleService.deleteById(id);
    }

    public void deleteRoleByStatusAndId(final Integer id) {
        roleService.deleteByStatusAndId(id);
    }

    public void deleteUserById(final Integer id) {
        userService.deleteById(id);
    }

    public void deleteUserByStatusAndId(final Integer id) {
        userService.deleteByStatusAndId(id);
    }

    public User findCurrentUser() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        final Jwt jwt = ((Jwt) authentication.getCredentials());
        final String email = (String) jwt.getClaims().get(CommonConstants.EMAIL_MS_KEY);
        return userService.findByEmail(email);
    }
    @Transactional
    public User updateLastLoginAndGetCurrentUser() {
        final User user = findCurrentUser();
        return userService.save(user.withLastLogin(LocalDateTime.now()));
    }

    public Long count(final UserSpecification specification) {
        return userService.count(specification);
    }

    public List<CustomerAssociate> findAllCustomer(final UserSpecification specification,
                                                   final Integer page, final Integer size,
                                                   final String direction, final String attribute) {
        final Sort.Direction directionEnum =
                Arrays.stream(Sort.Direction.values()).anyMatch(v -> v.name().equalsIgnoreCase(direction))
                        ? Sort.Direction.fromString(direction)
                        : Sort.Direction.DESC;
        final Sort sort = Sort.by(directionEnum, attribute);
        final Pageable pageable = PageRequest.of(page, size, sort);
        return userService.findAllCustomerAssociate(WhiteTypeAssociated(UserTypeEnum.CUSTOMER.name(), specification), pageable);
    }

    public static String  generateSHA256Hash(String password) {
        try {
            // Obtén una instancia de MessageDigest con el algoritmo SHA-256
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            // Convierte la contraseña en bytes
            byte[] passwordBytes = password.getBytes(StandardCharsets.UTF_8);

            // Calcula el hash SHA-256 de los bytes de la contraseña
            byte[] hashBytes = digest.digest(passwordBytes);

            // Convierte el hash en una representación hexadecimal
            StringBuilder hexHash = new StringBuilder();
            for (byte hashByte : hashBytes) {
                String hex = Integer.toHexString(0xff & hashByte);
                if (hex.length() == 1) {
                    hexHash.append('0');
                }
                hexHash.append(hex);
            }

            return hexHash.toString();
        } catch (NoSuchAlgorithmException e) {
            // Manejo de excepción en caso de algoritmo no encontrado
            e.printStackTrace();
            return null;
        }
    }


}
