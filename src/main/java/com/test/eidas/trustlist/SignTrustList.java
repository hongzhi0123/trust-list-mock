package com.test.eidas.trustlist;

import java.io.File;
import java.nio.file.Paths;
import java.util.Arrays;

import com.test.eidas.trustlist.lotl.SignDocument;

public class SignTrustList {
    private final static String basePath = new File("test_trust_list").getAbsolutePath();

    public static void main(String[] args) throws Exception {

        for (String docName : Arrays.asList("lotl-test", "LOTL-1", "LOTL-2", "LOTL-3", "LOTL-4")) {
            SignDocument.sign(
                Paths.get(basePath, docName + "_unsigned.xml").toString(),
                Paths.get(basePath, docName + "_signed.xml").toString(),
                Paths.get(basePath, "TSP-211", "certs", "TSP-1.pem").toString(),
                Paths.get(basePath, "TSP-211", "private", "TPP-1.key").toString(),
                ""
            );
        }
    }
}
