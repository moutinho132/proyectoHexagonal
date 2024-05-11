package com.martzatech.vdhg.crmprojectback.domains.chat.services;

import com.martzatech.vdhg.crmprojectback.application.exceptions.BusinessRuleException;
import com.martzatech.vdhg.crmprojectback.application.exceptions.not_found_exceptions.OutOfficeNotFoundException;
import com.martzatech.vdhg.crmprojectback.application.services.SecurityManagementService;
import com.martzatech.vdhg.crmprojectback.domains.chat.mappers.ChatOutOfficeMapper;
import com.martzatech.vdhg.crmprojectback.domains.chat.models.ChatOutOffice;
import com.martzatech.vdhg.crmprojectback.domains.chat.repositories.ChatOutOfficeRepository;
import com.martzatech.vdhg.crmprojectback.domains.customers.specifications.ChatOutOfficeSpecification;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Objects;

import static com.martzatech.vdhg.crmprojectback.application.helper.SpecificationHelper.addSoftDeleteChatOutOffice;

@AllArgsConstructor
@Slf4j
@Service
public class ChatMessageOutOfficeService {
    private static final String CHAT_VIEW_ALL_PERMISSION = "CHATS_VIEW_ALL";

    private ChatOutOfficeRepository repository;
    private ChatOutOfficeMapper mapper;
    private final SecurityManagementService securityManagementService;

    public ChatOutOffice save(final ChatOutOffice model) {
        if (Objects.requireNonNullElse(model.getId(), 0) != 0) {
            existsById(model.getId());
        }
        isValidHourFormat(model.getStart(),model.getEnd());
        return mapper.entityToModel(repository.save(mapper.modelToEntity(model)));
    }

    public static boolean isValidHourFormat(String hourStart,String hourEnd) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
            LocalTime hourSt = LocalTime.parse(hourStart, formatter);
            LocalTime hourEn = LocalTime.parse(hourEnd, formatter);
            return true;
        } catch (DateTimeParseException e) {
            throw  new BusinessRuleException("Date format invalid");
        }
    }

    public Long count(final ChatOutOfficeSpecification specification) {
        return repository.count(specification);
    }

    private void existsById(final Integer id) {
        if (!repository.existsById(id)) {
            throw new OutOfficeNotFoundException(id);
        }
    }

    public ChatOutOffice findById(final Integer id) {
        return mapper.entityToModel(repository.findById(id).orElseThrow(() -> new OutOfficeNotFoundException(id)));
    }

    public void deleteById(final Integer id) {
        existsById(id);
        repository.deleteById(id);
    }

    public List<ChatOutOffice> findAllOutOffice(final ChatOutOfficeSpecification specification, final Pageable pageable) {
        return mapper.entitiesToModelList(repository
                .findAll(addSoftDeleteChatOutOffice(1, specification), pageable)
                .stream().toList());
    }

    public List<ChatOutOffice> findAll() {
        return mapper.entitiesToModelList(repository.findAll());
    }


}
