package com.martzatech.vdhg.crmprojectback.application.services;

import com.martzatech.vdhg.crmprojectback.domains.customers.models.Note;
import com.martzatech.vdhg.crmprojectback.domains.customers.services.NoteService;
import com.martzatech.vdhg.crmprojectback.domains.customers.specifications.NoteSpecification;
import java.util.Arrays;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Slf4j
@Service
public class NoteManagementService {

  private final NoteService service;

  public List<Note> findAll(final NoteSpecification specification,
      final Integer page, final Integer size, final String direction, final String attribute) {
    final Direction directionEnum =
        Arrays.stream(Direction.values()).anyMatch(v -> v.name().equalsIgnoreCase(direction))
            ? Direction.fromString(direction)
            : Direction.DESC;
    final Sort sort = Sort.by(directionEnum, attribute);
    final Pageable pageable = PageRequest.of(page, size, sort);
    return service.findAll(specification, pageable);
  }

  public Long count(final NoteSpecification specification) {
    return service.count(specification);
  }
}
