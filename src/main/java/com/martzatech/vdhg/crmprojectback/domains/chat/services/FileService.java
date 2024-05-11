package com.martzatech.vdhg.crmprojectback.domains.chat.services;

import com.martzatech.vdhg.crmprojectback.application.exceptions.not_found_exceptions.FileNotFoundException;
import com.martzatech.vdhg.crmprojectback.domains.chat.entities.FileEntity;
import com.martzatech.vdhg.crmprojectback.domains.chat.mappers.FileMapper;
import com.martzatech.vdhg.crmprojectback.domains.chat.models.File;
import com.martzatech.vdhg.crmprojectback.domains.chat.repositories.ChatMessageFileRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Slf4j
@Service
public class FileService {

    private ChatMessageFileRepository repository;
    private FileMapper mapper;

    public File save(final File model) {
        return mapper.entityToModel(repository.save(mapper.modelToEntity(model)));
    }

    public File findByUrl(final String url){
        Optional<FileEntity> file = repository.findByUrl(url);
        if(file.isPresent()){
            return mapper.entityToModel(file.get());
        }
        return null;
    }

    public File saveOffer(final File model) {
        return mapper.entityToModel(repository.save(mapper.modelToEntity(model)));
    }

    public File findById(final Integer id) {
        return repository.findById(id).map(mapper::entityToModel).orElseThrow(() -> new FileNotFoundException(id));
    }

    @Transactional
    public List<File> getFilesCustomer(final Integer IdCustomer, Pageable pageable) {
        return mapper.entitiesToModelList(repository.findAllCustomerPersonFile(IdCustomer, pageable));
    }

    @Transactional
    public List<File> getFilesCustomerNotPageable(final Integer IdCustomer) {
        return mapper.entitiesToModelList(repository.findAllCustomerPersonFileNotPageable(IdCustomer));
    }



  /*  @Transactional
    public List<File> getFileOrder(final Integer idOrder) {
        return mapper.entitiesToModelList(repository.findAllOrderFile(idOrder));
    }*/

   /* @Transactional
    public List<File> getFileOffer(final Integer idOffer) {
        return mapper.entitiesToModelList(repository.findAllOfferFile(idOffer));
    }*/

    @Transactional
    public List<File> findAllChatMessageFile(final Integer chatMessageId) {
        return mapper.entitiesToModelList(repository.findAllChatMessageFile(chatMessageId));
    }

    @Transactional
    public List<File> getFileProduct(final Integer idProduct) {
        return mapper.entitiesToModelList(repository.findAllProductFile(idProduct));
    }
    @Transactional
    public List<File> getFileProductPreOffer(final Integer idPreOffer) {
        return mapper.entitiesToModelList(repository.findAllProductPreOfferFile(idPreOffer));
    }

    @Transactional
    public List<File> getFileProductOffer(final Integer idPreOffer) {
        return mapper.entitiesToModelList(repository.findAllProductOfferFile(idPreOffer));
    }

    @Transactional
    public List<File> getFileVendor(final Integer vendorId) {
        return mapper
                .entitiesToModelList(repository.findAllVendorFile(vendorId))
                .stream()
                .sorted(Comparator.comparing(File::getId)
                        .reversed())
                .toList();
    }
    public void deleteById(final Integer id) {
        existsById(id);
        repository.deleteById(id);
    }

    public void existsById(final Integer id) {
        if (!repository.existsById(id)) {
            throw new FileNotFoundException(id);
        }
    }
}
