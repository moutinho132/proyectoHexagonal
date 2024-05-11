package com.martzatech.vdhg.crmprojectback.domains.security.mappers;

import com.martzatech.vdhg.crmprojectback.domains.security.entities.UserMobilEntity;
import com.martzatech.vdhg.crmprojectback.domains.security.models.UserMobil;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.requests.UserMobilRequestDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(
        componentModel = "spring",
        uses = {
                CommonNamed.class,
                UserMapper.class,
        }
)
public interface UserMobilMapper {
    @Mapping(source = "user", target = "user", qualifiedByName = "customUserMobilMapping")
    UserMobil entityToModel(UserMobilEntity entity);

    UserMobil requestToModel(UserMobilRequestDto request);

    List<UserMobil> entitiesToModelList(List<UserMobilEntity> list);

    UserMobilEntity modelToEntity(UserMobil model);

    List<UserMobilEntity> modelsToEntityList(List<UserMobil> list);


}
