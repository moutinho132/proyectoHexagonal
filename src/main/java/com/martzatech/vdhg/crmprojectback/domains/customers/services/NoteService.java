package com.martzatech.vdhg.crmprojectback.domains.customers.services;

import com.martzatech.vdhg.crmprojectback.application.enums.DeletedStatusEnum;
import com.martzatech.vdhg.crmprojectback.application.exceptions.BusinessRuleException;
import com.martzatech.vdhg.crmprojectback.application.exceptions.not_found_exceptions.NoteNotFoundException;
import com.martzatech.vdhg.crmprojectback.application.services.SecurityManagementService;
import com.martzatech.vdhg.crmprojectback.domains.customers.entities.NoteEntity;
import com.martzatech.vdhg.crmprojectback.domains.customers.mappers.NoteBuildMapperImpl;
import com.martzatech.vdhg.crmprojectback.domains.customers.mappers.NoteMapper;
import com.martzatech.vdhg.crmprojectback.domains.customers.models.Note;
import com.martzatech.vdhg.crmprojectback.domains.customers.repositories.NoteRepository;
import com.martzatech.vdhg.crmprojectback.domains.customers.specifications.NoteSpecification;
import com.martzatech.vdhg.crmprojectback.domains.security.entities.RoleEntity;
import com.martzatech.vdhg.crmprojectback.domains.security.entities.UserEntity;
import com.martzatech.vdhg.crmprojectback.domains.security.models.DeletedStatus;
import com.martzatech.vdhg.crmprojectback.domains.security.models.Role;
import com.martzatech.vdhg.crmprojectback.domains.security.models.User;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.martzatech.vdhg.crmprojectback.application.helper.SpecificationHelper.addSoftDeleteNote;

@AllArgsConstructor
@Slf4j
@Service
public class NoteService {

    private static final String NOTE_VIEW_ALL_PERMISSION = "NOTES_VIEW_ALL";
    private static final String UPDATE_VIEW_ALL_PERMISSION = "NOTES_EDIT_ALL";
    private static final String NOTES_UPDATE_OWN_PERMISSION = "NOTES_EDIT_OWN";

    private final NoteRepository repository;
    private final NoteMapper mapper;
    private final SecurityManagementService securityManagementService;
    private final NoteBuildMapperImpl noteBuildMapper;

    public Note save(final Note model) {
        if (Objects.requireNonNullElse(model.getId(), 0) != 0) {
            existsById(model.getId());
        }
        model.withStatus(DeletedStatus.builder().id(DeletedStatusEnum.ACTIVE.getId()).build());

        final NoteEntity saved = repository.save(mapper.modelToEntity(model));
        return mapper.entityToModel(saved);
    }

    public Long count(final NoteSpecification specification) {
        return repository.count(checkUserPermission(specification));
    }

    public List<Note> findAll(final NoteSpecification specification, final Pageable pageable) {
       User currentUser = securityManagementService.findCurrentUser();
        List<Note> noteList =  new ArrayList<>();
        if (canViewAll(currentUser)) {
            mapper.entitiesToModelList(
                    repository.findAll(addSoftDeleteNote(DeletedStatusEnum.ACTIVE.getId(), specification)
                                    .and(checkUserPermission(specification)), pageable)
                            .stream()
                            .toList()

            ).forEach(note -> {
                Note noteSave = noteBuildMapper.buildNoteElement(note);
                if(Objects.nonNull(noteSave)){
                    noteList.add(noteSave);
                }
            });
            return noteList;
        }else{
            throw new BusinessRuleException("You cannot see notes if you are not an allowed user");
        }
    }
    private static final Specification<NoteEntity> WITH_STATUS_ACTIVE = (root, query, criteriaBuilder) ->
            criteriaBuilder.equal(
                    root.<Integer>get("status").get("id"), DeletedStatusEnum.ACTIVE.getId()
            );

    public Note findById(final Integer id) {
        final NoteSpecification byId = (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.<Integer>get("id"), id);
        User currentUser = securityManagementService.findCurrentUser();

        if (canViewAll(currentUser)) {
            final List<NoteEntity> findAll = repository.findAll(checkUserPermission(byId).and(WITH_STATUS_ACTIVE));
            if (CollectionUtils.isEmpty(findAll)) {
                throw new NoteNotFoundException(id);
            }
            return mapper.entityToModel(findAll.get(0));
        }else{
            throw new BusinessRuleException("You cannot see notes if you are not an allowed user");
        }
    }

    public List<Note> finByElementId(final Integer elementId){
        List<NoteEntity>  noteList = repository.findByElementId(elementId);
        if(noteList.isEmpty()){
            log.info("Not fount Note for element id : {}",elementId);
        }
        return mapper.entitiesToModelList(noteList);
    }

    public void existsById(final Integer id) {
        if (!repository.existsById(id)) {
            throw new NoteNotFoundException(id);
        }
    }

    private NoteSpecification checkUserPermission(final NoteSpecification specification) {
        final User currentUser = securityManagementService.findCurrentUser();
        if (!canViewAll(currentUser)) {
            final Specification<NoteEntity> conjunction = getConjunction(currentUser);
            return (root, query, criteriaBuilder) ->
                    Objects.nonNull(specification)
                            ? criteriaBuilder
                            .and(
                                    specification.toPredicate(root, query, criteriaBuilder),
                                    conjunction.toPredicate(root, query, criteriaBuilder)
                            )
                            : criteriaBuilder
                            .and(
                                    conjunction.toPredicate(root, query, criteriaBuilder)
                            );
        }
        return specification;
    }

    private static Specification<NoteEntity> getConjunction(final User currentUser) {
        return (root, query, criteriaBuilder) -> {
            final Join<NoteEntity, RoleEntity> roleJoin = root.join("roles", JoinType.LEFT);
            final Join<NoteEntity, UserEntity> userJoin = root.join("users", JoinType.LEFT);

            return criteriaBuilder
                    .or(
                            criteriaBuilder.equal(root.get("creationUser").get("id"), currentUser.getId()),
                            criteriaBuilder.equal(root.get("status").get("id"), DeletedStatusEnum.ACTIVE.getId()),
                            criteriaBuilder.in(roleJoin.get("id")).value(currentUser.getRoles().stream().map(Role::getId).toList()),
                            criteriaBuilder.equal(userJoin.get("id"), currentUser.getId())
                    );
        };
    }

    private boolean canViewAll(final User currentUser) {
        return currentUser.getRoles()
                .stream()
                .flatMap(role -> role.getPermissions().stream())
                .anyMatch(permission -> NOTE_VIEW_ALL_PERMISSION.equalsIgnoreCase(permission.getName()));
    }

    public boolean canUpdateAll(final User currentUser) {
        return currentUser.getRoles()
                .stream()
                .flatMap(role -> role.getPermissions().stream())
                .anyMatch(permission -> UPDATE_VIEW_ALL_PERMISSION.equalsIgnoreCase(permission.getName()));
    }

    public boolean canUpdateOwn(final User currentUser) {
        return currentUser.getRoles()
                .stream()
                .flatMap(role -> role.getPermissions().stream())
                .anyMatch(permission -> NOTES_UPDATE_OWN_PERMISSION.equalsIgnoreCase(permission.getName()));
    }

    public void deleteById(final Integer id) {
        existsById(id);

        repository.deleteById(id);
    }

    public void deleteStatus(final Integer id) {
        existsById(id);

        repository.deleteStatus(id);
    }
}
