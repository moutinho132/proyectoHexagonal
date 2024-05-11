package com.martzatech.vdhg.crmprojectback.domains.security.repositories;

import com.martzatech.vdhg.crmprojectback.domains.security.entities.UserEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Integer>, JpaSpecificationExecutor<UserEntity> {

    @Modifying
    @Transactional
    @Query(value = "DELETE from UserEntity c where c.id = :id")
    void deleteById(@Param("id") Integer id);

    boolean existsByEmailIgnoreCase(String email);

    @Override
    @Query(value = "select  u from UserEntity u where u.id = :id")
    Optional<UserEntity> findById(Integer id);

    @Query(value = "select  u from UserEntity u where u.customer.id = :id")
    Optional<UserEntity> findByIdCustomer(Integer id);

    @Query(value = "select  u from UserEntity u where u.associated.id = :id")
    Optional<UserEntity> findByIdAssociated(Integer id);
    @Query(value = "select  u from UserEntity u where u.email=:email and u.status.id =1")
    Optional<UserEntity> findByEmail(String email);

    @Query(value = "select  u from UserEntity u where u.email=:email and u.status.id =1 and u.typeUser in('CUSTOMER','ASSOCIATE') ")
    Optional<UserEntity> findByEmailMobile(String email);

    @Override
    @Query(value = "select  u from UserEntity u where u.status.id = 1")
    List<UserEntity> findAll(Sort sort);

    @Modifying
    @Transactional
    @Query(value = "update UserEntity u set u.status.id=2 where u.id = :id")
    void deleteUserByStatus(@Param("id") Integer id);

    @Modifying
    @Transactional
    @Query(value = "update UserEntity u set u.status.id=1 where u.id = :id")
    void activeUser(@Param("id") Integer id);


    @Query(
            value = "select u.id,u.name,u.creation_time ,u.status_id ,u.surname ,u.email ,u.mobile ," +
                    "u.address ,u.nationality ,u.modification_time ,u.title \n" +
                    ",u.creation_user_id ,u.modification_user_id ," +
                    "u.last_login ,u.customer_id ,u.associated_id ,u.type_user," +
                    "concat(u.name , ' ', u.surname,' ',u.email) as fullName  from t_users_roles ur ,t_users u " +
                    "where ur.role_id =:roleId and ur.user_id =u.id ",
            nativeQuery = true)
    List<UserEntity> findByRole(@Param("roleId") Integer roleId);
}
