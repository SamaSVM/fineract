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
package org.apache.fineract.portfolio.shareaccounts.serialization;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.apache.fineract.infrastructure.core.api.JsonCommand;
import org.apache.fineract.infrastructure.core.exception.PlatformApiDataValidationException;
import org.apache.fineract.infrastructure.core.serialization.FromJsonHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Regression test for the close validation reporting the wrong parameter name. {@code validateAndClose} reads the
 * {@code closedDate} field but used to report the missing-value error under the {@code approvedDate} parameter name,
 * which {@code closeParameters} does not even accept - leaving clients unable to discover the real required field.
 */
class ShareAccountDataSerializerTest {

    private FromJsonHelper fromJsonHelper;
    private ShareAccountDataSerializer serializer;

    @BeforeEach
    void setUp() {
        fromJsonHelper = new FromJsonHelper();
        serializer = new ShareAccountDataSerializer(null, fromJsonHelper, null, null, null, null, null);
    }

    private JsonCommand jsonCommand(String json) {
        return new JsonCommand(1L, fromJsonHelper.parse(json), fromJsonHelper);
    }

    @Test
    void closeWithoutClosedDateReportsClosedDateParameter() {
        PlatformApiDataValidationException ex = assertThrows(PlatformApiDataValidationException.class,
                () -> serializer.validateAndClose(jsonCommand("""
                        {
                          "locale": "en",
                          "dateFormat": "dd MMMM yyyy"
                        }
                        """), null));

        assertEquals(1, ex.getErrors().size());
        assertEquals("closedDate", ex.getErrors().get(0).getParameterName());
        assertEquals("validation.msg.sharesaccount.closedDate.cannot.be.blank", ex.getErrors().get(0).getUserMessageGlobalisationCode());
        assertTrue(ex.getErrors().stream().noneMatch(e -> "approvedDate".equals(e.getParameterName())),
                "approvedDate is not an accepted close parameter and must never be reported");
    }
}
