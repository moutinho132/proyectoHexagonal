package com.martzatech.vdhg.crmprojectback.application.services;

import com.martzatech.vdhg.crmprojectback.domains.commons.models.Country;
import com.martzatech.vdhg.crmprojectback.domains.commons.models.Email;
import com.martzatech.vdhg.crmprojectback.domains.commons.models.Person;
import com.martzatech.vdhg.crmprojectback.domains.commons.models.PersonTitle;
import com.martzatech.vdhg.crmprojectback.domains.commons.models.Phone;
import com.martzatech.vdhg.crmprojectback.domains.customers.models.Customer;
import com.martzatech.vdhg.crmprojectback.domains.customers.models.Lead;
import com.martzatech.vdhg.crmprojectback.domains.security.models.User;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@AllArgsConstructor
@Service
public class PersonFieldsBlockerService {

  private SecurityManagementService securityService;

  public List<?> block(final List<?> items) {
    if (!CollectionUtils.isEmpty(items)) {
      final User user = securityService.findCurrentUser();
      return items.stream()
          .map(item -> block(user, item))
          .collect(Collectors.toList());
    }
    return items;
  }

  public Object block(final Object item) {
    final User user = securityService.findCurrentUser();
    return block(user, item);
  }

  public Object block(final User user, final Object item) {
    if (item instanceof Customer && Objects.nonNull(((Customer) item).getPerson())) {
      return ((Customer) item).withPerson(block(((Customer) item).getPerson(), user, item.getClass()));
    }
    if (item instanceof Lead && Objects.nonNull(((Lead) item).getPerson())) {
      return ((Lead) item).withPerson(block(((Lead) item).getPerson(), user, item.getClass()));
    }
    return item;
  }

  private Person block(final Person person, final User user, final Class<?> clazz) {
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
