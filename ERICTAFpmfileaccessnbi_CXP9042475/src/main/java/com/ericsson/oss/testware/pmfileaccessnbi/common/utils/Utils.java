package com.ericsson.oss.testware.pmfileaccessnbi.common.utils;

import com.ericsson.cifwk.taf.data.Host;
import com.ericsson.oss.testware.hostconfigurator.HostConfigurator;
import com.ericsson.cifwk.taf.configuration.TafConfigurationProvider;
import com.ericsson.cifwk.taf.tools.cli.TafCliTools;
import com.ericsson.cifwk.taf.tools.cli.jsch.JSchSession;
import com.ericsson.de.tools.cli.CliCommandResult;
import com.ericsson.de.tools.cli.CliToolShell;
import com.ericsson.oss.testware.pmfileaccessnbi.cases.FileAccessNbiScenario;
import com.ericsson.oss.testware.pmfileaccessnbi.common.constants.FileAccessNbiConstants;
import com.ericsson.oss.testware.remoteexecution.operators.PemFileUtilities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

public class Utils {
    public static final Logger LOGGER = LoggerFactory.getLogger(FileAccessNbiScenario.class);
    public static Host host = HostConfigurator.getClientMachine();
    public static CliToolShell cliShell = getCliToolShell();

    /**
     * Get cli tool shell from the host
     *
     * @return
     *      CliToolShell: Return the CliToolShell retrieved from the host
     */
    public static CliToolShell getCliToolShell() {
        Path pemFile = Paths.get(getPemFilePath());
        TafConfigurationProvider.provide().setProperty(JSchSession.JSCH_SESSION_CONNECT_TIMEOUT_PROPERTY, 30);
        return ( Utils.host.getPass().isEmpty() || Utils.host.getPass().equalsIgnoreCase("dummy")) ?
                TafCliTools.sshShell(Utils.host).withKeyFile(pemFile).build()
                : TafCliTools.sshShell(Utils.host).build();
    }

    /**
     * Get the pemFilePath via PemFileUtilities
     *
     * @return
     *      String: The path to the pem file
     */
    public static String getPemFilePath() {
        final String hostName = PemFileUtilities.getHostnameOfDeployment();
        final String contents = PemFileUtilities.getPrivateKey(hostName);
        final File pemFile = PemFileUtilities.writePrivateKeyToFile(hostName, contents);
        Utils.LOGGER.info("Get PEM file path : {}", pemFile.toPath().toString());
        return pemFile.toPath().toString();
    }

    /**
     * Create the dump test file in the PM storage given the filePath and name
     *
     * @param pmFilePath
     *      The file path of file to be created
     *
     * @param fileName
     *      The name of the file to be created
     *
     * @return
     *      Boolean: The result of creating the file in PM storage
     */
    public static boolean createFileInPMStorage(String pmFilePath, String fileName) {
        Utils.LOGGER.info("Creating file in " + pmFilePath + fileName);
        String createFileCommand = "kubectl exec deploy/fileaccessnbi -c fileaccessnbi" + " -n " + Utils.host.getNamespace() +
                " -- sh -c 'echo " + FileAccessNbiConstants.TEST_STR + " > " + pmFilePath + fileName + "'";

        if (executeCommand(createFileCommand)) {
            Utils.LOGGER.info("File created: " + pmFilePath + fileName);
            return true;
        }
        Utils.LOGGER.error("Something went wrong, could not create a file: " + pmFilePath + fileName);
        return false;
    }

    /**
     * Remove the dump test file in the PM storage given the filePath and name
     *
     * @param pmFilePath
     *      The file path of file to be created
     *
     * @param fileName
     *      The name of the file to be created
     *
     * @return
     *      boolean: The result of removing the file in PM storage
     */
    public static boolean removeFileInPMStorage(String pmFilePath, String fileName) {
        Utils.LOGGER.info("Removing file : " + pmFilePath + fileName);
        String removeFileCommand = "kubectl exec deploy/fileaccessnbi -c fileaccessnbi" + " -n " + Utils.host.getNamespace() +
                " -- sh -c 'rm " + pmFilePath + fileName + "'";

        if (executeCommand(removeFileCommand)) {
            Utils.LOGGER.info("File removed: '" + pmFilePath + fileName);
            return true;
        }
        Utils.LOGGER.error("Something went wrong, could not delete a file:" + pmFilePath + fileName);
        return false;
    }

    /**
     * Executes given shell command
     *
     * @param cmd
     *      Command to be executed
     *
     * @return
     *      boolean: The command's success status
     */
    private static boolean executeCommand(String cmd){
        CliCommandResult result = Utils.cliShell.execute(cmd);
        Utils.LOGGER.info("command : {}", cmd);
        Utils.LOGGER.info("output : {}", result.getOutput());
        Utils.LOGGER.info("exitCode : {}", result.getExitCode());
        Utils.LOGGER.info("executionTime : {}", result.getExecutionTime());
        Utils.LOGGER.info("isSuccess : {}", result.isSuccess());
        return result.isSuccess();
    }

    // Utils for setup preconditions
    /**
     * Get and return the namespace from the host
     *
     * @return
     *      String: return the namespace of the host
     */
    public static String getNameSpace() {
        Utils.LOGGER.info("Checking for namespace...");
        String nameSpace = Utils.host.getNamespace();
        if(!Objects.equals(nameSpace, "")){
            Utils.LOGGER.info("Running in namespace: " + nameSpace);
            return nameSpace;
        }
        return null;
    }

    /**
     * Check if FAN pod is running given the namespace
     *
     * @param clusterNamespace
     *      The namespace of the cluster
     *
     * @return
     *      Boolean: return if FAN pod is running
     */
    public static boolean isFANPodRunning(String clusterNamespace) {
        Utils.LOGGER.info("Checking for running FAN pod...");
        String podExecCommand = "kubectl -n " + clusterNamespace + " exec deploy/fileaccessnbi -c fileaccessnbi -- bash";
        return executeCommand(podExecCommand);
    }
}
