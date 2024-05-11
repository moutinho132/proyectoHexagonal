package com.martzatech.vdhg.crmprojectback.domains.wallet.mapper;

import com.martzatech.vdhg.crmprojectback.domains.chat.mappers.FileMapper;
import com.martzatech.vdhg.crmprojectback.domains.commons.mappers.PersonMapper;
import com.martzatech.vdhg.crmprojectback.domains.wallet.entities.WalletEntity;
import com.martzatech.vdhg.crmprojectback.domains.wallet.models.Wallet;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(
        componentModel = "spring",
        uses = {
                FileMapper.class,
                PersonMapper.class
        }
)
public interface WalletMapper {
    WalletEntity modelToEntity(Wallet model);

    Wallet entityToModel(WalletEntity entity);

    List<Wallet> entityToModelList(List<WalletEntity> entity);
}
