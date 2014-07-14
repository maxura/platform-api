/*******************************************************************************
 * Copyright (c) 2012-2014 Codenvy, S.A.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Codenvy, S.A. - initial API and implementation
 *******************************************************************************/
package com.codenvy.api.account.shared.dto;

import com.codenvy.dto.shared.DTO;

import java.util.Map;

/**
 * Describes subscription - a link between {@link com.codenvy.api.account.server.SubscriptionService} and {@link
 * Account}
 *
 * @author Eugene Voevodin
 */
@DTO
// TODO: replace with class
public interface Subscription {

    public enum State {
        WAIT_FOR_PAYMENT, ACTIVE
    }

    String getId();

    void setId(String id);

    Subscription withId(String id);

    String getAccountId();

    void setAccountId(String orgId);

    Subscription withAccountId(String orgId);

    String getServiceId();

    void setServiceId(String id);

    Subscription withServiceId(String id);

    long getStartDate();

    void setStartDate(long date);

    Subscription withStartDate(long date);

    long getEndDate();

    void setEndDate(long date);

    Subscription withEndDate(long date);

    Map<String, String> getProperties();

    void setProperties(Map<String, String> properties);

    Subscription withProperties(Map<String, String> properties);

    State getState();

    void setState(State state);

    Subscription withState(State state);
}