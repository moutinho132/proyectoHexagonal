package com.martzatech.vdhg.crmprojectback.domains.customers.repositories;

import com.martzatech.vdhg.crmprojectback.domains.customers.entities.SubCategoryEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SubCategoryRepository extends JpaRepository<SubCategoryEntity, Integer> {

  @Query("select e from SubCategoryEntity e where lower(e.name)=lower(:name) and e.category.id=:categoryId")
  Optional<SubCategoryEntity> findByNameAndCategoryId(
      @Param("name") String name, @Param("categoryId") Integer categoryId
  );

  @Query("select e.id from SubCategoryEntity e where e.category.id=:categoryId")
  List<Integer> findByIdsByCategoryId(Integer categoryId);

  @Modifying
  @Query("delete from SubCategoryEntity e where e.id=:id")
  @Override
  void deleteById(@Param("id") Integer id);
}
