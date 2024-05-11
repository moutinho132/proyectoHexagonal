package com.martzatech.vdhg.crmprojectback.application.services;

import com.martzatech.vdhg.crmprojectback.application.constants.CommonConstants;
import com.martzatech.vdhg.crmprojectback.application.enums.DeletedStatusEnum;
import com.martzatech.vdhg.crmprojectback.application.enums.LeadStatusEnum;
import com.martzatech.vdhg.crmprojectback.application.enums.RegistrationTypeEnum;
import com.martzatech.vdhg.crmprojectback.application.exceptions.BusinessRuleException;
import com.martzatech.vdhg.crmprojectback.application.exceptions.FieldIsMissingException;
import com.martzatech.vdhg.crmprojectback.domains.commons.models.*;
import com.martzatech.vdhg.crmprojectback.domains.customers.enums.NoteTypeEnum;
import com.martzatech.vdhg.crmprojectback.domains.customers.models.*;
import com.martzatech.vdhg.crmprojectback.domains.customers.services.*;
import com.martzatech.vdhg.crmprojectback.domains.customers.specifications.LeadSpecification;
import com.martzatech.vdhg.crmprojectback.domains.security.models.DeletedStatus;
import com.martzatech.vdhg.crmprojectback.domains.security.models.User;
import jakarta.persistence.Transient;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

import static com.martzatech.vdhg.crmprojectback.application.constants.CommonConstants.AZURE_PATH;
import static com.martzatech.vdhg.crmprojectback.application.helper.FileHelper.validateExtensionFile;

@AllArgsConstructor
@Slf4j
@Service
public class LeadManagementService {

    private final LeadService leadService;
    private final LeadStatusService statusService;
    private final CustomerService customerService;
    private final PersonManagementService personManagementService;
    private final CompanyService companyService;
    private final RegistrationTypeService registrationTypeService;
    private final SecurityManagementService securityManagementService;
    private final AzureFileService azureFileService;
    private final LeadCustomerFileService leadCustomerFileService;
    private final NoteService noteService;

    /*
     * The core services
     */

    @Transactional
    public Lead save(final Lead model) {
        validations(model);
        return leadService.save(build(model.withStatus(LeadStatus.builder().id(LeadStatusEnum.NEW.getId()).build())
                .withDeletedStatus(DeletedStatus.builder().id(1).build())));
    }

    @Transactional
    public LeadCustomerFile addFile(final Integer id, final MultipartFile file, final String extension,
                                    final String name) {
        String extensionFile = FilenameUtils.getExtension(file.getOriginalFilename());
        validateExtensionFile(extension, extensionFile);
        Lead model = leadService.findById(id);
        return leadCustomerFileService.save(
                LeadCustomerFile.builder()
                        .url(azureFileService.uploadFile(file, AZURE_PATH, extension))
                        .name(name)
                        .status(DeletedStatus.builder().id(DeletedStatusEnum.ACTIVE.getId()).build())
                        .lead(model)
                        .person(model.getPerson())
                        .creationTime(LocalDateTime.now())
                        .creationUser(securityManagementService.findCurrentUser())
                        .build()
        );
    }

    public List<Lead> findAll(final LeadSpecification leadSpecification,
                              final Integer page, final Integer size, final String direction, final String attribute) {
        final Direction directionEnum =
                Arrays.stream(Direction.values()).anyMatch(v -> v.name().equalsIgnoreCase(direction))
                        ? Direction.fromString(direction)
                        : Direction.DESC;
        final Sort sort = Sort.by(directionEnum, attribute);
        final Pageable pageable = PageRequest.of(page, size, sort);
        return leadService.findAll(leadSpecification, pageable);
    }

    public void deleteById(final Integer id) {
        final Customer model = customerService.findByLeadId(id);

        if (Objects.nonNull(model)) {
            throw new BusinessRuleException(CommonConstants.IS_A_CUSTOMER_MESSAGE);
        }

        leadService.deleteById(id);
    }

    public void deleteByStatus(final Integer id) {
        final Customer model = customerService.findByLeadId(id);

        if (Objects.nonNull(model)) {
            throw new BusinessRuleException(CommonConstants.IS_A_CUSTOMER_MESSAGE);
        }
        leadService.deleteStatus(id);
        deleteNoteAssociateLead(id);
    }

