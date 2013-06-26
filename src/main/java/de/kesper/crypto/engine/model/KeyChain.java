package de.kesper.crypto.engine.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;

/**
 * User: kesper
 * Date: 18.06.13
 * Time: 11:33
 */
@XmlRootElement(name = "key-chain")
public class KeyChain {
    private String uuid;
    private String name;
    private String email;
    private ArrayList<KeyChainEntry> entries = new ArrayList<KeyChainEntry>();
    private byte[] encodedPublicKey;
    private byte[] encodedPrivateKey;

    @XmlElementWrapper(name="entries")
    @XmlElement(name="entry")
    public ArrayList<KeyChainEntry> getEntries() {
        return entries;
    }

    public void setEntries(ArrayList<KeyChainEntry> entries) {
        this.entries = entries;
    }

    @XmlElement(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @XmlElement(name = "email-address")
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @XmlElement(name = "uuid")
    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    @XmlElement(name = "public-key")
    public byte[] getEncodedPublicKey() {
        return encodedPublicKey;
    }

    public void setEncodedPublicKey(byte[] encodedPublicKey) {
        this.encodedPublicKey = encodedPublicKey;
    }

    @XmlElement(name = "private-key")
    public byte[] getEncodedPrivateKey() {
        return encodedPrivateKey;
    }

    public void setEncodedPrivateKey(byte[] encodedPrivateKey) {
        this.encodedPrivateKey = encodedPrivateKey;
    }
}
