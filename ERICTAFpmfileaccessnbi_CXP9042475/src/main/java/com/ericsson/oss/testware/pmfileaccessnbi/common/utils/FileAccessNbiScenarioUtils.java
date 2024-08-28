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

package com.ericsson.oss.testware.pmfileaccessnbi.common.utils;

import static com.ericsson.cifwk.taf.scenario.TestScenarios.runner;
import static com.ericsson.cifwk.taf.scenario.TestScenarios.scenario;
import static com.ericsson.cifwk.taf.scenario.api.ScenarioExceptionHandler.LOGONLY;
import static com.ericsson.oss.testware.pmfileaccessnbi.common.constants.FileAccessNbiConstants.*;

import com.ericsson.cifwk.taf.TafTestBase;
import com.ericsson.cifwk.taf.scenario.api.ScenarioExceptionHandler;
import com.ericsson.oss.testware.enmbase.data.CommonDataSources;
import com.ericsson.oss.testware.enmbase.data.ENMUser;
import com.ericsson.oss.testware.security.gim.flows.GimCleanupFlows;
import com.google.inject.Inject;
import static com.ericsson.oss.testware.security.gim.flows.GimCleanupFlows.EnmObjectType.USER;

import com.ericsson.cifwk.taf.scenario.TestScenario;
import com.ericsson.cifwk.taf.scenario.TestScenarioRunner;
import com.ericsson.cifwk.taf.scenario.impl.LoggingScenarioListener;
import com.ericsson.oss.testware.security.gim.flows.UserManagementTestFlows;

/**
 * Utility used in testing for TAF.
 */
public class FileAccessNbiScenarioUtils extends TafTestBase {

    @Inject private UserManagementTestFlows userManagementFlows;
    @Inject private GimCleanupFlows gimCleanupFlows;

    public FileAccessNbiScenarioUtils() {
    }

    /**
     * Execute Scenario and specify handler
     * @param scenario
     *          Name of the scenario
     * @param handler
     *          Type of logging
     */
    private void executeScenario(final TestScenario scenario, final ScenarioExceptionHandler handler) {
        final TestScenarioRunner runner = runner().withListener(new LoggingScenarioListener()).withDefaultExceptionHandler(handler).build();
        runner.start(scenario);
    }

    /**
     * Share datasource with one CSV - usersToCreate.csv
     */
    public void createShareDataSources() {
        DataSourceHelper.initializeAndCreateSharedDataSourceFromCsv(CommonDataSources.USER_TO_CLEAN_UP, USER_TO_CREATE, ENMUser.class);
        DataSourceHelper.initializeAndCreateSharedDataSourceFromCsv(CommonDataSources.USERS_TO_DELETE, USER_TO_CREATE, ENMUser.class);
    }

    /**
     * Create users given the dataSource
     */
    public void createUsers() {
        if (TRUE.equals(CREATE_USERS)) {
            final TestScenario scenario = scenario("Preconditions create users")
                    .addFlow(userManagementFlows.createUser())
                    .build();
            executeScenario(scenario, LOGONLY);
        }
    }

    /**
     * Remove users given the dataSource
     */
    public void removeUsers() {
        if (TRUE.equals(REMOVE_USERS)) {
            final TestScenario scenario = scenario("Remove users")
                    .addFlow(userManagementFlows.deleteUser())
                    .build();
            executeScenario(scenario, LOGONLY);
        }
    }

    /**
     * Clean up users given the dataSource
     */
    public void cleanupUsers() {
        final TestScenario scenario = scenario("Deleting Setup Users if any").addFlow(gimCleanupFlows.cleanUp(USER)).build();
        executeScenario(scenario, LOGONLY);
    }
}
