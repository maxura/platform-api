/*
 * CODENVY CONFIDENTIAL
 * __________________
 * 
 *  [2012] - [2014] Codenvy, S.A. 
 *  All Rights Reserved.
 * 
 * NOTICE:  All information contained herein is, and remains
 * the property of Codenvy S.A. and its suppliers,
 * if any.  The intellectual and technical concepts contained
 * herein are proprietary to Codenvy S.A.
 * and its suppliers and may be covered by U.S. and Foreign Patents,
 * patents in process, and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material
 * is strictly forbidden unless prior written permission is obtained
 * from Codenvy S.A..
 */
package com.codenvy.api.local;

import com.codenvy.api.account.server.dao.AccountDao;
import com.codenvy.api.account.server.exception.AccountException;
import com.codenvy.api.account.shared.dto.Account;
import com.codenvy.api.account.shared.dto.Attribute;
import com.codenvy.api.account.shared.dto.Member;
import com.codenvy.api.account.shared.dto.Subscription;
import com.codenvy.dto.server.DtoFactory;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * @author Eugene Voevodin
 */
public class LocalAccountDaoImpl implements AccountDao {
    @Override
    public void create(Account account) throws AccountException {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public Account getById(String id) throws AccountException {
        return DtoFactory.getInstance().createDto(Account.class)
                         .withName("account_name")
                         .withId(id)
                         .withOwner("userId122332133123")
                         .withAttributes(Arrays.asList(
                                 DtoFactory.getInstance().createDto(Attribute.class).withName("attribute1").withValue("value")
                                           .withDescription("important attribute")));
    }

    @Override
    public Account getByName(String name) throws AccountException {
        return DtoFactory.getInstance().createDto(Account.class)
                         .withName(name)
                         .withId("account0xffaassdeereqWsss")
                         .withOwner("userId122332133123")
                         .withAttributes(Arrays.asList(
                                 DtoFactory.getInstance().createDto(Attribute.class).withName("attribute1").withValue("value")
                                           .withDescription("important attribute")));
    }

    @Override
    public Account getByOwner(String owner) throws AccountException {
        return DtoFactory.getInstance().createDto(Account.class)
                         .withName("account_name")
                         .withId("account0xffaassdeereqWsss")
                         .withOwner(owner)
                         .withAttributes(Arrays.asList(
                                 DtoFactory.getInstance().createDto(Attribute.class).withName("attribute1").withValue("value")
                                           .withDescription("important attribute")));
    }

    @Override
    public void update(Account account) throws AccountException {
        throw new RuntimeException("Not implemented");

    }

    @Override
    public void remove(String id) throws AccountException {
        throw new RuntimeException("Not implemented");

    }

    @Override
    public void addMember(Member member) throws AccountException {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public void removeMember(String accountId, String userId) throws AccountException {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public void addSubscription(Subscription subscription, String accountId) throws AccountException {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public void removeSubscription(String accountId, String serviceId) throws AccountException {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public List<Subscription> getSubscriptions(String accountId) throws AccountException {
        return Arrays.asList(DtoFactory.getInstance().createDto(Subscription.class)
                                       .withStartDate("2013-12-01")
                                       .withEndDate("2013-12-01")
                                       .withServiceId("serviceId")
                                       .withProperties(new HashMap<String, String>())
                            );
    }

    @Override
    public List<Member> getMembers(String accountId) throws AccountException {
        return Arrays.asList(DtoFactory.getInstance().createDto(Member.class).withAccountId(accountId)
                                       .withUserId("userId122332133123").withRoles(Arrays.asList("account/owner")),
                             DtoFactory.getInstance().createDto(Member.class).withAccountId(accountId)
                                       .withUserId("userId112233322239").withRoles(Arrays.asList("account/member")));
    }
}