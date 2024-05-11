package com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.controllers;

import com.martzatech.vdhg.crmprojectback.application.enums.UserTypeEnum;
import com.martzatech.vdhg.crmprojectback.application.services.SecurityManagementService;
import com.martzatech.vdhg.crmprojectback.domains.customers.specifications.UserSpecification;
import com.martzatech.vdhg.crmprojectback.domains.security.models.CustomerAssociate;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.SecurityApi;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.mappers.*;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.requests.DepartmentRequest;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.requests.RoleRequest;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.requests.UserRequest;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.responses.*;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.martzatech.vdhg.crmprojectback.application.helper.SpecificationHelper.WhiteTypeAssociated;

@AllArgsConstructor
@RestController
@RequestMapping("/api/security")
@Validated
public class SecurityController implements SecurityApi {

    private SecurityManagementService managementService;
    private DepartmentApiMapper departmentApiMapper;
    private AccessGroupApiMapper accessGroupApiMapper;
    private RoleApiMapper roleApiMapper;
    private UserApiMapper userApiMapper;
    private CustomerAssociateApiMapper customerAssociateApiMapper;

    @Override
    public DepartmentResponse saveDepartment(final DepartmentRequest request) {
        return departmentApiMapper.modelToResponse(managementService.saveDepartment(departmentApiMapper.requestToModel(request)));
    }

    @Override
    public RoleResponse saveRole(final RoleRequest request) {
        return roleApiMapper.modelToResponse(managementService.saveRole(roleApiMapper.requestToModel(request)));
    }

    @Override
    public UserResponse saveUser(final UserRequest request) {
        return userApiMapper.modelToResponse(managementService.saveUser(userApiMapper.requestToModel(request)));
    }

    @Override
    public List<DepartmentResponse> findAllDepartments() {
        return departmentApiMapper.modelsToResponseList(managementService.findAllDepartments());
    }

    @Override
    public List<RoleResponse> findAllRoles() {
        return roleApiMapper.modelsToResponseList(managementService.findAllRoles());
    }

    @Override
    public List<UserResponse> findAllUsers() {
        return userApiMapper.modelsToResponseList(managementService.findAllUsers());
    }

    @Override
    public List<AccessGroupResponse> findAllAccessGroups() {
        return accessGroupApiMapper.modelsToResponseList(managementService.findAllAccessGroup());
    }

    @Override
    public DepartmentResponse findDepartmentById(final Integer id) {
        return departmentApiMapper.modelToResponse(managementService.findDepartmentById(id));
    }

    @Override
    public RoleResponse findRoleById(final Integer id) {
        return roleApiMapper.modelToResponse(managementService.findRoleById(id));
    }

    @Override
    public UserResponse findUserById(final Integer id) {
        return userApiMapper.modelToResponse(managementService.findUserById(id));
    }

    @Override
    public UserResponse findCurrentUser() {
        return userApiMapper.modelToResponse(managementService.updateLastLoginAndGetCurrentUser());
    }

    @Override
    public void deleteDepartmentById(final Integer id) {
        managementService.deleteDepartmentById(id);
    }

    @Override
    public void deleteDepartmentByStatusAndId(final Integer id) {
        managementService.deleteDepartmentByStatusAndId(id);
    }

    @Override
    public void deleteRoleById(final Integer id) {
        managementService.deleteRoleById(id);
    }

    @Override
    public void deleteRoleByStatusAndId(final Integer id) {
        managementService.deleteRoleByStatusAndId(id);
    }

    @Override
    public void deleteUserById(final Integer id) {
        managementService.deleteUserById(id);
    }

    @Override
    public PaginatedResponse<CustomerAssociateResponse> findAllCustomer(final UserSpecification specification, final Integer page, final Integer size, final String direction, final String attribute) {
        final Long total = managementService.count(WhiteTypeAssociated(UserTypeEnum.CUSTOMER.name(), specification));
        final List<CustomerAssociate> response = managementService.findAllCustomer(WhiteTypeAssociated(UserTypeEnum.CUSTOMER.name(), specification), page, size, direction, attribute);
        return PaginatedResponse.<CustomerAssociateResponse>builder().total(total.intValue()).page(page).size(response.size()).items(customerAssociateApiMapper.modelsToResponseList(response)).build();
    }

    @Override
    public void deleteUserByStatus(final Integer id) {
        managementService.deleteUserByStatusAndId(id);
    }
}
