package com.ericsson.oss.testware.pmfileaccessnbi.cases;

import static com.google.common.truth.Truth.assertThat;

import javax.inject.Inject;


import com.ericsson.cifwk.taf.tools.http.HttpResponse;
import com.ericsson.cifwk.taf.tools.http.HttpTool;

import com.ericsson.cifwk.taf.annotations.Input;
import com.ericsson.cifwk.taf.annotations.TestStep;

import static com.ericsson.oss.testware.pmfileaccessnbi.common.constants.FileAccessNbiConstants.*;

import com.ericsson.oss.testware.pmfileaccessnbi.common.utils.Utils;
import com.ericsson.oss.testware.security.authentication.tool.TafToolProvider;

import java.util.Map;

public class FileAccessNbiSteps {
    @Inject
    private TafToolProvider tafToolProvider;

    /**
     * Given Injected taf tool provider, and inputs from the data sources, retrieve result from FAN and assert if the
     * result and status code are expected
     *
     * @param filePath
     *            File path to the PM storage
     * @param expectedStatusCode
     *            Expected status code
     * @param expectedContent
     *            Expected content
     */
    @TestStep(id = StepIds.REQUEST_FILE_FROM_FILE_ACCESS_NBI)
    public void requestFileFromFileAccessNBI(@Input(REQUEST_FILE_PATH_INPUT) String filePath,
                                             @Input(EXPECTED_STATUS_CODE) int expectedStatusCode,
                                             @Input(EXPECTED_CONTENT) String expectedContent) {
        String resBody = "";
        int resStatusCode = -1;
        try {
            // Get user session from loginLogout function in the scenario
            HttpTool httptool = tafToolProvider.getHttpTool();
            Map<String, String> cookiesInfo = httptool.getCookies();
            String user = cookiesInfo.get("TorUserID");
            Utils.LOGGER.info("User : " + user);

            //To get the domain
            Utils.LOGGER.info("Domain URL LOGGER: " + httptool.getBaseUrl());

            // Domain is not required to form the url as HttpTool.get() method will take it by default
            Utils.LOGGER.info("Requesting for : " + httptool.getBaseUrl() + filePath);

            // make REST call to download pm file
            HttpResponse response = httptool.request().get(filePath);
            resBody = response.getBody().trim();
            resStatusCode = response.getResponseCode().getCode();

            String logMsg = "Expected: " + expectedStatusCode + ", Received: " + resStatusCode + ", File path: " + filePath + " User: " + user;
            if (expectedStatusCode == resStatusCode) {
                Utils.LOGGER.info(logMsg);
            } else {
                Utils.LOGGER.error(logMsg);
            }
        } catch (Exception e) {
            Utils.LOGGER.error("Failed to request the file. File: " + filePath, e);
        } finally {
            assertThat(resStatusCode).isEqualTo(expectedStatusCode);
            assertThat(resBody).contains(expectedContent);
        }
    }

    public static final class StepIds {
        public static final String REQUEST_FILE_FROM_FILE_ACCESS_NBI = "requestFileFromFileAccessNBI";
        private StepIds() {
        }
    }
}