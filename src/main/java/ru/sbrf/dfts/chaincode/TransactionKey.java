package ru.sbrf.dfts.chaincode;

import java.util.Objects;

public class TransactionKey {
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

    public TransactionKey(String timestamp, String receiverSignature, String senderSignature) {
        this.timestamp = timestamp;
        this.receiverSignature = receiverSignature;
        this.senderSignature = senderSignature;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TransactionKey that = (TransactionKey) o;
        return Objects.equals(timestamp, that.timestamp) &&
                Objects.equals(receiverSignature, that.receiverSignature) &&
                Objects.equals(senderSignature, that.senderSignature);
    }

    @Override
    public int hashCode() {
        return Objects.hash(timestamp, receiverSignature, senderSignature);
    }
}
