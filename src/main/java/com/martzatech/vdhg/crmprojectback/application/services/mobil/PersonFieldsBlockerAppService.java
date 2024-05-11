package com.martzatech.vdhg.crmprojectback.application.services.mobil;

import com.martzatech.vdhg.crmprojectback.application.services.mobil.SecurityManagementAppService;
import com.martzatech.vdhg.crmprojectback.domains.commons.models.*;
import com.martzatech.vdhg.crmprojectback.domains.customers.models.Customer;
import com.martzatech.vdhg.crmprojectback.domains.customers.models.Lead;
import com.martzatech.vdhg.crmprojectback.domains.security.models.User;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class PersonFieldsBlockerAppService {

  private SecurityManagementAppService securityService;

  public List<?> block(final List<?> items ,String token) {
    if (!CollectionUtils.isEmpty(items)) {
      final User user = securityService.findCurrentUser(token);
      return items.stream()
          .map(item -> block(user, item,token))
          .collect(Collectors.toList());
    }
    return items;
  }

  public Object block(final Object item,String token) {
    final User user = securityService.findCurrentUser(token);
    return block(user, item,token);
  }

  public Object block(final User user, final Object item,String token) {
    if (item instanceof Customer && Objects.nonNull(((Customer) item).getPerson())) {
      return ((Customer) item)
              .withPerson(block(((Customer) item).getPerson(), user, item.getClass(),token));
    }
    if (item instanceof Lead && Objects.nonNull(((Lead) item).getPerson())) {
      return ((Lead) item).withPerson(block(((Lead) item).getPerson(), user, item.getClass(),token));
    }
    return item;
  }

  private Person block(final Person person, final User user, final Class<?> clazz,String token) {
    return person
        .withTitle(
            hasField(user, "title", clazz)
                ? person.getTitle()
                : PersonTitle.builder().build())
        .withNationality(
            hasField(user, "nationality", clazz)
                ? person.getNationality()
                : Country.builder().build())
        .withName(
            hasField(user, "name", clazz)
                ? person.getName()
                : StringUtils.SPACE)
        .withSurname(
            hasField(user, "surname", clazz)
                ? person.getSurname()
                : StringUtils.SPACE)
        .withEmails(
            hasField(user, "email", clazz)
                ? person.getEmails()
                : List.of(Email.builder().build()))
        .withPhones(
            hasField(user, "phone", clazz)
                ? person.getPhones()
                : List.of(Phone.builder().build()));
  }

  private boolean hasField(final User user, final String field, final Class<?> clazz) {
    if (!CollectionUtils.isEmpty(user.getRoles())) {
      return user.getRoles()
          .stream().flatMap(role -> role.getPermissions().stream())
          .filter(permission -> StringUtils.isNoneBlank(permission.getResource()))
          .filter(permission -> StringUtils.isNoneBlank(permission.getField()))
          .filter(
              permission ->
                  (
                      permission.getResource().toLowerCase(Locale.ROOT).startsWith("/customers")
                          && clazz.equals(Customer.class)
                  )
                      || (
                      permission.getResource().toLowerCase(Locale.ROOT).startsWith("/leads")
                          && clazz.equals(Lead.class)
                  )
          )
          .anyMatch(permission -> permission.getField().equalsIgnoreCase(field));
    }
    return false;
  }
}
