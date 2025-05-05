package coba.api.psd2.signapp;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyFactory;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.ArrayList;
import java.util.List;

import eu.europa.esig.dss.enumerations.DigestAlgorithm;
import eu.europa.esig.dss.enumerations.SignaturePackaging;
import eu.europa.esig.dss.model.FileDocument;
import eu.europa.esig.dss.spi.validation.CommonCertificateVerifier;
import eu.europa.esig.dss.token.Pkcs12SignatureToken;
import eu.europa.esig.dss.utils.Utils;
import eu.europa.esig.dss.xades.XAdESSignatureParameters;
import eu.europa.esig.dss.xades.signature.XAdESService;

public class SignDocument {

    public static void sign(String docPath, String outputPath, String certPath, String privateKeyPath, String keyPassword) throws Exception {
        // Initialize BouncyCastle provider
        // Load the document to sign
        var toSignDocument = new FileDocument(new File(docPath));

        var p12Input = getKSInputStream(certPath, privateKeyPath, keyPassword);

        try (var token = new Pkcs12SignatureToken(p12Input, new KeyStore.PasswordProtection(keyPassword.toCharArray()))) {
            // Get private key entry
            var keys = token.getKeys();
            var signerKey = keys.get(0);

            // Configure signature parameters
            var parameters = new XAdESSignatureParameters();
            parameters.setDigestAlgorithm(DigestAlgorithm.SHA256);
            parameters.setSignaturePackaging(SignaturePackaging.ENVELOPED);
            parameters.setSigningCertificate(signerKey.getCertificate());
            parameters.setCertificateChain(signerKey.getCertificateChain());

            // Initialize the XAdESService
            var service = new XAdESService(new CommonCertificateVerifier());

            // Get data to sign
            var dataToSign = service.getDataToSign(toSignDocument, parameters);

            // Sign the data
            var signatureValue = token.sign(dataToSign, parameters.getDigestAlgorithm(), signerKey);

            // Complete the signature
            var signedDocument = service.signDocument(toSignDocument, parameters, signatureValue);

            // Save the signed document
            signedDocument.save(outputPath);
        }

    }

    private static InputStream getKSInputStream(String certPath, String privateKeyPath, String keyPassword) throws Exception {

        // Load private key and certificate
        var privateKey = loadPrivateKeyFromPEM(privateKeyPath, keyPassword);
        var certificate = loadCertificateFromPEM(certPath);
        List<Certificate> certChain = new ArrayList<>();
        certChain.add(certificate);

        // Create an in-memory KeyStore for DSS
        var keyStore = KeyStore.getInstance("PKCS12");
        keyStore.load(null, null); // Initialize empty KeyStore
        keyStore.setKeyEntry("signer", privateKey, keyPassword.toCharArray(), certChain.toArray(new Certificate[0]));

        // Initialize Pkcs12SignatureToken with in-memory KeyStore
        var p12Output = new ByteArrayOutputStream();
        keyStore.store(p12Output, keyPassword.toCharArray());
        var p12Input = new ByteArrayInputStream(p12Output.toByteArray());        

        return p12Input;
    }

    // Load private key from PEM file
    private static PrivateKey loadPrivateKeyFromPEM(String pemFilePath, String password) throws Exception {
        var pemContent = readFile(pemFilePath);
        pemContent = pemContent.replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replace("-----BEGIN RSA PRIVATE KEY-----", "")
                .replace("-----END RSA PRIVATE KEY-----", "")
                .replaceAll("\\s", "");

        var keyBytes = Utils.fromBase64(pemContent);
        var spec = new PKCS8EncodedKeySpec(keyBytes);
        var kf = KeyFactory.getInstance("RSA");
        return kf.generatePrivate(spec);
    }

    // Load certificate from PEM file
    private static X509Certificate loadCertificateFromPEM(String pemFilePath) throws Exception {
        var fis = new FileInputStream(pemFilePath);
        var cf = CertificateFactory.getInstance("X.509");
        return (X509Certificate) cf.generateCertificate(fis);
    }

    // Utility to read file content
    private static String readFile(String filePath) throws IOException {
        var content = new StringBuilder();
        try (var reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
        }
        return content.toString();
    }
}
