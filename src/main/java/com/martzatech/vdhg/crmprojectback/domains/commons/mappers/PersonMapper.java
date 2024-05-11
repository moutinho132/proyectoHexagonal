package com.martzatech.vdhg.crmprojectback.domains.commons.mappers;

import com.martzatech.vdhg.crmprojectback.domains.commons.entities.PersonEntity;
import com.martzatech.vdhg.crmprojectback.domains.commons.models.Person;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

@Mapper(
    componentModel = "spring",
    uses = {
        CountryMapper.class,
        LanguageMapper.class,
        GenderMapper.class,
        CivilStatusMapper.class,
        PersonTitleMapper.class,
        IdentityDocumentMapper.class,
        PhoneMapper.class,
        EmailMapper.class,
        AddressMapper.class
    }
)
public abstract class PersonMapper {

  @Autowired
  private IdentityDocumentMapper identityDocumentMapper;

  @Autowired
  private PhoneMapper phoneMapper;

  @Autowired
  private EmailMapper emailMapper;

  @Autowired
  private AddressMapper addressMapper;

  public abstract Person entityToModel(PersonEntity entity);

  public abstract List<Person> entitiesToModelList(List<PersonEntity> list);

  @Mapping(target = "identityDocuments", ignore = true)
  @Mapping(target = "phones", ignore = true)
  @Mapping(target = "emails", ignore = true)
  @Mapping(target = "addresses", ignore = true)
  public abstract PersonEntity modelToEntity(Person model);

  public abstract List<PersonEntity> modelsToEntityList(List<Person> list);

  @AfterMapping
  public void afterModelToEntity(@MappingTarget final PersonEntity.PersonEntityBuilder entityBuilder,
      final Person person) {
    entityBuilder.identityDocuments(
        Objects.nonNull(person.getId()) && !CollectionUtils.isEmpty(person.getIdentityDocuments())
            ? identityDocumentMapper.modelsToEntityList(person.getIdentityDocuments())
            .stream()
            .map(entity -> entity.withPerson(PersonEntity.builder().id(person.getId()).build()))
            .collect(Collectors.toList())
            : new ArrayList<>()
    );
    entityBuilder.phones(
        Objects.nonNull(person.getId()) && !CollectionUtils.isEmpty(person.getPhones())
            ? phoneMapper.modelsToEntityList(person.getPhones())
            .stream()
            .map(entity -> entity.withPerson(PersonEntity.builder().id(person.getId()).build()))
            .collect(Collectors.toList())
            : new ArrayList<>()
    );
    entityBuilder.emails(
        Objects.nonNull(person.getId()) && !CollectionUtils.isEmpty(person.getEmails())
            ? emailMapper.modelsToEntityList(person.getEmails()).stream()
            .map(entity -> entity.withPerson(PersonEntity.builder().id(person.getId()).build()))
            .collect(Collectors.toList())
            : new ArrayList<>()
    );
    entityBuilder.addresses(
        Objects.nonNull(person.getId()) && !CollectionUtils.isEmpty(person.getAddresses())
            ? addressMapper.modelsToEntityList(person.getAddresses()).stream()
            .map(entity -> entity.withPerson(PersonEntity.builder().id(person.getId()).build()))
            .collect(Collectors.toList())
            : new ArrayList<>()
    );
  }
}
