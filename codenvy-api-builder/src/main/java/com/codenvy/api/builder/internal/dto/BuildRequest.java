/*
 * CODENVY CONFIDENTIAL
 * __________________
 *
 *  [2012] - [2013] Codenvy, S.A.
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
package com.codenvy.api.builder.internal.dto;

import com.codenvy.api.core.rest.dto.DtoType;

import java.util.List;
import java.util.Map;

/**
 * Build request.
 *
 * @author <a href="mailto:andrew00x@gmail.com">Andrey Parfonov</a>
 */
@DtoType(BuilderDtoTypes.BUILD_REQUEST_TYPE)
public class BuildRequest extends BaseBuilderRequest {

    public BuildRequest(String sourcesUrl,
                        String builder,
                        List<String> targets,
                        Map<String, String> options,
                        String workspace,
                        String project,
                        String username) {
        super(sourcesUrl, builder, targets, options, workspace, project, username);
    }

    public BuildRequest() {
        super();
    }

    @Override
    public String toString() {
        return "BuildRequest{" +
               "sourcesUrl='" + sourcesUrl + '\'' +
               ", builder='" + builder + '\'' +
               ", targets=" + targets +
               ", options=" + options +
               ", workspace='" + workspace + '\'' +
               ", project='" + project + '\'' +
               ", username='" + username + '\'' +
               '}';
    }
}