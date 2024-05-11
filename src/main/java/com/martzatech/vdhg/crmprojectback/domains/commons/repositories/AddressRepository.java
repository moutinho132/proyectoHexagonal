package com.martzatech.vdhg.crmprojectback.domains.commons.repositories;

import com.martzatech.vdhg.crmprojectback.domains.commons.entities.AddressEntity;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepository extends JpaRepository<AddressEntity, Integer> {

  @Query(
      value = "select e.id from AddressEntity e where e.person.id=:personId "
  )
  List<Integer> findByIdsByPersonId(@Param("personId") Integer personId);

  @Query(
      value = "select e from AddressEntity e where e.person.id=:personId and e.id=:id"
  )
  Optional<AddressEntity> findByIdAndPersonId(@Param("personId") Integer personId, @Param("id") Integer id);

  @Modifying
  @Query(value = "delete from AddressEntity e where e.person.id=:personId and e.id=:id")
  @Transactional
  void deleteByIdAndPersonId(@Param("personId") Integer personId, @Param("id") Integer id);
}
