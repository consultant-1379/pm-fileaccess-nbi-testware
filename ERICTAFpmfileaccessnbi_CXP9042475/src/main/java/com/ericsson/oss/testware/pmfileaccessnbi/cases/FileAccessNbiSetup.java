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

import javax.inject.Inject;

import com.ericsson.oss.testware.pmfileaccessnbi.common.constants.FileAccessNbiConstants;
import com.ericsson.oss.testware.pmfileaccessnbi.common.utils.Utils;
import org.testng.annotations.BeforeSuite;
import com.ericsson.cifwk.taf.TafTestBase;
import com.ericsson.oss.testware.pmfileaccessnbi.common.utils.FileAccessNbiScenarioUtils;
import org.testng.annotations.Parameters;

/**
 * Pre Conditions used in testing for TAF.
 */
public class FileAccessNbiSetup extends TafTestBase {
    @Inject private FileAccessNbiScenarioUtils fileAccessNbiScenarioUtils;

    @BeforeSuite(alwaysRun = true, groups = { "RFA250" }, enabled = true, description = "Preconditions Setup")
    @Parameters({"MockFileInPMStorages", "CreateUsers"})
    public void executePreConditions() throws Exception {
        try {
            // Check if namespace running
            String nameSpace = Utils.getNameSpace();
            if (nameSpace != null) {
                Utils.LOGGER.info("Namespace is running.");
                // Check pod is running
                boolean IsFANPodRunning = Utils.isFANPodRunning(nameSpace);
                if (!IsFANPodRunning) {
                    throw new Exception("FAN Pod is not detected.");
                }
            } else {
                throw new Exception("Namespace is not running");
            }
            // create files in PM storages
            boolean fileCreatedPMIC1 = Utils.createFileInPMStorage(FileAccessNbiConstants.PMIC1_FULL_PATH, FileAccessNbiConstants.PMIC1_FILE);
            boolean fileCreatedPMIC2 = Utils.createFileInPMStorage(FileAccessNbiConstants.PMIC2_FULL_PATH, FileAccessNbiConstants.PMIC2_FILE);
            boolean fileCreatedPMUL = Utils.createFileInPMStorage(FileAccessNbiConstants.PMUL_FULL_PATH, FileAccessNbiConstants.PMUL_FILE);

            if (!fileCreatedPMIC1 || !fileCreatedPMIC2 || !fileCreatedPMUL) {
                throw new Exception("Failed to create test files in the PM Storage. Please check log for more info.");
            }
            fileAccessNbiScenarioUtils.createShareDataSources();
            fileAccessNbiScenarioUtils.cleanupUsers();
            fileAccessNbiScenarioUtils.createUsers();
        } catch (Exception e) {
            Utils.LOGGER.error("Failed to execute preconditions: " , e);
            throw e;
        }
    }
}
