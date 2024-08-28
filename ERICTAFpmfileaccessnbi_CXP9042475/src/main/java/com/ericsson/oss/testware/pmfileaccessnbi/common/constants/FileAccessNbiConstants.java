/*------------------------------------------------------------------------------
 *******************************************************************************
 * COPYRIGHT Ericsson 2016
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 *******************************************************************************
 *----------------------------------------------------------------------------*/

package com.ericsson.oss.testware.pmfileaccessnbi.common.constants;

import com.ericsson.cifwk.taf.configuration.TafConfiguration;
import com.ericsson.cifwk.taf.configuration.TafConfigurationProvider;

/**
 * Constants used in testing.
 */
public final class FileAccessNbiConstants {
    /* DATASOURCES */
    public static final String FAN_AUTHORIZED_DS = "fanAuthorizedDS";
    public static final String FAN_UNAUTHORIZED_DS= "fanUnauthorizedDS";
    public static final String FILE_FOR_AUTHORIZED_DS = "fileForAuthorizedDS";
    public static final String FILE_FOR_UNAUTHORIZED_DS = "fileForUnauthorizedDS";

    /* USERS */
    public static final String AUTHORIZED_USER = "fanAuthorizedUser";
    public static final String UNAUTHORIZED_USER = "fanUnauthorizedUser";
    public static final String USERNAME_FILTER = "username==" + FAN_AUTHORIZED_DS + ".user";
    /* CONFIG */
    public static final String TRUE = "true";
    public static final TafConfiguration tafConfiguration = TafConfigurationProvider.provide();
    public static final String CREATE_USERS = tafConfiguration.getString("nbi.createUsers", TRUE);
    public static final String REMOVE_USERS = tafConfiguration.getString("nbi.removeUsers", TRUE);
    public static final String USER_TO_CREATE = "usersToCreate.csv";
    public static final String FAN_HTML_PATH = "/var/www/html/";
    public static final String FAN_FILES_PATH = "file/v1/files/ericsson/";
    public static final String PMIC1_PATH = "pmic1/";
    public static final String PMIC2_PATH = "pmic2/";
    public static final String PMUL_PATH = "ul_spectrum_files/";

    public static final String PMIC1_FULL_PATH = FAN_HTML_PATH + FAN_FILES_PATH + PMIC1_PATH;
    public static final String PMIC2_FULL_PATH = FAN_HTML_PATH + FAN_FILES_PATH + PMIC2_PATH;
    public static final String PMUL_FULL_PATH = FAN_HTML_PATH + FAN_FILES_PATH + PMUL_PATH;

    public static final String PMIC1_FILE = "testFilePmic1.txt";
    public static final String PMIC2_FILE = "testFilePmic2.txt";
    public static final String PMUL_FILE = "testFilePmul.txt";
    public static final String REQUEST_FILE_PATH_INPUT = "requestFilePath";
    public static final String EXPECTED_CONTENT = "expectedContent";
    public static final String EXPECTED_STATUS_CODE = "expectedStatusCode";
    public static final String TEST_STR = "Test";


    private FileAccessNbiConstants() {
    }
}
