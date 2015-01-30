/*******************************************************************************
 * Copyright (c) 2012-2015 Codenvy, S.A.
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
import com.wordnik.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * @author Sergii Leschenko
 */
@DTO
public interface SubscriptionResources extends AccountResources {
    @ApiModelProperty(value = "Reference of subscription that provides resources")
    SubscriptionReference getSubscriptionReference();

    void setSubscriptionReference(SubscriptionReference subscriptionReference);

    SubscriptionResources withSubscriptionReference(SubscriptionReference subscriptionReference);

    @Override
    SubscriptionResources withUsed(List<WorkspaceResources> used);
}
