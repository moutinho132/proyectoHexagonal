package com.martzatech.vdhg.crmprojectback.domains.commons.services;

import com.martzatech.vdhg.crmprojectback.application.exceptions.not_found_exceptions.LanguageNotFoundException;
import com.martzatech.vdhg.crmprojectback.domains.commons.mappers.LanguageMapper;
import com.martzatech.vdhg.crmprojectback.domains.commons.models.Language;
import com.martzatech.vdhg.crmprojectback.domains.commons.repositories.LanguageRepository;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Slf4j
@Service
public class LanguageService {

  private LanguageRepository repository;
  private LanguageMapper mapper;


  public List<Language> findAll() {
    return mapper.entitiesToModelList(repository.findAll(Sort.by(Direction.ASC, "id")));
  }

  public Language findById(final Integer id) {
    return mapper.entityToModel(repository.findById(id).orElseThrow(() -> new LanguageNotFoundException(id)));
  }

  public void existsById(final Integer id) {
    if (!repository.existsById(id)) {
      throw new LanguageNotFoundException(id);
    }
  }
}
