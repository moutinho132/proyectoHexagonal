package com.martzatech.vdhg.crmprojectback.domains.customers.repositories;

import com.martzatech.vdhg.crmprojectback.domains.customers.entities.CategoryEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<CategoryEntity, Integer> {

    Optional<CategoryEntity> findByNameIgnoreCase(@Param("name") String name);

    @Override
    @Query(value = "select c from CategoryEntity c where c.id = :id and c.deleteStatus.id=1")
    Optional<CategoryEntity> findById(Integer id);

    @Override
    @Query(value = "select c from CategoryEntity c where  c.deleteStatus.id=1")
    List<CategoryEntity> findAll(Sort sort);

    @Modifying
    @Transactional
    @Query(value = "update CategoryEntity c set c.deleteStatus.id=2 where c.id = :id")
    void deleteById(Integer id);
}
