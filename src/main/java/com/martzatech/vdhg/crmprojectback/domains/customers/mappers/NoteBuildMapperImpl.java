package com.martzatech.vdhg.crmprojectback.domains.customers.mappers;

import com.martzatech.vdhg.crmprojectback.domains.customers.entities.CustomerEntity;
import com.martzatech.vdhg.crmprojectback.domains.customers.entities.LeadEntity;
import com.martzatech.vdhg.crmprojectback.domains.customers.entities.OrderEntity;
import com.martzatech.vdhg.crmprojectback.domains.customers.entities.ProductEntity;
import com.martzatech.vdhg.crmprojectback.domains.customers.models.*;
import com.martzatech.vdhg.crmprojectback.domains.customers.repositories.CustomerRepository;
import com.martzatech.vdhg.crmprojectback.domains.customers.repositories.LeadRepository;
import com.martzatech.vdhg.crmprojectback.domains.customers.repositories.OrderRepository;
import com.martzatech.vdhg.crmprojectback.domains.customers.repositories.ProductRepository;
import com.martzatech.vdhg.crmprojectback.domains.vendors.entities.VendorEntity;
import com.martzatech.vdhg.crmprojectback.domains.vendors.mapper.VendorMapper;
import com.martzatech.vdhg.crmprojectback.domains.vendors.models.Vendor;
import com.martzatech.vdhg.crmprojectback.domains.vendors.repositories.VendorRepository;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.Optional;

@AllArgsConstructor
@Getter
@Builder
@Component
public class NoteBuildMapperImpl {
    private CustomerRepository customerRepository;
    private LeadRepository leadRepository;
    private ProductRepository productRepository;
    private OrderRepository orderRepository;
    private CustomerMapper customerMapper;
    private VendorRepository vendorRepository;
    private VendorMapper vendorMapper;
    private LeadMapper leadMapper;
    private ProductMapper productMapper;
    private OrderMapper orderMapper;

    public Note buildNoteElement(Note note) {
        switch (note.getType()) {
            case LEAD -> {
                Optional<LeadEntity> lead = leadRepository.findById(note.getElementId());
                if (lead.isPresent()) {
                   return  getNoteElementLead(leadMapper.entityToModel(lead.get()), note);
                }
            }
            case CUSTOMER -> {
                Optional<CustomerEntity> customer = customerRepository.findById(note.getElementId());
                if (customer.isPresent()) {
                    return getNoteElementCustomer(customerMapper.entityToModel(customer.get()), note);
                }
            }
            case PRODUCT -> {
                Optional<ProductEntity> product = productRepository.findById(note.getElementId());
                if (product.isPresent()) {
                    return getNoteElementProduct(productMapper.entityToModel(product.get()), note);

                }
            }
            case ORDER -> {
                Optional<OrderEntity> order = orderRepository.findById(note.getElementId());
                if (order.isPresent()) {
                  return  getNoteElementOrder(orderMapper.entityToModel(order.get()), note);

                }
            }
            case VENDOR -> {
                Optional<VendorEntity> vendor = vendorRepository.findById(note.getElementId());
                if (vendor.isPresent()) {
                    return  getNoteElementVendor(vendorMapper.entityToModel(vendor.get()), note);
                }
            }
        }
        return  null;
    }

    private static Note getNoteElementOrder(Order order, Note note) {
        return Note
                .builder()
                .id(note.getId())
                .title(note.getTitle())
                .users(note.getUsers())
                .type(note.getType())
                .roles(note.getRoles())
                .description(note.getDescription())
                .elementId(note.getElementId())
                .status(note.getStatus())
                .creationUser(note.getCreationUser())
                .modificationUser(note.getModificationUser())
                .creationTime(note.getCreationTime())
                .modificationTime(note.getModificationTime())
                .element(NoteElement
                        .builder()
                        .id(order.getId())
                        .name(order.getOffer().getName()).build())
                .build();
    }

    private static Note getNoteElementVendor(Vendor vendor, Note note) {
        return Note
                .builder()
                .id(note.getId())
                .title(note.getTitle())
                .users(note.getUsers())
                .type(note.getType())
                .roles(note.getRoles())
                .description(note.getDescription())
                .elementId(note.getElementId())
                .status(note.getStatus())
                .creationUser(note.getCreationUser())
                .modificationUser(note.getModificationUser())
                .creationTime(note.getCreationTime())
                .modificationTime(note.getModificationTime())
                .element(NoteElement
                        .builder()
                        .id(vendor.getId())
                        .name(vendor.getName()).build())
                .build();
    }

    private static Note getNoteElementProduct(Product product, Note note) {
        return Note
                .builder()
                .id(note.getId())
                .title(note.getTitle())
                .users(note.getUsers())
                .type(note.getType())
                .roles(note.getRoles())
                .description(note.getDescription())
                .elementId(note.getElementId())
                .status(note.getStatus())
                .creationUser(note.getCreationUser())
                .modificationUser(note.getModificationUser())
                .creationTime(note.getCreationTime())
                .modificationTime(note.getModificationTime())
                .element(NoteElement
                        .builder()
                        .id(product.getId())
                        .name(product.getName())
                        .build())
                .build();
    }

    private static Note getNoteElementLead(Lead lead, Note note) {
        return Note
                .builder()
                .id(note.getId())
                .title(note.getTitle())
                .users(note.getUsers())
                .type(note.getType())
                .roles(note.getRoles())
                .description(note.getDescription())
                .elementId(note.getElementId())
                .status(note.getStatus())
                .creationUser(note.getCreationUser())
                .modificationUser(note.getModificationUser())
                .creationTime(note.getCreationTime())
                .modificationTime(note.getModificationTime())
                .element(NoteElement
                        .builder()
                        .id(lead.getId())
                        .title(lead.getPerson().getTitle().getName())
                        .name(lead.getPerson().getName())
                        .surname(lead.getPerson().getSurname())
                        .build())
                .build();
    }

    private static Note getNoteElementCustomer(Customer customer, Note note) {
        return Note
                .builder()
                .id(note.getId())
                .title(note.getTitle())
                .users(note.getUsers())
                .type(note.getType())
                .roles(note.getRoles())
                .description(note.getDescription())
                .elementId(note.getElementId())
                .status(note.getStatus())
                .creationUser(note.getCreationUser())
                .modificationUser(note.getModificationUser())
                .creationTime(note.getCreationTime())
                .modificationTime(note.getModificationTime())
                .element(NoteElement
                        .builder()
                        .id(customer.getId())
                        .title(customer.getPerson().getTitle().getName())
                        .name(customer.getPerson().getName())
                        .surname(customer.getPerson().getSurname()).build()).build();
    }
}
