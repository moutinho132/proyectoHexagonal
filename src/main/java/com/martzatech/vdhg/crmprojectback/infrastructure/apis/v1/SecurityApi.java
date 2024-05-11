package com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1;

import com.martzatech.vdhg.crmprojectback.domains.customers.specifications.UserSpecification;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.requests.*;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.responses.*;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public interface SecurityApi {

    @Operation(summary = "Save a Department")
    @PostMapping(path = "/departments", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    DepartmentResponse saveDepartment(@RequestBody @Valid DepartmentRequest request);

    @Operation(summary = "Save a Role")
    @PostMapping(path = "/roles", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    RoleResponse saveRole(@RequestBody @Valid RoleRequest request);

    @Operation(summary = "Save a User")
    @PostMapping(path = "/users", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    UserResponse saveUser(@RequestBody @Valid UserRequest request);

    @Operation(summary = "Get all Departments")
    @GetMapping(path = "/departments", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    List<DepartmentResponse> findAllDepartments();

    @Operation(summary = "Get all Roles")
    @GetMapping(path = "/roles", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    List<RoleResponse> findAllRoles();

    @Operation(summary = "Get all Users ")
    @GetMapping(path = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    List<UserResponse> findAllUsers();

    @Operation(summary = "Get all Access Groups")
    @GetMapping(path = "/accesses", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    List<AccessGroupResponse> findAllAccessGroups();

    @Operation(summary = "Get a Department by its id")
    @GetMapping(value = "/departments/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    DepartmentResponse findDepartmentById(@PathVariable("id") Integer id);

    @Operation(summary = "Get a Role by its id")
    @GetMapping(value = "/roles/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    RoleResponse findRoleById(@PathVariable("id") Integer id);

    @Operation(summary = "Get a Role by its id")
    @GetMapping(value = "/users/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    UserResponse findUserById(@PathVariable("id") Integer id);

    @Operation(summary = "Get the current user")
    @GetMapping(value = "/users/current", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    UserResponse findCurrentUser();

    @Operation(summary = "Delete a Department by its id soft delete")
    @DeleteMapping(value = "/departments/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.ACCEPTED)
    void deleteDepartmentByStatusAndId(@PathVariable("id") Integer id);

    @Operation(summary = "Delete a Department by its id hard delete")
    @DeleteMapping(value = "/departments/deleteDepartmentById/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.ACCEPTED)
    void deleteDepartmentById(@PathVariable("id") Integer id);

    @Operation(summary = "Delete a Role by its id soft delete")
    @DeleteMapping(value = "/roles/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.ACCEPTED)
    void deleteRoleByStatusAndId(@PathVariable("id") Integer id);

    @Operation(summary = "Delete a Role by its id hard delete")
    @DeleteMapping(value = "/roles/deleteRoleById/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.ACCEPTED)
    void deleteRoleById(@PathVariable("id") Integer id);

    @Operation(summary = "Delete a user by their id soft delete")
    @DeleteMapping(value = "/users/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.ACCEPTED)
    void deleteUserByStatus(@PathVariable("id") Integer id);

    @Operation(summary = "Delete a user by their id hard delete")
    @DeleteMapping(value = "/users/deleteUserById/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.ACCEPTED)
    void deleteUserById(@PathVariable("id") Integer id);

    @Operation(summary = "Get all Customers and Associated")
    @GetMapping(value = "/users/customers", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    PaginatedResponse<CustomerAssociateResponse> findAllCustomer(UserSpecification specification,
                                                                 @RequestParam(name = "page", defaultValue = "0") Integer page,
                                                                 @RequestParam(name = "size", defaultValue = "10") Integer size,
                                                                 @RequestParam(name = "sort-direction", defaultValue = "DESC") String direction,
                                                                 @RequestParam(name = "sort-attribute", defaultValue = "id") String attribute
    );
}
