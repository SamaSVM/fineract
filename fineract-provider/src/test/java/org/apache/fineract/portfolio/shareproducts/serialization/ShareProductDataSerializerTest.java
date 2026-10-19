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
package org.apache.fineract.portfolio.shareproducts.serialization;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.apache.fineract.infrastructure.core.api.JsonCommand;
import org.apache.fineract.infrastructure.core.exception.PlatformApiDataValidationException;
import org.apache.fineract.infrastructure.core.serialization.FromJsonHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Regression test for the dividend period validation reporting the wrong parameter name. {@code
 * validateDividendDetails} reads the {@code dividendPeriodEndDate} field but used to report both the "missing value"
 * and the "end before start" errors under the {@code dividendPeriodStartDate} parameter name.
 */
class ShareProductDataSerializerTest {

    private FromJsonHelper fromJsonHelper;
    private ShareProductDataSerializer serializer;

    @BeforeEach
    void setUp() {
        fromJsonHelper = new FromJsonHelper();
        serializer = new ShareProductDataSerializer(fromJsonHelper, null, null);
    }

    private JsonCommand jsonCommand(String json) {
        return new JsonCommand(1L, fromJsonHelper.parse(json), fromJsonHelper);
    }

    @Test
    void dividendWithoutEndDateReportsEndDateParameter() {
        PlatformApiDataValidationException ex = assertThrows(PlatformApiDataValidationException.class,
                () -> serializer.validateDividendDetails(jsonCommand("""
                        {
                          "locale": "en",
                          "dateFormat": "dd MMMM yyyy",
                          "dividendPeriodStartDate": "01 January 2026",
                          "dividendAmount": "100"
                        }
                        """)));

        assertTrue(
                ex.getErrors().stream()
                        .anyMatch(e -> "dividendPeriodEndDate".equals(e.getParameterName())
                                && "validation.msg.sharesproduct.dividend.processing.dividendPeriodEndDate.cannot.be.blank"
                                        .equals(e.getUserMessageGlobalisationCode())),
                "Expected a cannot.be.blank error reported against 'dividendPeriodEndDate'");
    }

    @Test
    void dividendWithEndDateBeforeStartDateReportsEndDateParameter() {
        PlatformApiDataValidationException ex = assertThrows(PlatformApiDataValidationException.class,
                () -> serializer.validateDividendDetails(jsonCommand("""
                        {
                          "locale": "en",
                          "dateFormat": "dd MMMM yyyy",
                          "dividendPeriodStartDate": "01 January 2026",
                          "dividendPeriodEndDate": "01 December 2025",
                          "dividendAmount": "100"
                        }
                        """)));

        assertEquals(1, ex.getErrors().size());
        assertEquals("dividendPeriodEndDate", ex.getErrors().get(0).getParameterName());
        assertEquals("validation.msg.sharesproduct.dividend.processing.dividendPeriodEndDate.is.less.than.date",
                ex.getErrors().get(0).getUserMessageGlobalisationCode());
    }
}
