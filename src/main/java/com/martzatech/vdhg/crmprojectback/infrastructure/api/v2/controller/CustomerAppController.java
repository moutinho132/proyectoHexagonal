package com.martzatech.vdhg.crmprojectback.infrastructure.api.v2.controller;

import com.martzatech.vdhg.crmprojectback.application.services.mobil.CustomerManagementAppService;
import com.martzatech.vdhg.crmprojectback.application.services.mobil.SecurityManagementAppService;
import com.martzatech.vdhg.crmprojectback.domains.chat.models.FileWallet;
import com.martzatech.vdhg.crmprojectback.domains.chat.services.ChatRoomService;
import com.martzatech.vdhg.crmprojectback.domains.chat.services.FileService;
import com.martzatech.vdhg.crmprojectback.domains.customers.services.CustomerService;
import com.martzatech.vdhg.crmprojectback.domains.customers.specifications.WalletSpecification;
import com.martzatech.vdhg.crmprojectback.domains.wallet.services.WalletService;
import com.martzatech.vdhg.crmprojectback.infrastructure.api.v2.CustomerAppApi;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.mappers.*;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.responses.FileWalletResponse;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.responses.PaginatedResponse;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v2/customers")
@Validated
public class CustomerAppController implements CustomerAppApi {

  private CustomerService service;
  private SecurityManagementAppService secureManagementService;

  private CustomerManagementAppService managementService;

  private CustomerApiMapper mapper;
  private UserApiMapper userApiMapper;

  private IdentityDocumentApiMapper identityDocumentApiMapper;

  private PhoneApiMapper phoneApiMapper;

  private EmailApiMapper emailApiMapper;

  private AddressApiMapper addressApiMapper;

  private NoteApiMapper noteApiMapper;
  private LeadCustomerFileApiMapper leadCustomerFileApiMapper;
  private ChatRoomService roomService;
  private FileApiMapper fileApiMapper;
  private FileService fileService;
  private WalletService walletService;
  /*@Override
  public PaginatedResponse<CustomerResponse> findAll(final CustomerSpecification specification,
      final Integer page, final Integer size, final String direction, final String attribute,
                                                     final String token) {
    final Long total = service.count(specification);
    final List<Customer> response = managementService.findAll(specification,token, page, size, direction, attribute);
    return PaginatedResponse.<CustomerResponse>builder()
        .total(total.intValue())
        .page(page)
        .size(response.size())
        .items(mapper.modelsToResponseList(response))
        .build();
  }*/

  @Override
  public PaginatedResponse<FileWalletResponse> findByWalletCustomer(final Integer id, final WalletSpecification specification, final Integer page, final Integer size,
                                                                    final String direction, final String attribute,final String token) {
    final Long total = walletService.count(specification);
    final Sort.Direction directionEnum =
            Arrays.stream(Sort.Direction.values()).anyMatch(v -> v.name().equalsIgnoreCase(direction))
                    ? Sort.Direction.fromString(direction)
                    : Sort.Direction.DESC;
    final Sort sort = Sort.by(directionEnum, attribute);
    final Pageable pageable = PageRequest.of(page, size, sort);
    List<FileWallet> response = managementService.getWallet(specification,pageable,token);
    return PaginatedResponse.<FileWalletResponse>builder()
            .total(total.intValue())
            .page(page)
            .size(response.size())
            .items(fileApiMapper.modelsToResponseWalletList(response))
            .build();
  }
}
