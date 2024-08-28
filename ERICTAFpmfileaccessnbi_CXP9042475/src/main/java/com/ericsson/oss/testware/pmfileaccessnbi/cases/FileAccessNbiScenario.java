package com.ericsson.oss.testware.pmfileaccessnbi.cases;

import static com.ericsson.cifwk.taf.scenario.TestScenarios.annotatedMethod;
import static com.ericsson.cifwk.taf.scenario.TestScenarios.dataDrivenScenario;
import static com.ericsson.cifwk.taf.scenario.TestScenarios.dataSource;
import static com.ericsson.cifwk.taf.scenario.TestScenarios.flow;
import static com.ericsson.cifwk.taf.scenario.TestScenarios.runner;
import static com.ericsson.oss.testware.pmfileaccessnbi.common.constants.FileAccessNbiConstants.*;

import javax.inject.Inject;

import com.ericsson.oss.testware.pmfileaccessnbi.common.utils.Utils;
import org.testng.annotations.Test;
import com.ericsson.cifwk.taf.TafTestBase;
import com.ericsson.cifwk.taf.annotations.TestId;
import com.ericsson.cifwk.taf.annotations.TestSuite;
import com.ericsson.cifwk.taf.scenario.TestScenario;
import com.ericsson.oss.testware.security.authentication.flows.LoginLogoutRestFlows;

public final class FileAccessNbiScenario extends TafTestBase  {

    @Inject
    private FileAccessNbiSteps fileAccessNbiSteps;
    @Inject
    private LoginLogoutRestFlows loginLogoutFlows;

    @Test(priority = 1, enabled = true, groups = {"RFA250"})
    @TestSuite
    @TestId(id = "TORF-574623-19", title = "Verify that a new user, configured with a Role compatible with the new Posix attribute, can access PM files.")
    public void checkFileAccessNbiAndDownloadFiles() {
        Utils.LOGGER.info("Running first test - Test authorized user download files successfully.");
        TestScenario scenario = dataDrivenScenario("Test authorized user download files successfully")
                .addFlow(loginLogoutFlows.login(USERNAME_FILTER))
                .addFlow(flow("Check PM file accessible by authorized user")
                        .addTestStep(annotatedMethod(fileAccessNbiSteps, FileAccessNbiSteps.StepIds.REQUEST_FILE_FROM_FILE_ACCESS_NBI))
                        .withDataSources(dataSource(FILE_FOR_AUTHORIZED_DS)))
                .withScenarioDataSources(dataSource(FAN_AUTHORIZED_DS))
                .addFlow(loginLogoutFlows.logout())
                .build();
        runner().build().start(scenario);
        Utils.LOGGER.info("First test completed.");
    }

    @Test(priority = 1, enabled = true, groups = {"RFA250"})
    @TestSuite
    @TestId(id = "TORF-574623-20", title = "Verify that a new user, configured without a Role compatible with the new Posix attribute, cannot access PM files.")
    public void checkFileAccessNbiWrongUser() {
        Utils.LOGGER.info("Running second test - Test unauthorized user download files failed");
        TestScenario scenario = dataDrivenScenario("Test unauthorized user download files unsuccessfully")
                .addFlow(loginLogoutFlows.loginWithUserName(UNAUTHORIZED_USER))
                .addFlow(flow("Check PM file accessible by unauthorized user")
                        .addTestStep(annotatedMethod(fileAccessNbiSteps, FileAccessNbiSteps.StepIds.REQUEST_FILE_FROM_FILE_ACCESS_NBI))
                        .withDataSources(dataSource(FILE_FOR_UNAUTHORIZED_DS)))
                .withScenarioDataSources(dataSource(FAN_UNAUTHORIZED_DS))
                .addFlow(loginLogoutFlows.logout())
                .build();
        runner().build().start(scenario);
        Utils.LOGGER.info("Second test completed.");
    }
}