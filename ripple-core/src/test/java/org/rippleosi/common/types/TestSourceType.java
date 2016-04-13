/*
 * Copyright 2015 Ripple OSI
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.rippleosi.common.types;

/**
 */
public enum TestSourceType implements RepoSource {
    SOURCE1("Source 1"),
    SOURCE2("Source 2");

    private final String sourceName;

    private TestSourceType(final String sourceName) {
        this.sourceName = sourceName;
    }

    @Override
    public String getSourceName() {
        return sourceName;
    }

    public static RepoSource fromString(final String sourceName) {
        if (sourceName == null) {
            return null;
        }

        for (RepoSource enumValue : TestSourceType.values()) {
            if (enumValue.getSourceName().equalsIgnoreCase(sourceName)) {
                return enumValue;
            }
        }

        return null;
    }
}