    private void deleteNoteAssociateLead(Integer id) {
        List<Note> notes = noteService.finByElementId(id);
        if (Objects.nonNull(notes)) {
            notes.forEach(note -> {
                noteService.deleteStatus(note.getId());
            });
        }
    }

    /*
     * The identity document services is directly related to the leads module
     */

    public IdentityDocument saveIdentityDocument(final Integer leadId, final IdentityDocument model) {
        final Lead lead = leadService.findById(leadId);
        return personManagementService.saveIdentityDocument(lead.getPerson().getId(), model);
    }

    public List<IdentityDocument> getIdentityDocuments(final Integer leadId) {
        final Person person = leadService.findById(leadId).getPerson();
        if (Objects.nonNull(person) && !CollectionUtils.isEmpty(person.getIdentityDocuments())) {
            return person.getIdentityDocuments();
        }
        return Collections.emptyList();
    }

    public void deleteIdentityDocumentById(final Integer leadId, final Integer id) {
        final Lead lead = leadService.findById(leadId);
        personManagementService.deleteIdentityDocumentById(lead.getPerson().getId(), id);
    }

    /*
     * The phone services is directly related to the leads module
     */

    public Phone savePhone(final Integer leadId, final Phone model) {
        final Lead lead = leadService.findById(leadId);
        return personManagementService.savePhone(lead.getPerson().getId(), model);
    }

    public List<Phone> getPhones(final Integer leadId) {
        final Person person = leadService.findById(leadId).getPerson();
        if (Objects.nonNull(person) && !CollectionUtils.isEmpty(person.getPhones())) {
            return person.getPhones();
        }
        return Collections.emptyList();
    }

    public void validatePhone(final Integer leadId, final Integer id, final Boolean valid) {
        final Lead lead = leadService.findById(leadId);
        personManagementService.validatePhone(lead.getPerson().getId(), id, valid);
    }

    public void deletePhoneById(final Integer leadId, final Integer id) {
        final Lead lead = leadService.findById(leadId);
        personManagementService.deletePhoneById(lead.getPerson().getId(), id);
    }

    /*
     * The email services is directly related to the leads module
     */

    public Email saveEmail(final Integer leadId, final Email model) {
        final Lead lead = leadService.findById(leadId);
        final Lead byEmail = leadService.findByEmail(model.getValue());
        if (!Objects.isNull(byEmail)) {
            if (lead.getId().intValue() != byEmail.getId()) {
                throw new BusinessRuleException("Already exists a Lead with this email: " + model.getValue());
            } else if (Objects.nonNull(model.getId())) {
                lead.getPerson().getEmails().stream()
                        .filter(e -> e.getValue().equalsIgnoreCase(model.getValue()))
                        .findAny()
                        .ifPresent(email -> {
                            if (model.getId().intValue() != email.getId()) {
                                throw new BusinessRuleException("The lead already has this email: " + model.getValue());
                            }
                        });
            }
        }
        return personManagementService.saveEmail(lead.getPerson().getId(), model);
    }

    public List<Email> getEmails(final Integer leadId) {
        final Person person = leadService.findById(leadId).getPerson();
        if (Objects.nonNull(person) && !CollectionUtils.isEmpty(person.getEmails())) {
            return person.getEmails();
        }
        return Collections.emptyList();
    }

    public void validateEmail(final Integer leadId, final Integer id, final Boolean valid) {
        final Lead lead = leadService.findById(leadId);
        personManagementService.validateEmail(lead.getPerson().getId(), id, valid);
    }

    public void deleteEmailById(final Integer leadId, final Integer id) {
        final Lead lead = leadService.findById(leadId);
        personManagementService.deleteEmailById(lead.getPerson().getId(), id);
    }

    /*
     * The address services is directly related to the leads module
     */

    public Address saveAddress(final Integer leadId, final Address model) {
        final Lead lead = leadService.findById(leadId);
        return personManagementService.saveAddress(lead.getPerson().getId(), model);
    }

