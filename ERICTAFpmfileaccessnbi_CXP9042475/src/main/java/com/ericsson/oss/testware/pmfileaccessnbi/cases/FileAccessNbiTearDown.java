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

package com.ericsson.oss.testware.pmfileaccessnbi.cases;

import com.ericsson.oss.testware.pmfileaccessnbi.common.constants.FileAccessNbiConstants;
import com.ericsson.oss.testware.pmfileaccessnbi.common.utils.Utils;
import com.google.inject.Inject;

import org.testng.annotations.AfterSuite;
import com.ericsson.cifwk.taf.TafTestBase;
import com.ericsson.oss.testware.pmfileaccessnbi.common.utils.FileAccessNbiScenarioUtils;
import org.testng.annotations.Parameters;

/**
 * Tear down used in testing for TAF.
 */
public class FileAccessNbiTearDown extends TafTestBase {
    @Inject FileAccessNbiScenarioUtils fileAccessNbiScenarioUtils;

    @AfterSuite(alwaysRun = true, groups = { "RFA250" }, enabled = true, description = "Teardown")
    @Parameters({"RemoveUsers"})
    public void executeTearDown() {
        // Removing files that was created before in the setup
        boolean fileRemovedPMIC3 = Utils.removeFileInPMStorage(FileAccessNbiConstants.PMIC1_FULL_PATH, FileAccessNbiConstants.PMIC1_FILE);
        boolean fileRemovedPMIC2 = Utils.removeFileInPMStorage(FileAccessNbiConstants.PMIC2_FULL_PATH, FileAccessNbiConstants.PMIC2_FILE);
        boolean fileRemovedPMUL = Utils.removeFileInPMStorage(FileAccessNbiConstants.PMUL_FULL_PATH, FileAccessNbiConstants.PMUL_FILE);

        if (!fileRemovedPMIC3 || !fileRemovedPMIC2 || !fileRemovedPMUL) {
            Utils.LOGGER.warn("Failed to delete test files from the PM Storage. Please check log for more info.");
        }
        fileAccessNbiScenarioUtils.removeUsers();
    }
}
