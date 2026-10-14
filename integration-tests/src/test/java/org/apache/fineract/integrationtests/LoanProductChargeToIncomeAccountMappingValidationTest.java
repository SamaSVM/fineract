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
package org.apache.fineract.integrationtests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import org.apache.fineract.client.models.PostLoanProductsRequest;
import org.apache.fineract.integrationtests.client.feign.FeignLoanTestBase;
import org.apache.fineract.integrationtests.common.CommonConstants;
import org.apache.fineract.integrationtests.common.accounting.Account;
import org.apache.fineract.integrationtests.common.loans.LoanProductTestBuilder;
import org.junit.jupiter.api.Test;

/**
 * Regression tests for FINERACT-2948-style bug in {@code LoanProductDataValidator}, where
 * {@code feeToIncomeAccountMappings} was never validated and {@code penaltyToIncomeAccountMappings} errors were
 * reported twice.
 */
public class LoanProductChargeToIncomeAccountMappingValidationTest extends FeignLoanTestBase {

    private static final String PARAMETER_NAME = "parameterName";

    @Test
    public void createRejectsFeeToIncomeAccountMappingWithMissingChargeId() {
        final Account[] accounts = createAccountingAccounts();
        final Long incomeAccountId = accounts[1].getAccountID().longValue();

        final PostLoanProductsRequest request = new LoanProductTestBuilder().withAccountingRulePeriodicAccrual(accounts)
                .withFeeToIncomeAccountMapping(null, incomeAccountId).buildRequest(null, null);

        final ArrayList<HashMap<String, Object>> errors = getLoanProductError(request, CommonConstants.RESPONSE_ERROR);

        assertNotNull(errors);
        assertTrue(errors.stream().anyMatch(error -> "feeToIncomeAccountMappings[0].chargeId".equals(error.get(PARAMETER_NAME))),
                "Expected a validation error for parameter 'feeToIncomeAccountMappings[0].chargeId', got: " + errors);
    }

    @Test
    public void createReportsPenaltyToIncomeAccountMappingErrorOnlyOnce() {
        final Account[] accounts = createAccountingAccounts();
        final Long incomeAccountId = accounts[1].getAccountID().longValue();

        final PostLoanProductsRequest request = new LoanProductTestBuilder().withAccountingRulePeriodicAccrual(accounts)
                .withPenaltyToIncomeAccountMapping(null, incomeAccountId).buildRequest(null, null);

        final ArrayList<HashMap<String, Object>> errors = getLoanProductError(request, CommonConstants.RESPONSE_ERROR);

        assertNotNull(errors);
        final long matchingErrorCount = errors.stream()
                .filter(error -> "penaltyToIncomeAccountMappings[0].chargeId".equals(error.get(PARAMETER_NAME))).count();
        assertEquals(1, matchingErrorCount,
                "Expected exactly one validation error for parameter " + "'penaltyToIncomeAccountMappings[0].chargeId', got: " + errors);
    }

    @Test
    public void createSucceedsWithValidFeeAndPenaltyToIncomeAccountMappings() {
        final Account[] accounts = createAccountingAccounts();
        final Long incomeAccountId = accounts[1].getAccountID().longValue();
        final Long feeChargeId = createInstallmentFeeCharge(10.0);
        final Long penaltyChargeId = createInstallmentPenaltyCharge(10.0);

        final PostLoanProductsRequest request = new LoanProductTestBuilder().withAccountingRulePeriodicAccrual(accounts)
                .withFeeToIncomeAccountMapping(feeChargeId, incomeAccountId)
                .withPenaltyToIncomeAccountMapping(penaltyChargeId, incomeAccountId).buildRequest(null, null);

        final Long loanProductId = createLoanProduct(request);

        assertNotNull(loanProductId);
    }

    private Account[] createAccountingAccounts() {
        return new Account[] { accountHelper.createAssetAccount(), accountHelper.createIncomeAccount(),
                accountHelper.createExpenseAccount(), accountHelper.createLiabilityAccount() };
    }
}
