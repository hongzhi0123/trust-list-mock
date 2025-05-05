package com.test.eidas.trustlist;

import static com.test.eidas.trustlist.ca.CAUtils.*;

import java.security.KeyPair;
import java.security.cert.X509Certificate;

import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.X500NameBuilder;
import org.bouncycastle.asn1.x500.style.BCStyle;

public class CreateCA {
    public static void main(String[] args) throws Exception {
        // Build the subject
        X500Name signingSubject = new X500NameBuilder(BCStyle.INSTANCE)
                .addRDN(BCStyle.C, "LU") // Country
                .addRDN(BCStyle.O, "EUROPEAN COMMISSION") // Organization
                .addRDN(BCStyle.OU, "Certificate Profile - Qualified Certificate - Organization") // Organizational Unit
                .addRDN(BCStyle.CN, "LOTL") // Common Name
                .build();

        // Generate Signing CA
        KeyPair signingKeyPair = generateKeyPair();
        X509Certificate signingCert = generateSigningCA(signingSubject, signingKeyPair);
        saveCertificateToDerFile("signingCA.crt", signingCert.getEncoded());
        saveCertificateToPemFile("signingCA.pem", signingCert);
        savePrivateKeyToPemFile("signingCA.key", signingKeyPair.getPrivate());

        // Build the subject
        X500Name rootSubject = new X500NameBuilder(BCStyle.INSTANCE)
                .addRDN(BCStyle.C, "DE") // Country
                .addRDN(BCStyle.O, "My Organization") // Organization
                .addRDN(BCStyle.OU, "My Organizational Unit") // Organizational Unit
                .addRDN(BCStyle.CN, "Root CA") // Common Name
                .build();

        // Generate Root CA
        KeyPair rootKeyPair = generateKeyPair();
        X509Certificate rootCert = generateRootCA(rootSubject, rootKeyPair);
        saveCertificateToDerFile("rootCA.crt", rootCert.getEncoded());
        saveCertificateToPemFile("rootCA.pem", rootCert);
        savePrivateKeyToPemFile("rootCA.key", rootKeyPair.getPrivate());

        // Build the subject
        X500Name intermediateSubject = new X500NameBuilder(BCStyle.INSTANCE)
                .addRDN(BCStyle.C, "DE") // Country
                .addRDN(BCStyle.O, "My Organization") // Organization
                .addRDN(BCStyle.OU, "My Organizational Unit") // Organizational Unit
                .addRDN(BCStyle.CN, "Intermediate CA") // Common Name
                .build();

        // Generate Intermediate CA
        KeyPair intermediateKeyPair = generateKeyPair();
        X509Certificate intermediateCert = generateIntermediateCA(
                intermediateSubject, intermediateKeyPair, rootKeyPair, rootCert);
        saveCertificateToDerFile("intermediateCA.crt", intermediateCert.getEncoded());
        saveCertificateToPemFile("intermediateCA.pem", intermediateCert);
        savePrivateKeyToPemFile("intermediateCA.key", intermediateKeyPair.getPrivate());
    }
}
