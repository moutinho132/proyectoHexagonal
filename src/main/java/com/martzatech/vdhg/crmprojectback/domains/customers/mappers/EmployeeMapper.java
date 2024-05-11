package com.martzatech.vdhg.crmprojectback.domains.customers.mappers;

import com.martzatech.vdhg.crmprojectback.domains.commons.mappers.PersonMapper;
import com.martzatech.vdhg.crmprojectback.domains.customers.entities.EmployeeEntity;
import com.martzatech.vdhg.crmprojectback.domains.customers.models.Employee;
import com.martzatech.vdhg.crmprojectback.domains.security.mappers.SubsidiaryMapper;
import com.martzatech.vdhg.crmprojectback.domains.security.mappers.UserMapper;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(
    componentModel = "spring",
    uses = {
        EmployeeTypeMapper.class,
        PersonMapper.class,
        SubsidiaryMapper.class,
        UserMapper.class,
    }
)
public interface EmployeeMapper {

  Employee entityToModel(EmployeeEntity entity);

  List<Employee> entitiesToModelList(List<EmployeeEntity> list);

  EmployeeEntity modelToEntity(Employee model);

  List<EmployeeEntity> modelsToEntityList(List<Employee> list);
}
