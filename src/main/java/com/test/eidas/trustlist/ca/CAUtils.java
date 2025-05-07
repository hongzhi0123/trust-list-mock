package com.test.eidas.trustlist.ca;

import java.io.FileOutputStream;
import java.io.FileWriter;
import java.math.BigInteger;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.SecureRandom;
import java.security.Security;
import java.security.cert.X509Certificate;
import java.util.Date;

import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x509.AccessDescription;
import org.bouncycastle.asn1.x509.AuthorityInformationAccess;
import org.bouncycastle.asn1.x509.BasicConstraints;
import org.bouncycastle.asn1.x509.CRLDistPoint;
import org.bouncycastle.asn1.x509.DistributionPoint;
import org.bouncycastle.asn1.x509.DistributionPointName;
import org.bouncycastle.asn1.x509.ExtendedKeyUsage;
import org.bouncycastle.asn1.x509.Extension;
import org.bouncycastle.asn1.x509.GeneralName;
import org.bouncycastle.asn1.x509.GeneralNames;
import org.bouncycastle.asn1.x509.KeyPurposeId;
import org.bouncycastle.asn1.x509.KeyUsage;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509CertificateHolder;
import org.bouncycastle.cert.jcajce.JcaX509ExtensionUtils;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.jcajce.JcaPEMWriter;
import org.bouncycastle.openssl.jcajce.JcaPKCS8Generator;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;

public class CAUtils {
    static long oneYear = 365 * 24 * 60 * 60 * 1000L; // One year in milliseconds
    static int serialNumberLength = 160; // Length of the serial number in bits
    static String signatureAlgorithm = "SHA512WithRSA"; // Signature algorithm

    static {
        // Add Bouncy Castle as a security provider
        Security.addProvider(new BouncyCastleProvider());
    }

    public static KeyPair generateKeyPair() throws Exception {
        var keyPairGenerator = KeyPairGenerator.getInstance("RSA", "BC");
        keyPairGenerator.initialize(4096);
        return keyPairGenerator.generateKeyPair();
    }

    public static X509Certificate generateSigningCA(X500Name subject, KeyPair keyPair) throws Exception {
        var issuer = subject;
        var serial = new BigInteger(serialNumberLength, new SecureRandom()); // Generate a 64-bit random serial number
        var notBefore = new Date();
        var notAfter = new Date(notBefore.getTime() + 15 * oneYear); // 15 years validity
        var certBuilder = new JcaX509v3CertificateBuilder(
                issuer,
                serial,
                notBefore,
                notAfter,
                subject,
                keyPair.getPublic());

        // Basic Constraints: CA = false
        certBuilder.addExtension(Extension.basicConstraints, true, new BasicConstraints(false));
        // Key Usage: KeyCertSign
        certBuilder.addExtension(Extension.keyUsage, true, new KeyUsage(KeyUsage.nonRepudiation));
        // Generate the Subject Key Identifier
        var extensionUtils = new JcaX509ExtensionUtils();
        certBuilder.addExtension(
                Extension.subjectKeyIdentifier,
                false, // Not critical
                extensionUtils.createSubjectKeyIdentifier(keyPair.getPublic()));
        // Add Extended Key Usage (EKU) extension with OID 0.4.0.2231.3.0
        ASN1ObjectIdentifier customOID = new ASN1ObjectIdentifier("0.4.0.2231.3.0");
        certBuilder.addExtension(
                Extension.extendedKeyUsage,
                false,
                new ExtendedKeyUsage(KeyPurposeId.getInstance(customOID)));

        var signer = new JcaContentSignerBuilder(signatureAlgorithm).build(keyPair.getPrivate());
        var certHolder = certBuilder.build(signer);
        return new JcaX509CertificateConverter().getCertificate(certHolder);
    }    

    public static X509Certificate generateRootCA(X500Name subject, KeyPair keyPair) throws Exception {
        var issuer = subject;
        var serial = new BigInteger(serialNumberLength, new SecureRandom()); // Generate a 64-bit random serial number
        var notBefore = new Date();
        var notAfter = new Date(notBefore.getTime() + 15 * oneYear); // 15 years validity
        var certBuilder = new JcaX509v3CertificateBuilder(
                issuer,
                serial,
                notBefore,
                notAfter,
                subject,
                keyPair.getPublic());

        // Basic Constraints: CA = true
        certBuilder.addExtension(Extension.basicConstraints, true, new BasicConstraints(true));
        // Key Usage: KeyCertSign
        certBuilder.addExtension(Extension.keyUsage, true, new KeyUsage(KeyUsage.keyCertSign | KeyUsage.cRLSign));
        // Generate the Subject Key Identifier
        var extensionUtils = new JcaX509ExtensionUtils();
        certBuilder.addExtension(
                Extension.subjectKeyIdentifier,
                false, // Not critical
                extensionUtils.createSubjectKeyIdentifier(keyPair.getPublic()));

        var signer = new JcaContentSignerBuilder(signatureAlgorithm).build(keyPair.getPrivate());
        var certHolder = certBuilder.build(signer);
        return new JcaX509CertificateConverter().getCertificate(certHolder);
    }

