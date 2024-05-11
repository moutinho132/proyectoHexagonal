package com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.mappers;

import com.martzatech.vdhg.crmprojectback.domains.chat.models.File;
import com.martzatech.vdhg.crmprojectback.domains.chat.models.FileWallet;
import com.martzatech.vdhg.crmprojectback.domains.security.mappers.UserMapper;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.responses.FileResponse;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.responses.FileWalletResponse;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(
        componentModel = "spring",
        uses = {
                UserMapper.class
        }

)
public interface FileApiMapper {

   // @Mapping(source = "creationUser", target = "creationUser", qualifiedByName = "customUser")
    FileResponse modelToResponse(File model);

  List<FileResponse> modelsToResponseList(List<File> list);

    FileWalletResponse modelToWalletResponse(FileWallet model);

    List<FileWalletResponse> modelsToResponseWalletList(List<FileWallet> list);


}
