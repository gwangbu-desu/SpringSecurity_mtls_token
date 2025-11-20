package com.example.demo.service;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMKeyPair;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.pkcs.PKCS10CertificationRequest;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.math.BigInteger;
import java.security.KeyPair;
import java.security.PublicKey;
import java.security.Security;
import java.security.cert.X509Certificate;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Slf4j
@Service
public class CertificateService {

    private KeyPair caKeyPair;
    private X509Certificate caCertificate;

    @PostConstruct
    private void init() throws Exception {
        Security.addProvider(new BouncyCastleProvider());

        // Load CA private key from ca-key.pem
        try (InputStream is = getClass().getClassLoader().getResourceAsStream("ca-key.pem");
             PEMParser pemParser = new PEMParser(new InputStreamReader(is))) {
            Object object = pemParser.readObject();
            if (object instanceof PEMKeyPair) {
                PEMKeyPair pemKeyPair = (PEMKeyPair) object;
                JcaPEMKeyConverter converter = new JcaPEMKeyConverter().setProvider("BC");
                this.caKeyPair = converter.getKeyPair(pemKeyPair);
            } else {
                throw new IllegalStateException("ca-key.pem does not contain a valid PEMKeyPair.");
            }
        }

        // Load CA certificate from ca-cert.pem
        try (InputStream is = getClass().getClassLoader().getResourceAsStream("ca-cert.pem");
             PEMParser pemParser = new PEMParser(new InputStreamReader(is))) {
            Object object = pemParser.readObject();

            // Debugging log to show the actual class of the parsed object
            if (object != null) {
                log.debug("Parsed object from ca-cert.pem is of type: {}", object.getClass().getName());
            }

            // Use the general X509CertificateHolder for both check and cast
            if (object instanceof X509CertificateHolder) {
                this.caCertificate = new JcaX509CertificateConverter()
                        .setProvider("BC")
                        .getCertificate((X509CertificateHolder) object);
            } else {
                throw new IllegalStateException("ca-cert.pem does not contain a valid X509Certificate. Found: " + (object != null ? object.getClass().getName() : "null"));
            }
        }

        log.info("CA certificate and key loaded from PEM files.");
    }

    public String signCsr(String csrString, String expectedDeviceCn) throws Exception {
        PKCS10CertificationRequest csr = parseCsr(csrString);

        // 1. Validate CSR
        if (!verifyCsr(csr, expectedDeviceCn)) {
            throw new SecurityException("CSR validation failed. CN does not match expected device or signature is invalid.");
        }

        // 2. Create and sign the new certificate
        X509Certificate signedCertificate = createSignedCertificate(csr);

        // 3. Convert to PEM format
        return convertToPem(signedCertificate);
    }

    // 전달받은 String 형태의 CSR을 CSR객체로 변환.
    private PKCS10CertificationRequest parseCsr(String csrString) throws Exception {
        try (PEMParser pemParser = new PEMParser(new StringReader(csrString))) {
            Object parsedObj = pemParser.readObject();
            if (parsedObj instanceof PKCS10CertificationRequest) {
                return (PKCS10CertificationRequest) parsedObj;
            }
            throw new IllegalArgumentException("Provided string is not a valid PEM-encoded CSR.");
        }
    }

    private boolean verifyCsr(PKCS10CertificationRequest csr, String expectedDeviceCn) throws Exception {
        // Subject는 발급할 cert의 주체. 서버에서 VEHICLE-1234 에게 인증서를 발급한다. 이때, VEHICLE-1234가 주체임
        X500Name subject = csr.getSubject();
        String cn = subject.getRDNs(new org.bouncycastle.asn1.ASN1ObjectIdentifier("2.5.4.3"))[0].getFirst().getValue().toString();

        return cn.equals(expectedDeviceCn);
    }

    private X509Certificate createSignedCertificate(PKCS10CertificationRequest csr) throws Exception {
        X500Name subject = csr.getSubject();
        // BC = BouncyCastle, Cert의 확장 기능을 사용하기 위해 필요함. JSA = Java Security Auth (여기는 ECC안됨)
        PublicKey clientPublicKey = new JcaPEMKeyConverter().setProvider("BC").getPublicKey(csr.getSubjectPublicKeyInfo());

        X509v3CertificateBuilder certificateBuilder = new X509v3CertificateBuilder(
                new X500Name(caCertificate.getSubjectX500Principal().getName()),
                BigInteger.valueOf(System.currentTimeMillis()), // Unique serial number
                Date.from(Instant.now()),
                Date.from(Instant.now().plus(365, ChronoUnit.DAYS)), // 1 year validity
                subject,
                SubjectPublicKeyInfo.getInstance(clientPublicKey.getEncoded())
        );

        ContentSigner signer = new JcaContentSignerBuilder("SHA256WithECDSA").build(caKeyPair.getPrivate());

        return new JcaX509CertificateConverter().getCertificate(certificateBuilder.build(signer));
    }

    private String convertToPem(X509Certificate certificate) throws Exception {
        java.io.StringWriter sw = new java.io.StringWriter();
        try (org.bouncycastle.util.io.pem.PemWriter pemWriter = new org.bouncycastle.util.io.pem.PemWriter(sw)) {
            pemWriter.writeObject(new org.bouncycastle.util.io.pem.PemObject("CERTIFICATE", certificate.getEncoded()));
        }
        return sw.toString();
    }
}
