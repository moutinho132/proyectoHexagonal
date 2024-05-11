package com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.controllers;

import com.martzatech.vdhg.crmprojectback.application.services.OrderManagementService;
import com.martzatech.vdhg.crmprojectback.domains.chat.services.FileService;
import com.martzatech.vdhg.crmprojectback.domains.customers.enums.OrderStatusEnum;
import com.martzatech.vdhg.crmprojectback.domains.customers.models.Order;
import com.martzatech.vdhg.crmprojectback.domains.customers.services.OrderService;
import com.martzatech.vdhg.crmprojectback.domains.customers.specifications.OrderSpecification;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.OrderApi;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.mappers.NoteApiMapper;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.mappers.OrderApiMapper;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.requests.NoteRequest;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.requests.OrderRequest;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.responses.NoteResponse;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.responses.OrderResponse;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.responses.PaginatedResponse;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.responses.StatusResponse;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@AllArgsConstructor
@RestController
@RequestMapping("/api/orders")
@Validated
public class OrderController implements OrderApi {

  private OrderService service;
  private OrderManagementService managementService;
  private OrderApiMapper mapper;
  private NoteApiMapper noteApiMapper;
  private FileService fileService;

  @Override
  public OrderResponse save(final OrderRequest request) throws IOException {
    return mapper.modelToResponse(managementService.save(mapper.requestToModel(request)));
  }

  @Override
  public boolean sendEmailAndChat( Integer id) {
    return managementService.sendEmailAndChat(id);
  }

  @Override
  public void changeStatus(final Integer id, final Integer statusId) {
    managementService.changeStatus(id, statusId);
  }

  @Override
  public List<StatusResponse> getStatus() {
    return Arrays.stream(OrderStatusEnum.values())
        .map(e -> StatusResponse.builder().id(e.getId()).name(e.name()).build())
        .collect(Collectors.toList());
  }

  @Override
  public PaginatedResponse<OrderResponse> findAll(final OrderSpecification specification, final Integer page,
      final Integer size, final String direction, final String attribute) {
    final Long total = service.count(specification);
    final List<Order> response = managementService.findAll( specification, page, size, direction, attribute);
    return PaginatedResponse.<OrderResponse>builder()
        .total(total.intValue())
        .page(page)
        .size(response.size())
        .items(mapper.modelsToResponseList(response))
        .build();
  }

  @Override
  public OrderResponse findById(final Integer id) {
    return mapper.modelToResponse(service.findById(id));
  }

  @Override
  public void deleteById(final Integer id) {
    managementService.deleteById(id);
  }

  public void deleteByStatus(final Integer id) {
    managementService.deleteByStatus(id);

  }

  @Override
  public Map<String, String> getPdfById(final Integer number) throws IOException {
    return managementService.getPdfById(number);
  }
  @Override
  public Map<String, String> getPdfNewById(final Integer number) throws IOException {
    return managementService.getPdfInternal(number);
  }

  @Override
  public NoteResponse saveNote(final Integer id, final NoteRequest note) {
    return noteApiMapper.modelToResponse(managementService.saveNote(id, noteApiMapper.requestToModel(note)));
  }
}
