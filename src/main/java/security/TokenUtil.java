package security;

import dto.enumm.ERole;
import entity.User;
import io.smallrye.jwt.algorithm.SignatureAlgorithm;
import io.smallrye.jwt.build.Jwt;
import io.smallrye.jwt.build.JwtClaimsBuilder;
import io.smallrye.jwt.util.KeyUtils;

import java.io.InputStream;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.time.Duration;
import java.util.Base64;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class TokenUtil {
    public static String generateToken(String username, Set<ERole> roles) throws Exception {
        String privateKeyLocation = "/privatekey.pem";
        PrivateKey privateKey = readPrivateKey(privateKeyLocation);

        JwtClaimsBuilder claimsBuilder = Jwt.claims();
        long currentTimeInSecs = currentTimeInSecs();

        Set<String> groups = new HashSet<>();
        for (ERole role : roles) groups.add(role.toString());

        claimsBuilder.subject(username);
        claimsBuilder.issuedAt(currentTimeInSecs);
        claimsBuilder.groups(groups);

        return claimsBuilder.jws().signatureKeyId(privateKeyLocation).sign(privateKey);
    }

    public static String generateToken(User user) throws Exception {
        String privateKeyLocation = "/privatekey.pem";
        PrivateKey privateKey = readPrivateKey(privateKeyLocation);

        JwtClaimsBuilder claimsBuilder = Jwt.claims();
        long currentTimeInSecs = currentTimeInSecs();

        Set<String> groups = new HashSet<>();
        for (ERole role : user.getRoles()) groups.add(role.toString());

        claimsBuilder.subject(user.getUserName());
        claimsBuilder.issuedAt(currentTimeInSecs);
        claimsBuilder.groups(groups);

        String token = claimsBuilder.jws().signatureKeyId(privateKeyLocation).sign(privateKey);
        System.out.println("asdasdasdasdas         " +token);
        return token;
    }

    public static PrivateKey readPrivateKey(final String pemResName) throws Exception {
        try (InputStream contentIS = TokenUtil.class.getResourceAsStream(pemResName)) {
            byte[] tmp = new byte[4096];
            int length = contentIS.read(tmp);
            return decodePrivateKey(new String(tmp, 0, length, "UTF-8"));
        }
    }

    public static PrivateKey decodePrivateKey(final String pemEncoded) throws Exception {
        byte[] encodedBytes = toEncodedBytes(pemEncoded);

        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(encodedBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePrivate(keySpec);
    }

    public static byte[] toEncodedBytes(final String pemEncoded) {
        final String normalizedPem = removeBeginEnd(pemEncoded);
        return Base64.getDecoder().decode(normalizedPem);
    }

    public static String removeBeginEnd(String pem) {
        pem = pem.replaceAll("-----BEGIN (.*)-----", "");
        pem = pem.replaceAll("-----END (.*)----", "");
        pem = pem.replaceAll("\r\n", "");
        pem = pem.replaceAll("\n", "");
        return pem.trim();
    }

    public static int currentTimeInSecs() {
        long currentTimeMS = System.currentTimeMillis();
        return (int) (currentTimeMS / 1000);
    }



}
