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
package org.apache.fineract.portfolio.savings.data;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import org.apache.fineract.infrastructure.core.data.ApiParameterError;
import org.apache.fineract.infrastructure.core.data.DataValidatorBuilder;
import org.apache.fineract.infrastructure.core.serialization.FromJsonHelper;
import org.junit.jupiter.api.Test;

class SavingsProductAccountingDataValidatorTest {

    private final FromJsonHelper fromJsonHelper = new FromJsonHelper();
    private final SavingsProductAccountingDataValidator underTest = new SavingsProductAccountingDataValidator(fromJsonHelper);

    @Test
    void validateChargeToIncomeAccountMappingsRejectsFeeMappingWithMissingChargeId() {
        final String json = """
                {"feeToIncomeAccountMappings": [{"incomeAccountId": 5}]}
                """;
        List<ApiParameterError> errors = new ArrayList<>();
        DataValidatorBuilder baseDataValidator = new DataValidatorBuilder(errors).resource("savingsproduct");

        underTest.validateChargeToIncomeAccountMappings(baseDataValidator, fromJsonHelper.parse(json));

        assertTrue(errors.stream().anyMatch(error -> "feeToIncomeAccountMappings[0].chargeId".equals(error.getParameterName())),
                "Expected a validation error for parameter 'feeToIncomeAccountMappings[0].chargeId', got: " + errors);
    }

    @Test
    void validateChargeToIncomeAccountMappingsReportsPenaltyMappingErrorOnlyOnce() {
        final String json = """
                {"penaltyToIncomeAccountMappings": [{"incomeAccountId": 5}]}
                """;
        List<ApiParameterError> errors = new ArrayList<>();
        DataValidatorBuilder baseDataValidator = new DataValidatorBuilder(errors).resource("savingsproduct");

        underTest.validateChargeToIncomeAccountMappings(baseDataValidator, fromJsonHelper.parse(json));

        long matchingErrorCount = errors.stream()
                .filter(error -> "penaltyToIncomeAccountMappings[0].chargeId".equals(error.getParameterName())).count();
        assertEquals(1, matchingErrorCount,
                "Expected exactly one validation error for parameter 'penaltyToIncomeAccountMappings[0].chargeId', got: " + errors);
    }
}
