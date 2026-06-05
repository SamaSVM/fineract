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
package org.apache.fineract.infrastructure.dataqueries.data;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;

public final class ReportParameters {

    // Private constructor to prevent instantiation
    private ReportParameters() {
        throw new AssertionError("Utility class should not be instantiated");
    }

    private static final String EXPORT_CSV = "exportCSV";
    private static final String PARAMETER_TYPE = "parameterType";
    private static final String OUTPUT_TYPE = "output-type";
    private static final String ENABLE_BUSINESS_DATE = "enable-business-date";
    private static final String OBLIG_DATE_TYPE = "obligDateType";
    private static final String DECIMAL_CHOICE = "decimalChoice";
    private static final String PORTFOLIO_RISK_BRANCH = "Portfolio at Risk by Branch";

    public static final String FULL_DESCRIPTION = """
            This resource allows you to run and receive output from pre-defined Apache Fineract reports.

            Reports can also be used to provide data for searching and workflow functionality.

            The default output is a JSON formatted "Generic Resultset". The Generic Resultset contains Column Heading as well as Data information. However, you can export to CSV format by simply adding "&exportCSV=true" to the end of your URL.

            If Pentaho reports have been pre-defined, they can also be run through this resource. Pentaho reports can return HTML, PDF or CSV formats.

            The Apache Fineract reference application uses a JQuery plugin called stretchy reporting which, itself, uses this reports resource to provide a pretty flexible reporting User Interface (UI).



            Example Requests:

            runreports/Client%20Listing?R_officeId=1


            runreports/Client%20Listing?R_officeId=1&exportCSV=true


            runreports/OfficeIdSelectOne?R_officeId=1&parameterType=true


            runreports/OfficeIdSelectOne?R_officeId=1&parameterType=true&exportCSV=true


            runreports/Expected%20Payments%20By%20Date%20-%20Formatted?R_endDate=2013-04-30&R_loanOfficerId=-1&R_officeId=1&R_startDate=2013-04-16&output-type=HTML&R_officeId=1


            runreports/Expected%20Payments%20By%20Date%20-%20Formatted?R_endDate=2013-04-30&R_loanOfficerId=-1&R_officeId=1&R_startDate=2013-04-16&output-type=XLS&R_officeId=1


            runreports/Expected%20Payments%20By%20Date%20-%20Formatted?R_endDate=2013-04-30&R_loanOfficerId=-1&R_officeId=1&R_startDate=2013-04-16&output-type=CSV&R_officeId=1


            runreports/Expected%20Payments%20By%20Date%20-%20Formatted?R_endDate=2013-04-30&R_loanOfficerId=-1&R_officeId=1&R_startDate=2013-04-16&output-type=PDF&R_officeId=1

            **Available Parameters (All Optional):**

            **Common Control Parameters:**
            - `exportCSV`: Set to true to export results as CSV (default: false)
            - `parameterType`: Indicates if this is a parameter type request (default: false)
            - `output-type`: Output format type (HTML, XLS, CSV, PDF)
            - `enable-business-date`: Enable business date filtering
            - `obligDateType`: Obligation date type
            - `decimalChoice`: Decimal formatting choice
            - `Portfolio at Risk by Branch`: Portfolio risk parameter

            **Common Report Parameters (R_ prefixed):**
            - `R_officeId`: Office ID filter
            - `R_loanOfficerId`: Loan officer ID filter
            - `R_currencyId`: Currency ID filter
            - `R_fromDate`, `R_toDate`: Date range filters (yyyy-MM-dd)
            - `R_accountNo`: Account number filter
            - `R_transactionId`: Transaction ID filter
            - `R_centerId`: Center ID filter
            - `R_branch`: Branch filter
            - `R_ondate`: Specific date filter
            - `R_cycleX`, `R_cycleY`: Cycle filters
            - `R_fromX`, `R_toY`: Range filters
            - `R_overdueX`, `R_overdueY`: Overdue filters
            - `R_endDate`: End date filter

            **Other Common Parameters:**
            - `OfficeId`: Office ID filter (alternative)
            - `loanOfficerId`: Loan officer ID filter (alternative)
            - `currencyId`: Currency ID filter (alternative)
            - `fundId`: Fund ID filter
            - `loanProductId`: Loan product ID filter
            - `loanPurposeId`: Loan purpose ID filter
            - `parType`: Portfolio at risk type
            - `SelectGLAccountNO`: GL account number selection
            - `SavingsAccountSubStatus`: Savings account status
            - `SelectLoanType`: Loan type selection

            **Note:** All parameters are optional and report-specific.\s
            The exact parameters required depend on the specific report being executed.
            Some reports may accept additional parameters not listed here.""";

