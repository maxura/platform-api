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
package com.codenvy.api.factory;

import java.lang.annotation.*;

/**
 * Provide factory parameter compatibility options.
 *
 * @author Alexander Garagatyi
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Compatibility {
    public enum Optionality {
        MANDATORY, OPTIONAL
    }

    public enum Encoding {
        ENCODED, NONENCODED, BOTH
    }

    public enum Version {
        // NEVER must be the last constant
        V1_0, V1_1, V1_2, NEVER;

        public static Version fromString(String v) {
            if (null != v) {
                switch (v) {
                    case "1.0" :
                        return V1_0;
                    case "1.1" :
                        return V1_1;
                    case "1.2" :
                        return V1_2;
                }
            }

            throw new IllegalArgumentException(String.format("Unknown version %s.", v));
        }
    }

    public Encoding encoding() default Encoding.BOTH;

    public Optionality optionality();

    public boolean trackedOnly() default false;

    public Version deprecatedSince() default Version.NEVER;

    public Version ignoredSince() default Version.NEVER;

    public Class<? extends CompatibilityConverter> converter() default CompatibilityConverter.DefaultCompatibilityConverter.class;
}
