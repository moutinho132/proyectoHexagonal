package com.martzatech.vdhg.crmprojectback.domains.chat.mappers;

import com.martzatech.vdhg.crmprojectback.domains.chat.entities.ChatOutOfficeEntity;
import com.martzatech.vdhg.crmprojectback.domains.chat.models.ChatOutOffice;
import com.martzatech.vdhg.crmprojectback.domains.commons.mappers.DayOfficeMapper;
import com.martzatech.vdhg.crmprojectback.domains.security.mappers.CommonNamed;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.mappers.UserApiMapper;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.requests.OutOfficeRequest;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.responses.ChatOutOfficeResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(
        componentModel = "spring",
        uses = {
                DayOfficeMapper.class,
                UserApiMapper.class,
                CommonNamed.class,
                UserApiMapper.class
        }
)
public interface ChatOutOfficeMapper {
    @Mapping(source = "creationUser", target = "creationUser", qualifiedByName = "customUserMapping")
    @Mapping(source = "modificationUser", target = "modificationUser", qualifiedByName = "customUserMapping")
    ChatOutOffice entityToModel(ChatOutOfficeEntity entity);
    ChatOutOffice requestToModel(OutOfficeRequest request);

    List<ChatOutOffice> entitiesToModelList(List<ChatOutOfficeEntity> list);

    ChatOutOfficeEntity modelToEntity(ChatOutOffice model);

    List<ChatOutOfficeEntity> modelsToEntityList(List<ChatOutOffice> list);

    ChatOutOfficeResponse modelToResponse(ChatOutOffice model);

    List<ChatOutOfficeResponse> modelsToResponseList(List<ChatOutOffice> list);

}
