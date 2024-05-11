package com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.mappers;

import com.martzatech.vdhg.crmprojectback.domains.customers.models.Note;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.requests.NoteRequest;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.responses.NoteResponse;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(
    componentModel = "spring",
    uses = {
        NoteApiMapper.class
    }
)
public interface NoteApiMapper {

  Note requestToModel(NoteRequest request);

  List<Note> requestToModelList(List<NoteRequest> list);

  NoteResponse modelToResponse(Note model);

  List<NoteResponse> modelsToResponseList(List<Note> list);
}
