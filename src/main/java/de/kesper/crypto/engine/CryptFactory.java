package de.kesper.crypto.engine;

import de.kesper.crypto.ApplicationContext;
import de.kesper.crypto.engine.model.KeyChain;
import de.kesper.crypto.engine.model.KeyChainEntry;
import de.kesper.crypto.gui.CryptoMainFrame;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.*;
import javax.xml.bind.JAXB;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

/**
 * User: kesper
 * Date: 18.06.13
 * Time: 10:58
 */
public class CryptFactory {

    public static final String ASYMMETRIC_ALGORITHM = "RSA";
    public static final String SYMMETRIC_ALGORITHM = "AES";
    public static final String DIGEST_ALGORITHM = "SHA-256";
    public static final String MESSAGE_ENCODING = "UTF-8";
    public static final int AES_KEY_LENGTH = 256;
    public static final int MAXIMUM_KEY_LENGTH = 8192;
    private volatile KeyChain keyChain;
    private volatile Path configDir;

    private CryptFactory() {}

    public static CryptFactory newInstance() {
        return new CryptFactory();
    }

    public void setKeyChain(KeyChain chain) {
        keyChain = chain;
    }

    public KeyChain getKeyChain() {
        return keyChain;
    }

    public void initialize(Path directory) throws Exception {
        configDir = directory;
        final Path keyChainStorage = directory.resolve(".keychain");
        if (Files.exists(keyChainStorage)) {
            keyChain = deserialize(keyChainStorage);
            ApplicationContext.getInstance().getMainFrame().setTitle("Crypto - "+keyChain.getName());
        } else {
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    CryptoMainFrame mainframe = ApplicationContext.getInstance().getMainFrame();
                    mainframe.setTextContent("\ncreating key pair, this may take several minutes. Please be patient!");
                    keyChain = createEmptyKeyChain();
                    mainframe.setTextContent("\nKey pair generated. Thanks for your patience.");
                    String name = JOptionPane.showInputDialog("Please enter your name");
                    keyChain.setName(name);
                    String email = JOptionPane.showInputDialog("Please enter your Email address");
                    keyChain.setEmail(email);
                    mainframe.setTextContent("");
                    try {
                        serialize(keyChain, keyChainStorage);
                    } catch(Exception e) {
                        e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                    }
                    ApplicationContext.getInstance().getMainFrame().setTitle("Crypto - "+keyChain.getName());

                }
            });
            t.start();

        }
    }

    public KeyChainEntry getMeAsEntry() {
        KeyChainEntry e = new KeyChainEntry();
        e.setId(keyChain.getUuid());
        e.setLabel(keyChain.getName()+" <"+keyChain.getEmail()+">");
        e.setEncodedPublicKey(keyChain.getEncodedPublicKey());
        return e;
    }

    public void store() throws Exception {
        Path keyChainStorage = configDir.resolve(".keychain");
        serialize(keyChain, keyChainStorage);
    }

    public String addKeyChainEntry(KeyChainEntry entry) {
        if (entry.getId().equals(keyChain.getUuid())) {
            return "This is your own key, you should not try to import it.";
        }
        for(KeyChainEntry e : keyChain.getEntries()) {
            if (e.getId().equals(entry.getId())) {
                return "This key has already been imported.";
            }
        }
        keyChain.getEntries().add(entry);
        return null;
    }

    private KeyChain createEmptyKeyChain() {
        KeyChain chain = new KeyChain();
        chain.setUuid(UUID.randomUUID().toString());
        KeyPairGenerator g;
        try {
            g = KeyPairGenerator.getInstance(ASYMMETRIC_ALGORITHM);
        } catch(NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        g.initialize(MAXIMUM_KEY_LENGTH);
        KeyPair pair = g.generateKeyPair();

        chain.setEncodedPrivateKey(pair.getPrivate().getEncoded());
        chain.setEncodedPublicKey(pair.getPublic().getEncoded());
        return chain;
    }

    public PrivateKey decodePrivateKey(byte[] encodedPrivateKey) throws NoSuchAlgorithmException, InvalidKeySpecException {
        return KeyFactory.getInstance(ASYMMETRIC_ALGORITHM).generatePrivate(new PKCS8EncodedKeySpec(encodedPrivateKey));
    }

    public PublicKey decodePublicKey(byte[] encodedPublicKey) throws NoSuchAlgorithmException, InvalidKeySpecException {
        return KeyFactory.getInstance(ASYMMETRIC_ALGORITHM).generatePublic(new X509EncodedKeySpec(encodedPublicKey));
    }

    private void serialize(KeyChain chain, Path toFile) throws Exception {
        try (OutputStream os = Files.newOutputStream(toFile)) {
            KeyChain chainCopy = new KeyChain();
            chainCopy.setEmail(chain.getEmail());
            chainCopy.setName(chain.getName());
            chainCopy.setEncodedPublicKey(chain.getEncodedPublicKey());
            byte[] encryptedPrivateKey = encryptAes(chain.getEncodedPrivateKey(), ApplicationContext.getInstance().getPasswordKey());
            chainCopy.setEncodedPrivateKey(encryptedPrivateKey);
            chainCopy.setEntries(chain.getEntries());
            chainCopy.setUuid(chain.getUuid());
            JAXB.marshal(chainCopy, os);
            os.flush();
        }
    }

    private KeyChain deserialize(Path fromFile) throws Exception {
        try (InputStream is = Files.newInputStream(fromFile)) {
            KeyChain kc = JAXB.unmarshal(is, KeyChain.class);
            byte[] clearKey = decryptAes(kc.getEncodedPrivateKey(), ApplicationContext.getInstance().getPasswordKey());
            kc.setEncodedPrivateKey(clearKey);
            return kc;
        }
    }

    public String encrypt(KeyChainEntry target, String content) throws Exception {
        PublicKey key = decodePublicKey(target.getEncodedPublicKey());
        byte[] enc = encrypt(key, content.getBytes(MESSAGE_ENCODING));
        String b64 = new String(Base64.encodeBase64(enc, true));
        StringBuilder sb = new StringBuilder();

        sb.append("BEGIN ENCRYPTED MESSAGE FOR ");
        sb.append(target.getId());
        sb.append("\n\nSender: ");
        sb.append(keyChain.getName());
        sb.append("\nRecipient: ");
        sb.append(target.getLabel());
        sb.append("\nDate: ");
        sb.append(new Date().toString());
        sb.append("\n\nCopy message below the following line\n----------------------------------------------------------------------------\n");
        sb.append(b64);
        return sb.toString();
    }

    public byte[] encrypt(PublicKey key, byte[] content) throws Exception {
        ByteArrayInputStream bais = new ByteArrayInputStream(content);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        encrypt(bais, baos, key);
        return baos.toByteArray();
    }

    public void encrypt(Path source, Path target, KeyChainEntry kce) throws Exception {
        InputStream is = Files.newInputStream(source);
        OutputStream os = Files.newOutputStream(target);
        PublicKey key = decodePublicKey(kce.getEncodedPublicKey());
        encrypt(is, os, key);
    }

    public void encrypt(InputStream source, OutputStream target, PublicKey key) throws Exception {
        Key aesKey = createAESKey();
        byte[] wrapped = wrap(key, aesKey);
        target.write(toByte(wrapped.length));
        target.write(wrapped);
        Cipher cipher = getAesCipher(Cipher.ENCRYPT_MODE, aesKey);
        CipherOutputStream cos = new CipherOutputStream(target, cipher);
        copy(source,cos);
        cos.flush();
        cos.close();
        target.flush();
    }

    public byte[] encryptAes(byte[] clear, SecretKey aesKey) throws Exception {
        Cipher cipher = getAesCipher(Cipher.ENCRYPT_MODE, aesKey);
        return cipher.doFinal(clear);
    }


    public void decrypt(Path source, Path target, PrivateKey key) throws Exception {
        InputStream is = Files.newInputStream(source);
        OutputStream os = Files.newOutputStream(target);
        decrypt(is, os, key);
    }

    public byte[] decrypt(PrivateKey key, byte[] content) throws Exception {
        ByteArrayInputStream bais = new ByteArrayInputStream(content);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        decrypt(bais, baos, key);
        return baos.toByteArray();
    }

    public String decrypt(String b64Encrypted) throws Exception {
        byte[] raw = Base64.decodeBase64(b64Encrypted);
        PrivateKey key = decodePrivateKey(keyChain.getEncodedPrivateKey());
        byte[] clear = decrypt(key, raw);
        return new String(clear, MESSAGE_ENCODING);
    }

    public void decrypt(InputStream source, OutputStream target, PrivateKey key) throws Exception {
        byte[] l = readExactAmountOfBytes(source, 2);
        int len = toInt(l);
        byte[] wrappedBuffer = readExactAmountOfBytes(source, len);
        Key aesKey = unwrap(key, wrappedBuffer);
        Cipher cipher = getAesCipher(Cipher.DECRYPT_MODE, aesKey);
        CipherInputStream cis = new CipherInputStream(source, cipher);
        copy(cis, target);
        cis.close();
        target.flush();
    }

    public byte[] decryptAes(byte[] encrypted, SecretKey aesKey) throws Exception {
        Cipher cipher = getAesCipher(Cipher.DECRYPT_MODE, aesKey);
        return cipher.doFinal(encrypted);
    }


    private SecretKey createAESKey() throws Exception {
        KeyGenerator kg = KeyGenerator.getInstance(SYMMETRIC_ALGORITHM);
        kg.init(AES_KEY_LENGTH);
        return kg.generateKey();
    }

    public SecretKey createAesKeyFromPassword(String password) throws Exception {
        MessageDigest dig = MessageDigest.getInstance(DIGEST_ALGORITHM);
        byte[] digest = dig.digest(password.getBytes(MESSAGE_ENCODING));
        SecretKeySpec sks = new SecretKeySpec(digest, SYMMETRIC_ALGORITHM);
        return sks;
    }

    private byte[] wrap(PublicKey key, Key keyToWrap) throws Exception {
        Cipher cipher = Cipher.getInstance(ASYMMETRIC_ALGORITHM);
        cipher.init(Cipher.WRAP_MODE, key);
        return cipher.wrap(keyToWrap);
    }

    private Key unwrap(PrivateKey key, byte[] wrapped) throws Exception {
        Cipher cipher = Cipher.getInstance(ASYMMETRIC_ALGORITHM);
        cipher.init(Cipher.UNWRAP_MODE, key);
        return cipher.unwrap(wrapped, SYMMETRIC_ALGORITHM, Cipher.SECRET_KEY);
    }

    private Cipher getAesCipher(int mode, Key key) throws Exception {
        Cipher cipher = Cipher.getInstance(SYMMETRIC_ALGORITHM);
        cipher.init(mode, key);
        return cipher;
    }


    private byte[] readExactAmountOfBytes(InputStream is, int numberOfBytes) throws IOException {
        byte[] buffer = new byte[numberOfBytes];
        int read = 0;
        while(true) {
            int currentRead = is.read(buffer, read, numberOfBytes-read);
            if (currentRead<0) break;
            if (currentRead>0) {
                read += currentRead;
                if (read==numberOfBytes) break;
            }
        }
        return buffer;
    }

    public static byte[] toByte(int i) {
        byte[] b = new byte[2];
        b[0] = (byte)(i & 0xff);
        b[1] = (byte)((i & 0xff00)>>8);
        return b;
    }

    public static int toInt(byte[] b) {
        int i= (b[1]&0xff);
        i = i<<8;
        return i | (0xff & b[0]);
    }

    private void copy(InputStream is, OutputStream os) throws IOException {
        byte[] buffer = new byte[1024*1024]; // 1MB Buffer
        int read;

        while(true) {
            read = is.read(buffer);
            if (read<0) break;
            if (read>0) {
                os.write(buffer,0,read);
            }
        }
    }
}
