package de.kesper.crypto.engine.model;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.security.PublicKey;

/**
 * User: kesper
 * Date: 18.06.13
 * Time: 10:58
 */
@XmlRootElement(name = "key-info")
public class KeyChainEntry {
    private String id;
    private String label;
    private byte[] encodedPublicKey;

    @XmlAttribute
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @XmlAttribute
    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    @XmlElement(name = "public-key")
    public byte[] getEncodedPublicKey() {
        return encodedPublicKey;
    }

    public void setEncodedPublicKey(byte[] encodedPublicKey) {
        this.encodedPublicKey = encodedPublicKey;
    }

    @Override
    public String toString() {
        return label;
    }
}
