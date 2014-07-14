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

import java.util.List;

/**
 * @author andrew00x
 */
@DTO
public interface NewMember {
    List<String> getRoles();

    void setRoles(List<String> roles);

    NewMember withRoles(List<String> roles);

    String getUserId();

    void setUserId(String id);

    NewMember withUserId(String id);

    String getAccountId();

    void setAccountId(String id);

    NewMember withAccountId(String id);
}