    public List<Address> getAddress(final Integer leadId) {
        final Person person = leadService.findById(leadId).getPerson();
        if (Objects.nonNull(person) && !CollectionUtils.isEmpty(person.getAddresses())) {
            return person.getAddresses();
        }
        return Collections.emptyList();
    }

    public void deleteAddressById(final Integer leadId, final Integer id) {
        final Lead lead = leadService.findById(leadId);
        personManagementService.deleteAddressById(lead.getPerson().getId(), id);
    }

    /*
     * Validators
     */

    private void validations(final Lead model) {
        validPerson(model);
        validEmails(model);
        validRegistrationType(model.getRegistrationType());
    }

    private void validEmails(final Lead model) {
        if (CollectionUtils.isEmpty(model.getPerson().getEmails())) {
            throw new FieldIsMissingException(CommonConstants.EMAILS);
        }
        model.getPerson().getEmails().forEach(email -> {
            if (StringUtils.isNotEmpty(email.getValue())) {
                final Lead byEmail = leadService.findByEmail(email.getValue());
                if (!Objects.isNull(byEmail)) {
                    if (Objects.isNull(model.getId()) || model.getId().intValue() != byEmail.getId()) {
                        throw new BusinessRuleException("Already exists a Lead with this email: " + email.getValue());
                    } else if (Objects.nonNull(email.getId())) {
                        byEmail.getPerson().getEmails().stream()
                                .filter(e -> e.getValue().equalsIgnoreCase(email.getValue()))
                                .findAny()
                                .ifPresent(item -> {
                                    if (email.getId().intValue() != item.getId()) {
                                        throw new BusinessRuleException("The lead already has this email: " + email.getValue());
                                    }
                                });
                    }
                }
            }
        });
    }

    private void validPerson(final Lead model) {
        if (Objects.isNull(model.getPerson())) {
            throw new FieldIsMissingException(CommonConstants.PERSON);
        }
        if (Objects.nonNull(model.getId()) && Objects.isNull(model.getPerson().getId())) {
            throw new BusinessRuleException(CommonConstants.WHEN_UPDATE_THE_PERSON_ID_IS_MANDATORY_MESSAGE);
        }
        if (Objects.nonNull(model.getId())) {
            final Lead lead = leadService.findById(model.getId());
            if (model.getPerson().getId().intValue() != lead.getPerson().getId()) {
                throw new BusinessRuleException(CommonConstants.PERSON_ID_DOES_NOT_CORRESPOND_MESSAGE);
            }
        }

        if (Objects.isNull(model.getId()) && Objects.nonNull(model.getPerson().getId())) {
            throw new BusinessRuleException(CommonConstants.WHEN_CREATE_THE_LEAD_WITH_EXISTING_PERSON);
        }
    }

    private void validRegistrationType(final RegistrationType model) {
        if (Objects.nonNull(model) && Objects.nonNull(model.getId())) {
            registrationTypeService.existsById(model.getId());
        }
    }

    /*
     * Builders
     */
    private Lead build(final Lead model) {
        final AtomicReference<Lead> built = new AtomicReference<>(findById(model));
        built.set(buildRegistrationType(built.get()));
        built.set(buildDeleteStatus(model));
        built.set(buildCreationTime(built.get()));
        built.set(buildModificationTime(built.get()));
        built.set(buildPerson(built.get()));
        built.set(buildCompany(built.get()));
        return built.get();
    }

    private Lead findById(final Lead model) {
        if (Objects.nonNull(model.getId())) {
            return leadService
                    .findById(model.getId())
                    .withCompany(model.getCompany())
                    .withPerson(model.getPerson())
                    .withDeletedStatus(model.getDeletedStatus())
                    .withReferringCustomer(model.getReferringCustomer())
                    .withReference(model.getReference());
        }
        return model;
    }

    private Lead buildPerson(final Lead model) {
        if (!Objects.isNull(model.getPerson())) {
            return model.withPerson(personManagementService.save(model.getPerson()));
        }
        return model;
    }

    private Lead buildCreationTime(final Lead model) {
        if (Objects.isNull(model.getCreationTime())) {
            return model.withCreationTime(LocalDateTime.now());
        }
        return model;
    }