    public static X509Certificate generateIntermediateCA(X500Name subject, KeyPair intermediateKeyPair, KeyPair rootKeyPair,
            X509Certificate rootCert) throws Exception {
        var issuer = new JcaX509CertificateHolder(rootCert).getSubject();
        // var issuer = new X500Name(rootCert.getSubjectX500Principal().getName("CANONICAL")); // Use the issuer from the root certificate
        var serial = new BigInteger(serialNumberLength, new SecureRandom()); // Generate a 64-bit random serial number
        var notBefore = new Date();
        var notAfter = new Date(notBefore.getTime() + 10 * oneYear); // 10 years validity
        var certBuilder = new JcaX509v3CertificateBuilder(
                issuer,
                serial,
                notBefore,
                notAfter,
                subject,
                intermediateKeyPair.getPublic());

        // Basic Constraints: CA = true, pathLen = 0
        certBuilder.addExtension(Extension.basicConstraints, true, new BasicConstraints(0));
        // Key Usage: KeyCertSign
        certBuilder.addExtension(Extension.keyUsage, true, new KeyUsage(KeyUsage.keyCertSign | KeyUsage.cRLSign));
        // Generate the Authority Key Identifier
        var extensionUtils = new JcaX509ExtensionUtils();
        certBuilder.addExtension(
                Extension.authorityKeyIdentifier,
                false,
                extensionUtils.createAuthorityKeyIdentifier(rootCert.getPublicKey()));
        // Generate the Subject Key Identifier
        certBuilder.addExtension(
                Extension.subjectKeyIdentifier,
                false, // Not critical
                extensionUtils.createSubjectKeyIdentifier(intermediateKeyPair.getPublic()));
        // Extend Key Usage: TLS Web Server Authentication, TLS Web Client
        // Authentication
        certBuilder.addExtension(
                Extension.extendedKeyUsage,
                false,
                new ExtendedKeyUsage(new KeyPurposeId[] {
                        KeyPurposeId.id_kp_serverAuth,
                        KeyPurposeId.id_kp_clientAuth
                }));
        // Add Authority Information Access (AIA) with CA Issuers
        certBuilder.addExtension(
                Extension.authorityInfoAccess,
                false, // Not critical
                new AuthorityInformationAccess(
                        AccessDescription.id_ad_caIssuers, // CA Issuers OID
                        new GeneralName(GeneralName.uniformResourceIdentifier, "http://example.com/ca.crt") // URL to CA
                                                                                                            // certificate
                ));
        // Add CRL Distribution Points
        certBuilder.addExtension(
                Extension.cRLDistributionPoints,
                false, // Not critical
                new CRLDistPoint(new DistributionPoint[] {
                        new DistributionPoint(
                                new DistributionPointName(
                                        new GeneralNames(
                                                new GeneralName(GeneralName.uniformResourceIdentifier,
                                                        "http://example.com/crl.crl") // URL to CRL
                                        )),
                                null, // No specific reasons
                                null // No specific CRL issuer
                        )
                }));

        var signer = new JcaContentSignerBuilder(signatureAlgorithm).build(rootKeyPair.getPrivate());
        var certHolder = certBuilder.build(signer);
        return new JcaX509CertificateConverter().getCertificate(certHolder);
    }

    public static void saveCertificateToDerFile(String filename, byte[] data) throws Exception {
        try (var fos = new FileOutputStream(filename)) {
            fos.write(data);
        }
    }

    public static void saveCertificateToPemFile(String filename, X509Certificate certificate) throws Exception {
        try (var pemWriter = new JcaPEMWriter(new FileWriter(filename))) {
            pemWriter.writeObject(certificate);
        }
    }

    public static void savePrivateKeyToPemFile(String filename, PrivateKey privateKey) throws Exception {
        try (JcaPEMWriter pemWriter = new JcaPEMWriter(new FileWriter(filename))) {
            // JcaPEMWriter will write the rsa private key in PKCS#1 format by default
            // For writing the rsa private key in PKCS#8 format, JcaPKCS8Generator should be used
            pemWriter.writeObject(new JcaPKCS8Generator(privateKey, null));
        }
    }    
}
