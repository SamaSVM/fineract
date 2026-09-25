/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.fineract.infrastructure.core.data;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class DataValidatorBuilderOneOfTheseStringValuesTest {

    private static final String RESOURCE = "test";
    private static final String PARAMETER = "action";

    private enum TestAction {
        PAUSE, RESCHEDULE
    }

    @Test
    void varargsAcceptsExactLowercaseMatch() {
        final List<ApiParameterError> errors = new ArrayList<>();
        new DataValidatorBuilder(errors).resource(RESOURCE).parameter(PARAMETER).value("pause").isOneOfTheseStringValues("pause",
                "reschedule");
        assertThat(errors).isEmpty();
    }

    @Test
    void varargsAcceptsMixedCaseInputAgainstLowercaseValues() {
        final List<ApiParameterError> errors = new ArrayList<>();
        new DataValidatorBuilder(errors).resource(RESOURCE).parameter(PARAMETER).value("PaUsE").isOneOfTheseStringValues("pause",
                "reschedule");
        assertThat(errors).isEmpty();
    }

    @Test
    void varargsAcceptsExactMatchAgainstUppercaseDeclaredValues() {
        final List<ApiParameterError> errors = new ArrayList<>();
        new DataValidatorBuilder(errors).resource(RESOURCE).parameter(PARAMETER).value("PAUSE").isOneOfTheseStringValues("PAUSE",
                "RESCHEDULE");
        assertThat(errors).isEmpty();
    }

    @Test
    void varargsAcceptsLowercaseInputAgainstUppercaseDeclaredValues() {
        final List<ApiParameterError> errors = new ArrayList<>();
        new DataValidatorBuilder(errors).resource(RESOURCE).parameter(PARAMETER).value("pause").isOneOfTheseStringValues("PAUSE");
        assertThat(errors).isEmpty();
    }

    @Test
    void varargsRejectsUnknownValue() {
        final List<ApiParameterError> errors = new ArrayList<>();
        new DataValidatorBuilder(errors).resource(RESOURCE).parameter(PARAMETER).value("unknown").isOneOfTheseStringValues("pause",
                "reschedule");
        assertThat(errors).hasSize(1);
        assertThat(errors.getFirst().getParameterName()).isEqualTo("action");
        assertThat(errors.getFirst().getUserMessageGlobalisationCode())
                .isEqualTo("validation.msg.test.action.is.not.one.of.expected.enumerations");
        assertThat(errors.getFirst().getDeveloperMessage()).contains("pause, reschedule");
    }

    @Test
    void varargsReportsErrorForNullValueWhenIgnoreNullNotSet() {
        final List<ApiParameterError> errors = new ArrayList<>();
        new DataValidatorBuilder(errors).resource(RESOURCE).parameter(PARAMETER).value(null).isOneOfTheseStringValues("pause",
                "reschedule");
        assertThat(errors).hasSize(1);
    }

    @Test
    void varargsIgnoresNullValueWhenIgnoreIfNullSet() {
        final List<ApiParameterError> errors = new ArrayList<>();
        new DataValidatorBuilder(errors).resource(RESOURCE).parameter(PARAMETER).value(null).ignoreIfNull()
                .isOneOfTheseStringValues("pause", "reschedule");
        assertThat(errors).isEmpty();
    }

    @ParameterizedTest
    @ValueSource(strings = { "pause", "PAUSE", "PaUsE" })
    void listOverloadAcceptsAnyCaseAgainstMixedCaseDeclaredValues(final String value) {
        final List<ApiParameterError> errors = new ArrayList<>();
        new DataValidatorBuilder(errors).resource(RESOURCE).parameter(PARAMETER).value(value)
                .isOneOfTheseStringValues(List.of("PAUSE", "Reschedule"));
        assertThat(errors).isEmpty();
    }

    @Test
    void listOverloadRejectsUnknownValue() {
        final List<ApiParameterError> errors = new ArrayList<>();
        new DataValidatorBuilder(errors).resource(RESOURCE).parameter(PARAMETER).value("unknown")
                .isOneOfTheseStringValues(List.of("pause"));
        assertThat(errors).hasSize(1);
    }

    @Test
    void listOverloadReportsErrorForNullValueWhenIgnoreNullNotSet() {
        final List<ApiParameterError> errors = new ArrayList<>();
        new DataValidatorBuilder(errors).resource(RESOURCE).parameter(PARAMETER).value(null).isOneOfTheseStringValues(List.of("pause"));
        assertThat(errors).hasSize(1);
    }

    @Test
    void listOverloadIgnoresNullValueWhenIgnoreIfNullSet() {
        final List<ApiParameterError> errors = new ArrayList<>();
        new DataValidatorBuilder(errors).resource(RESOURCE).parameter(PARAMETER).value(null).ignoreIfNull()
                .isOneOfTheseStringValues(List.of("pause"));
        assertThat(errors).isEmpty();
    }

    @ParameterizedTest
    @ValueSource(strings = { "pause", "PAUSE" })
    void isOneOfEnumValuesIsCaseInsensitive(final String value) {
        final List<ApiParameterError> errors = new ArrayList<>();
        new DataValidatorBuilder(errors).resource(RESOURCE).parameter(PARAMETER).value(value).isOneOfEnumValues(TestAction.class);
        assertThat(errors).isEmpty();
    }

    @Test
    void isOneOfEnumValuesRejectsUnknownValue() {
        final List<ApiParameterError> errors = new ArrayList<>();
        new DataValidatorBuilder(errors).resource(RESOURCE).parameter(PARAMETER).value("unknown").isOneOfEnumValues(TestAction.class);
        assertThat(errors).hasSize(1);
    }
}
