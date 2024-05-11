package com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.mappers;

import com.martzatech.vdhg.crmprojectback.domains.commons.models.Email;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.requests.EmailRequest;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.responses.EmailResponse;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(
    componentModel = "spring",
    uses = {
        AttributeTypeApiMapper.class
    }
)
public interface EmailApiMapper {

  Email requestToModel(EmailRequest request);

  List<Email> requestToModelList(List<EmailRequest> list);

  EmailResponse modelToResponse(Email model);

  List<EmailResponse> modelsToResponseList(List<Email> list);
}
