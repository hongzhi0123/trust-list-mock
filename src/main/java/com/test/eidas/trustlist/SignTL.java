package com.test.eidas.trustlist;

import java.io.File;
import java.security.KeyStore.PasswordProtection;

import eu.europa.esig.dss.model.DSSDocument;
import eu.europa.esig.dss.model.FileDocument;
import eu.europa.esig.dss.model.SignatureValue;
import eu.europa.esig.dss.model.ToBeSigned;
import eu.europa.esig.dss.spi.validation.CommonCertificateVerifier;
import eu.europa.esig.dss.token.DSSPrivateKeyEntry;
import eu.europa.esig.dss.token.Pkcs12SignatureToken;
import eu.europa.esig.dss.xades.XAdESSignatureParameters;
import eu.europa.esig.dss.xades.signature.XAdESService;
import eu.europa.esig.dss.xades.tsl.TrustedListV5SignatureParametersBuilder;

public class SignTL {

    public static void main(String[] args) throws Exception {

        final FileDocument document = new FileDocument("LOTL-2_unsigned.xml");

        try (Pkcs12SignatureToken token = new Pkcs12SignatureToken(new File("LOTL-2.p12"), new PasswordProtection("changeit".toCharArray()))) {
            final DSSPrivateKeyEntry key = token.getKey("lotl-2");

            final XAdESService service = new XAdESService(new CommonCertificateVerifier());
    
            final var paramsBuilder = new TrustedListV5SignatureParametersBuilder(key.getCertificate(), document);
            final XAdESSignatureParameters parameters = paramsBuilder.build();
    
            final ToBeSigned dataToSign = service.getDataToSign(document, parameters);
            final SignatureValue signatureValue = token.sign(dataToSign, parameters.getDigestAlgorithm(), key);
            final DSSDocument signDocument = service.signDocument(document, parameters, signatureValue);
            signDocument.save("LOTL-2_signed.xml");
        }
    }
}