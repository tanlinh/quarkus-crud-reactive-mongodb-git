package security;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;


@Slf4j
@RequestScoped
public class PasswordEncode {

    @Inject
    @ConfigProperty(name = "quarkusjwt.password.iteration")
    Integer iteration;
    @Inject
    @ConfigProperty(name = "quarkusjwt.password.secret")
    String secret;
    @Inject
    @ConfigProperty(name = "quarkusjwt.password.keylength")
    Integer keylength;
    @Inject
    @ConfigProperty(name = "quarkusjwt.password.keyFactory")
    String keyFactory;

    public String encode(CharSequence cs) {
        try {
            byte[] result = SecretKeyFactory.getInstance(keyFactory)
                    .generateSecret(new PBEKeySpec(cs.toString().toCharArray(), secret.getBytes(), iteration, keylength))
                    .getEncoded();
              return Base64.getEncoder().encodeToString(result);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException ex) {
            throw new RuntimeException(ex);
        }
    }
}
