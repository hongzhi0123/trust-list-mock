package com.test.eidas.trustlist;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.security.KeyFactory;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.Security;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.ArrayList;
import java.util.List;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import eu.europa.esig.dss.enumerations.DigestAlgorithm;
import eu.europa.esig.dss.enumerations.SignatureLevel;
import eu.europa.esig.dss.enumerations.SignaturePackaging;
import eu.europa.esig.dss.model.DSSDocument;
import eu.europa.esig.dss.model.FileDocument;
import eu.europa.esig.dss.model.SignatureValue;
import eu.europa.esig.dss.model.ToBeSigned;
import eu.europa.esig.dss.pades.PAdESSignatureParameters;
import eu.europa.esig.dss.pades.signature.PAdESService;
import eu.europa.esig.dss.spi.validation.CommonCertificateVerifier;
import eu.europa.esig.dss.token.DSSPrivateKeyEntry;
import eu.europa.esig.dss.token.Pkcs12SignatureToken;
import eu.europa.esig.dss.utils.Utils;

public class DSSSignPDFWithPEM {
    public static void main(String[] args) throws Exception {
        // Initialize BouncyCastle provider
        Security.addProvider(new BouncyCastleProvider());

        // Paths to input files
        String pdfPath = "input.pdf";
        String privateKeyPath = "privatekey.pem";
        String certPath = "certificate.pem";
        String outputPath = "signed_output.pdf";
        String keyPassword = "your-key-password"; // Password for the private key, if encrypted

        // Load the document to sign
        FileDocument toSignDocument = new FileDocument(new File(pdfPath));

        // Load private key and certificate
        PrivateKey privateKey = loadPrivateKeyFromPEM(privateKeyPath, keyPassword);
        X509Certificate certificate = loadCertificateFromPEM(certPath);
        List<Certificate> certChain = new ArrayList<>();
        certChain.add(certificate);

        // Create an in-memory KeyStore for DSS
        KeyStore keyStore = KeyStore.getInstance("PKCS12");
        keyStore.load(null, null); // Initialize empty KeyStore
        keyStore.setKeyEntry("signer", privateKey, keyPassword.toCharArray(), certChain.toArray(new Certificate[0]));

        // Initialize Pkcs12SignatureToken with in-memory KeyStore
        ByteArrayOutputStream p12Output = new ByteArrayOutputStream();
        keyStore.store(p12Output, keyPassword.toCharArray());
        ByteArrayInputStream p12Input = new ByteArrayInputStream(p12Output.toByteArray());
        try (Pkcs12SignatureToken token = new Pkcs12SignatureToken(p12Input, new KeyStore.PasswordProtection(keyPassword.toCharArray()))) {
            // Get private key entry
            List<DSSPrivateKeyEntry> keys = token.getKeys();
            DSSPrivateKeyEntry signerKey = keys.get(0);

            // Configure signature parameters
            PAdESSignatureParameters parameters = new PAdESSignatureParameters();
            parameters.setSignatureLevel(SignatureLevel.PAdES_BASELINE_B);
            parameters.setDigestAlgorithm(DigestAlgorithm.SHA256);
            parameters.setSignaturePackaging(SignaturePackaging.ENVELOPED);
            parameters.setSigningCertificate(signerKey.getCertificate());
            parameters.setCertificateChain(signerKey.getCertificateChain());

            // Initialize certificate verifier
            CommonCertificateVerifier verifier = new CommonCertificateVerifier();

            // Initialize PAdES service
            PAdESService service = new PAdESService(verifier);

            // Get data to sign
            ToBeSigned dataToSign = service.getDataToSign(toSignDocument, parameters);

            // Sign the data
            SignatureValue signatureValue = token.sign(dataToSign, parameters.getDigestAlgorithm(), signerKey);

            // Complete the signature
            DSSDocument signedDocument = service.signDocument(toSignDocument, parameters, signatureValue);

            // Save the signed document
            signedDocument.save(outputPath);
        }

        System.out.println("Signed PDF saved to: " + outputPath);
    }

    // Load private key from PEM file
    private static PrivateKey loadPrivateKeyFromPEM(String pemFilePath, String password) throws Exception {
        String pemContent = readFile(pemFilePath);
        pemContent = pemContent.replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replace("-----BEGIN RSA PRIVATE KEY-----", "")
                .replace("-----END RSA PRIVATE KEY-----", "")
                .replaceAll("\\s", "");

        byte[] keyBytes = Utils.fromBase64(pemContent);
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePrivate(spec);
    }

    // Load certificate from PEM file
    private static X509Certificate loadCertificateFromPEM(String pemFilePath) throws Exception {
        FileInputStream fis = new FileInputStream(pemFilePath);
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        return (X509Certificate) cf.generateCertificate(fis);
    }

    // Utility to read file content
    private static String readFile(String filePath) throws IOException {
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
        }
        return content.toString();
    }
}
