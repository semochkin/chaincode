package ru.sbrf.dfts.chaincode;

public class Transaction {

    /**
     * timestamp of approve to receive file from receiver
     */
    private String timestamp;

    /**
     * Signature of receiver
     */
    private String receiverSignature;

    /**
     * Signature of sender
     */
    private String senderSignature;

    /**
     * Checksum of original file;
     */
    private String originalFileChecksum;

    /**
     * Checksum of encrypted file;
     */
    private String encryptedFileChecksum;

    private String encryptedSymmetricKey;

    private State state;

    public Transaction(String timestamp, String receiverSignature, String senderSignature, String originalFileChecksum, String encryptedFileChecksum, String encryptedSymmetricKey, State state) {
        this.timestamp = timestamp;
        this.receiverSignature = receiverSignature;
        this.senderSignature = senderSignature;
        this.originalFileChecksum = originalFileChecksum;
        this.encryptedFileChecksum = encryptedFileChecksum;
        this.encryptedSymmetricKey = encryptedSymmetricKey;
        this.state = state;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getReceiverSignature() {
        return receiverSignature;
    }

    public String getSenderSignature() {
        return senderSignature;
    }

    public String getOriginalFileChecksum() {
        return originalFileChecksum;
    }

    public String getEncryptedFileChecksum() {
        return encryptedFileChecksum;
    }

    public String getEncryptedSymmetricKey() {
        return encryptedSymmetricKey;
    }

    public State getState() {
        return state;
    }

    public enum State {
        INITIAL,
        CONFIRMED
    }
}
