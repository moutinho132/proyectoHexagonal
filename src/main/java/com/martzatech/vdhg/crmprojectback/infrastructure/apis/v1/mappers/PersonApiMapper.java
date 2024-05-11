package com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.mappers;

import com.martzatech.vdhg.crmprojectback.domains.commons.models.Person;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.requests.PersonRequest;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.responses.PersonResponse;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(
    componentModel = "spring",
    uses = {
        CountryApiMapper.class,
        LanguageApiMapper.class,
        GenderApiMapper.class,
        CivilStatusApiMapper.class,
        PersonTitleApiMapper.class,
        IdentityDocumentApiMapper.class,
        PhoneApiMapper.class,
        EmailApiMapper.class,
        AddressApiMapper.class,
    }
)
public interface PersonApiMapper {

  Person requestToModel(PersonRequest request);

  List<Person> requestToModelList(List<PersonRequest> list);

  PersonResponse modelToResponse(Person model);

  List<PersonResponse> modelsToResponseList(List<Person> list);
}
