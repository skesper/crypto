import de.kesper.crypto.engine.CryptFactory;
import de.kesper.crypto.engine.model.KeyChain;
import de.kesper.crypto.engine.model.KeyChainEntry;

import javax.xml.bind.JAXB;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.util.UUID;

/**
 * User: kesper
 * Date: 18.06.13
 * Time: 11:09
 */
public class Serialtest {

    public static void main(String[] args) throws Exception {

        KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
        kpg.initialize(1024);
        KeyPair pair = kpg.generateKeyPair();

        KeyChainEntry e = new KeyChainEntry();

        e.setId(UUID.randomUUID().toString());
        e.setLabel("Mathilda Swinton");
        e.setEncodedPublicKey(pair.getPublic().getEncoded());

        KeyChain chain = new KeyChain();
        chain.getEntries().add(e);
        chain.setEncodedPublicKey(pair.getPublic().getEncoded());
        chain.setEncodedPrivateKey(pair.getPrivate().getEncoded());
        chain.setName("Stephan Kesper");
        chain.setUuid(UUID.randomUUID().toString());

        JAXB.marshal(chain, System.out);

        CryptFactory cf = CryptFactory.newInstance();
        System.out.println(cf.decodePrivateKey(chain.getEncodedPrivateKey()));
        System.out.println(cf.decodePublicKey(chain.getEncodedPublicKey()));
    }
}
