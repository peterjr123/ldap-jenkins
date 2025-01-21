package org.example;

import com.unboundid.ldap.sdk.*;
import com.unboundid.util.ssl.AggregateTrustManager;
import com.unboundid.util.ssl.JVMDefaultTrustManager;
import com.unboundid.util.ssl.SSLUtil;
import com.unboundid.util.ssl.TrustStoreTrustManager;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.net.ssl.SSLHandshakeException;
import java.io.File;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@SpringBootApplication
@RequestMapping("/rest")
public class Main {
    SSLUtil sslUtil;

    public Main() {
        String trustStorePath = "./cert/truststore";
        File truststore = new File(trustStorePath);

        if(truststore.exists()) {
            System.out.println("exist!");
        }
        String trustStorePIN = "storepeterjr123!";
        AggregateTrustManager trustManager = new AggregateTrustManager(
                false,
                new TrustStoreTrustManager(truststore, trustStorePIN.toCharArray(), "PKCS12", true));
        sslUtil = new SSLUtil(trustManager);
    }

    @GetMapping("/search")
    public List<SearchResultEntry> getUser(@RequestParam(value = "name") String name) {
        List<SearchResultEntry> response;

        try(LDAPConnection connection = new LDAPConnection(sslUtil.createSSLSocketFactory(), "192.168.3.188", 11636)) {

            String bindDN = "uid=dsmuser01,ou=Users,o=dsmcorps";
            String password = "dms1234";
            SimpleBindRequest bindRequest = new SimpleBindRequest(bindDN, password);
            BindResult result  = connection.bind(bindRequest);
            System.out.println(result);


            String baseDN = "ou=Users,o=dsmcorps";
            Filter filter = Filter.createEqualityFilter("sn", "france");
            SearchRequest searchRequest = new SearchRequest(baseDN, SearchScope.SUB, filter);

            SearchResult searchResult = connection.search(searchRequest);
            System.out.println(searchResult);

            System.out.println(searchResult.getSearchEntries());
            response = searchResult.getSearchEntries();
        }
        catch (LDAPException | GeneralSecurityException e) {
            throw new RuntimeException(e);
        }

        return response;
    }

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
}