    private Lead buildModificationTime(final Lead model) {
        if (!Objects.isNull(model.getId())) {
            return model.withModificationTime(LocalDateTime.now());
        }
        return model;
    }

    private Lead buildDeleteStatus(final Lead model) {
        if (Objects.isNull(model.getDeletedStatus())) {
            return model.withDeletedStatus(DeletedStatus.builder().id(DeletedStatusEnum.ACTIVE.getId())
                    .name("Active").build());
        }
        return model;
    }


    private Lead buildCompany(final Lead model) {
        if (Objects.nonNull(model.getCompany())) {
            if (Objects.isNull(model.getCompany().getId())) {
                if (StringUtils.isNoneEmpty(model.getCompany().getName())) {
                    final Optional<Company> byName = companyService.findByName(model.getCompany().getName());
                    if (byName.isPresent()) {
                        return model.withCompany(byName.get());
                    }
                    return model.withCompany(companyService.save(model.getCompany()));
                } else {
                    throw new FieldIsMissingException(CommonConstants.LEAD_COMPANY_FIELD);
                }
            }else if (Objects.nonNull(model.getCompany().getId())){
                return model.withCompany(companyService.findById(model.getCompany().getId()));
            }
        }
        return model;
    }

    private Lead buildRegistrationType(final Lead model) {
        if (Objects.isNull(model.getRegistrationType())) {
            return model.withRegistrationType(RegistrationType.builder().id(RegistrationTypeEnum.AUTOMATIC.getId()).build());
        }
        return model;
    }

    public void updateStatus(final Integer leadId, final Integer statusId) {

        final Lead lead = leadService.findById(leadId);
        final LeadStatus status = statusService.findById(statusId);

        if (lead.getStatus().getId() == status.getId().intValue()) {
            throw new BusinessRuleException("The lead already has the same status");
        } else if (lead.getStatus().getId() == LeadStatusEnum.CONVERTED.getId().intValue()
                && status.getId() != LeadStatusEnum.DISCARDED.getId().intValue()) {
            throw new BusinessRuleException("You can only go from Converted to Discarded");
        }

        leadService.save(lead.withStatus(status).withModificationTime(LocalDateTime.now()));
    }

    public Note saveNote(final Integer id, final Note note) {
        leadService.existsById(id);

        final User currentUser = securityManagementService.findCurrentUser();
        final Note toSave = Objects.nonNull(note.getId())
                ? getPersistedNote(id, note, currentUser)
                : note.withType(NoteTypeEnum.LEAD).withElementId(id);

        return noteService.save(
                toSave
                        .withCreationTime(
                                Objects.nonNull(note.getId()) ? toSave.getCreationTime() : LocalDateTime.now()
                        )
                        .withCreationUser(
                                Objects.nonNull(note.getId()) ? toSave.getCreationUser() : currentUser
                        )
                        .withStatus(Objects.nonNull(note.getId()) ? toSave.getStatus() : DeletedStatus.builder().id(1).build())
                        .withModificationTime(
                                Objects.nonNull(note.getId()) ? LocalDateTime.now() : null
                        )
                        .withModificationUser(
                                Objects.nonNull(note.getId()) ? currentUser : null
                        )
        );
    }

    private Note getPersistedNote(final Integer id, final Note note, final User currentUser) {
        final Note byId = noteService.findById(note.getId());

        if (!(noteService.canUpdateAll(currentUser)
                || (byId.getCreationUser().getId().intValue() == currentUser.getId()
                && noteService.canUpdateOwn(currentUser)))) {
            throw new BusinessRuleException(
                    String.format("Current user with id %s, cannot update this note.", currentUser.getId())
            );
        }

        if (byId.getType() != NoteTypeEnum.LEAD) {
            throw new BusinessRuleException(
                    String.format("Current note type is %s. Note type cannot be changed.", byId.getType())
            );
        }

        if (byId.getElementId().intValue() != id) {
            throw new BusinessRuleException(
                    String.format("Current elementId is %s. ElementId cannot be changed.", byId.getElementId())
            );
        }

        return byId
                .withTitle(note.getTitle())
                .withDescription(note.getDescription())
                .withUsers(note.getUsers())
                .withRoles(note.getRoles());
    }
}
