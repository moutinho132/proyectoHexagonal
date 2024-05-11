package com.martzatech.vdhg.crmprojectback.domains.customers.mappers;

import com.martzatech.vdhg.crmprojectback.domains.customers.entities.NoteEntity;
import com.martzatech.vdhg.crmprojectback.domains.customers.models.Note;
import com.martzatech.vdhg.crmprojectback.domains.security.entities.RoleEntity;
import com.martzatech.vdhg.crmprojectback.domains.security.entities.UserEntity;
import com.martzatech.vdhg.crmprojectback.domains.security.models.Role;
import com.martzatech.vdhg.crmprojectback.domains.security.models.User;
import java.util.List;
import java.util.stream.Collectors;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.util.CollectionUtils;

@Mapper(
    componentModel = "spring",
    uses = {
        CustomNamedMappers.class
    }
)
public interface NoteMapper {

  @Mapping(source = "creationUser", target = "creationUser", qualifiedByName = "customUser")
  @Mapping(source = "modificationUser", target = "modificationUser", qualifiedByName = "customUser")
  @Mapping(source = "roles", target = "roles", qualifiedByName = "customRoles")
  @Mapping(source = "users", target = "users", qualifiedByName = "customUsers")
  Note entityToModel(NoteEntity entity);

  List<Note> entitiesToModelList(List<NoteEntity> list);

  NoteEntity modelToEntity(Note model);

  List<NoteEntity> modelsToEntityList(List<Note> list);

  @Named("customRoles")
  static List<Role> customRoles(final List<RoleEntity> list) {
    return !CollectionUtils.isEmpty(list)
        ? list.stream()
        .map(entity -> Role
            .builder()
            .id(entity.getId())
            .name(entity.getName())
            .build())
        .collect(Collectors.toList())
        : null;
  }

  @Named("customUsers")
  static List<User> customUsers(final List<UserEntity> list) {
    return !CollectionUtils.isEmpty(list)
        ? list.stream()
        .map(entity -> User
            .builder()
            .id(entity.getId())
            .title(entity.getTitle())
            .name(entity.getName())
            .surname(entity.getSurname())
            .build())
        .collect(Collectors.toList())
        : null;
  }
}