    @Parameters({ @Parameter(name = EXPORT_CSV, description = "Optional - Set to true to export results as CSV", example = "true"),
            @Parameter(name = PARAMETER_TYPE, description = "Optional - Indicates if this is a parameter type request", example = "false"),
            @Parameter(name = OUTPUT_TYPE, description = "Optional - Output format type (HTML, XLS, CSV, PDF)", example = "HTML"),
            @Parameter(name = ENABLE_BUSINESS_DATE, description = "Optional - Enable business date filtering", example = "true"),
            @Parameter(name = OBLIG_DATE_TYPE, description = "Optional - Obligation date type", example = "due"),
            @Parameter(name = DECIMAL_CHOICE, description = "Optional - Decimal formatting choice", example = "2"),
            @Parameter(name = PORTFOLIO_RISK_BRANCH, description = "Optional - Portfolio at Risk by Branch parameter", example = "30"),

            @Parameter(name = "R_officeId", description = " Office ID filter", example = "1"),
            @Parameter(name = "R_loanOfficerId", description = "Optional - Loan officer ID filter", example = "5"),
            @Parameter(name = "R_currencyId", description = "Optional - Currency ID filter", example = "USD"),
            @Parameter(name = "R_fromDate", description = "Optional - Start date filter (yyyy-MM-dd)", example = "2023-01-01"),
            @Parameter(name = "R_toDate", description = "Optional - End date filter (yyyy-MM-dd)", example = "2023-12-31"),
            @Parameter(name = "R_accountNo", description = "Optional - Account number filter", example = "00010001"),
            @Parameter(name = "R_transactionId", description = "Optional - Transaction ID filter", example = "12345"),
            @Parameter(name = "R_centerId", description = "Optional - Center ID filter", example = "10"),
            @Parameter(name = "R_branch", description = "Optional - Branch filter", example = "Main"),
            @Parameter(name = "R_ondate", description = "Optional - Specific date filter", example = "2023-06-15"),
            @Parameter(name = "R_cycleX", description = "Optional - Cycle X filter", example = "1"),
            @Parameter(name = "R_cycleY", description = "Optional - Cycle Y filter", example = "12"),
            @Parameter(name = "R_fromX", description = "Optional - From X value filter", example = "0"),
            @Parameter(name = "R_toY", description = "Optional - To Y value filter", example = "100"),
            @Parameter(name = "R_overdueX", description = "Optional - Overdue X days filter", example = "30"),
            @Parameter(name = "R_overdueY", description = "Optional - Overdue Y days filter", example = "90"),
            @Parameter(name = "R_endDate", description = "Optional - End date filter", example = "2023-12-31"),

            @Parameter(name = "OfficeId", description = "Optional - Office ID filter (alternative)", example = "1"),
            @Parameter(name = "loanOfficerId", description = "Optional - Loan officer ID filter (alternative)", example = "5"),
            @Parameter(name = "currencyId", description = "Optional - Currency ID filter (alternative)", example = "USD"),
            @Parameter(name = "fundId", description = "Optional - Fund ID filter", example = "1"),
            @Parameter(name = "loanProductId", description = "Optional - Loan product ID filter", example = "2"),
            @Parameter(name = "loanPurposeId", description = "Optional - Loan purpose ID filter", example = "3"),
            @Parameter(name = "parType", description = "Optional - Portfolio at risk type", example = "30"),
            @Parameter(name = "SelectGLAccountNO", description = "Optional - GL account number selection", example = "11001"),
            @Parameter(name = "SavingsAccountSubStatus", description = "Optional - Savings account sub-status", example = "active"),
            @Parameter(name = "SelectLoanType", description = "Optional - Loan type selection", example = "individual"),

            @Parameter(name = "R_*", description = "Optional - Additional report-specific parameters prefixed with 'R_'") })

    public static void getOpenApiParameters() {

    }

    public static String getExportCsv() {
        return EXPORT_CSV;
    }

    public static String getParameterType() {
        return PARAMETER_TYPE;
    }

    public static String getOutputType() {
        return OUTPUT_TYPE;
    }

    public static String getEnableBusinessDate() {
        return ENABLE_BUSINESS_DATE;
    }

    public static String getObligDateType() {
        return OBLIG_DATE_TYPE;
    }

    public static String getDecimalChoice() {
        return DECIMAL_CHOICE;
    }

    public static String getPortfolioRiskBranch() {
        return PORTFOLIO_RISK_BRANCH;
    }

    public static String getFullDescription() {
        return FULL_DESCRIPTION;
    }

    @Parameters({ @Parameter(name = EXPORT_CSV, description = "Optional - Set to true to export results as CSV", example = "true"),
            @Parameter(name = PARAMETER_TYPE, description = "Optional - Indicates if this is a parameter type request", example = "false"),
            @Parameter(name = OUTPUT_TYPE, description = "Optional - Output format type (HTML, XLS, CSV, PDF)", example = "HTML"),
            @Parameter(name = "R_*", description = "Optional - Report-specific parameters prefixed with 'R_'") })
    public static void getMinimalOpenApiParameters() {

    }
}
