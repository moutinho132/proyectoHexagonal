package com.martzatech.vdhg.crmprojectback.domains.chat.services;

import com.martzatech.vdhg.crmprojectback.domains.chat.mappers.ChatMessageReaderMapper;
import com.martzatech.vdhg.crmprojectback.domains.chat.models.ChatMessageReader;
import com.martzatech.vdhg.crmprojectback.domains.chat.repositories.ChatMessageReaderRepository;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Slf4j
@Service
public class ChatMessageReaderService {

  private ChatMessageReaderRepository repository;
  private ChatMessageReaderMapper mapper;

  public ChatMessageReader save(final ChatMessageReader model) {
    return mapper.entityToModel(repository.save(mapper.modelToEntity(model)));
  }

  public List<ChatMessageReader> findAll() {
    return mapper.entitiesToModelList(repository.findAll());
  }
}
