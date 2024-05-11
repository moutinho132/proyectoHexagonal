package com.martzatech.vdhg.crmprojectback.infrastructure.api.v2.controller;

import com.martzatech.vdhg.crmprojectback.application.enums.UserTypeEnum;
import com.martzatech.vdhg.crmprojectback.application.services.mobil.OrderManagementMobileService;
import com.martzatech.vdhg.crmprojectback.application.services.mobil.SecurityManagementAppService;
import com.martzatech.vdhg.crmprojectback.domains.chat.services.FileService;
import com.martzatech.vdhg.crmprojectback.domains.customers.models.Customer;
import com.martzatech.vdhg.crmprojectback.domains.customers.models.Order;
import com.martzatech.vdhg.crmprojectback.domains.customers.services.mobil.OrderMobileService;
import com.martzatech.vdhg.crmprojectback.domains.customers.specifications.OrderMobileSpecification;
import com.martzatech.vdhg.crmprojectback.domains.security.models.User;
import com.martzatech.vdhg.crmprojectback.infrastructure.api.v2.OrderMobileApi;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.mappers.NoteApiMapper;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.mappers.OrderApiMapper;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.requests.OrderRequest;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.responses.OrderMobilResponse;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.responses.OrderMobileResponse;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.responses.OrderResponse;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.responses.PaginatedResponse;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v2/order-mobile")
@Validated
public class OrderMobileController implements OrderMobileApi {

  private OrderMobileService service;
  private OrderManagementMobileService managementService;
  private OrderApiMapper mapper;
  private NoteApiMapper noteApiMapper;
  private FileService fileService;
  private SecurityManagementAppService securityManagementService;


  @Override
  public OrderMobileResponse save(final OrderRequest request, final String token) throws IOException {
    return mapper.modelToMobileResponse(managementService.save(mapper.requestToModel(request),token));
  }

  @Override
  public PaginatedResponse<OrderMobilResponse> findAll(final OrderMobileSpecification specification, final Integer page,
                                                  final Integer size, final String direction, final String attribute,
                                                  final Integer number, final BigDecimal priceFrom,final BigDecimal priceTo,
                                                  final String status, final String token) {
    User user;
    Customer customer = null;
    OrderMobileSpecification orderMobileSpecification = null;
    if (securityManagementService.validateToken(token)) {
      user = securityManagementService.findCurrentUser(token);

      if (UserTypeEnum.CUSTOMER.name().equals(user.getTypeUser())) {
        customer = user.getCustomer();
      }
      orderMobileSpecification = new OrderMobileSpecification(customer,number,priceFrom,priceTo,status);

    }

    final Long total = service.count(orderMobileSpecification);
    final List<Order> response = managementService.findAll(orderMobileSpecification, page, size, direction, attribute);
    return PaginatedResponse.<OrderMobilResponse>builder()
            .total(total.intValue())
            .page(page)
            .size(response.size())
            .items(mapper.modelsToResponseMobileList(response))
            .build();
  }

  @Override
  public OrderResponse findById(final Integer id,
                                final String token) {
    User user = null;
    if (securityManagementService.validateToken(token)) {
      user = securityManagementService.findCurrentUser(token);
    }
    return mapper.modelToResponse(service.findById(id));
  }

  @Override
  public boolean sendMobileChat(final Integer id,final String token) {
    return managementService.sendMobileChatService(id,token);

  }
}
