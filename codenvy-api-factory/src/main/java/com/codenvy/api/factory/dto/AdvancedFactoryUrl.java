package com.codenvy.api.factory.dto;

import com.codenvy.dto.shared.DTO;

import java.util.List;

/**
 * Advanced factory format for factory 1.1. Contains additional information about factory.
 */
@DTO
public interface AdvancedFactoryUrl extends SimpleFactoryUrl {
    String getStyle();

    void setStyle(String style);

    String getDescription();

    void setDescription(String description);

    String getContactmail();

    void setContactmail(String contactMail);

    String getAuthor();

    void setAuthor(String author);

    String getId();

    void setId(String id);

    String getUserid();

    void setUserid(String userid);

    List<Link> getLinks();

    void setLinks(List<Link> links);

    String getValiduntil();

    void setValiduntil(String validuntil);

    String getValidsince();

    void setValidsince(String validsince);

    String getCreated();

    void setCreated(String created);

    WelcomePage getWelcome();

    void setWelcome(WelcomePage welcome);
}
