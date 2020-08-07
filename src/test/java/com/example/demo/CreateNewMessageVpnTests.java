package com.example.demo;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestClientException;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import io.swagger.client.ApiClient;
import io.swagger.client.api.MsgVpnApi;
import io.swagger.client.model.MsgVpn;
import io.swagger.client.model.MsgVpn.AuthenticationBasicTypeEnum;
import io.swagger.client.model.MsgVpnResponse;

@SpringBootTest
@Testcontainers
class CreateNewMessageVpnTests {
    @SuppressWarnings("rawtypes")
    @Container
    public GenericContainer solace = new GenericContainer("solace/solace-pubsub-standard")
            .withEnv("username_admin_globalaccesslevel", "admin").withEnv("username_admin_password", "admin")
            .withSharedMemorySize(2L * 1024L * 1024L * 1024L).withExposedPorts(55555, 8080);

    MsgVpnApi sempApiInstance;

    public void initialize(String basePath, String user, String password) throws Exception {

        System.out.format("SEMP initializing: %s, %s \n", basePath, user);

        ApiClient thisClient = new ApiClient();
        thisClient.setBasePath(basePath);
        thisClient.setUsername(user);
        thisClient.setPassword(password);
        sempApiInstance = new MsgVpnApi(thisClient);
    }

    public MsgVpnResponse createMessageVpn(String messageVpnName) throws RestClientException {

        System.out.format("Creating Message-VPN: %s...\n", messageVpnName);

        // Create message-vpn
        MsgVpn msgVpn = new MsgVpn();
        msgVpn.setMsgVpnName(messageVpnName);
        msgVpn.setAuthenticationBasicType(AuthenticationBasicTypeEnum.INTERNAL);
        msgVpn.setMaxMsgSpoolUsage(1500L);
        msgVpn.setEnabled(true);
        msgVpn.setAlias("");
        msgVpn.authenticationBasicEnabled(false);
        msgVpn.authenticationBasicProfileName("");
        msgVpn.authenticationBasicRadiusDomain("");
        msgVpn.authenticationClientCertAllowApiProvidedUsernameEnabled(false);
        msgVpn.authenticationClientCertEnabled(false);

        MsgVpnResponse resp = sempApiInstance.createMsgVpn(msgVpn, null, null);

        return resp;
    }

    @Test
    void test() throws Exception {
        initialize(String.format("http://%s:%d/SEMP/v2/config", solace.getHost(), solace.getMappedPort(8080)), "admin",
                "admin");
        MsgVpnResponse msgVpnResponse = createMessageVpn("messageVpnName");
    }

}
