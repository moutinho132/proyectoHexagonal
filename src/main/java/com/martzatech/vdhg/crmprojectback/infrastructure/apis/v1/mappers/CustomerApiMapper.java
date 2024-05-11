package com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.mappers;

import com.martzatech.vdhg.crmprojectback.domains.customers.models.Customer;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.requests.CustomerRequest;
import com.martzatech.vdhg.crmprojectback.infrastructure.apis.v1.responses.CustomerResponse;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(
    componentModel = "spring",
    uses = {
        CustomerStatusApiMapper.class,
        MembershipApiMapper.class,
        CompanyApiMapper.class,
        PersonApiMapper.class,
        LeadApiMapper.class,
        CustomerStatusApiMapper.class,
        UserApiMapper.class,
        CreationTypeApiMapper.class,
        PaymentDetailsApiMapper.class
    }
)
public interface CustomerApiMapper {

  Customer requestToModel(CustomerRequest request);

  List<Customer> requestToModelList(List<CustomerRequest> list);

  CustomerResponse modelToResponse(Customer model);

  List<CustomerResponse> modelsToResponseList(List<Customer> list);
}
