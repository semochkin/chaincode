package ru.sbrf.dfts.chaincode;

import org.hyperledger.fabric.shim.ChaincodeBase;
import org.hyperledger.fabric.shim.ChaincodeStub;
import ru.sbrf.dfts.chaincode.utilities.CipherHelper;
import ru.sbrf.dfts.chaincode.utilities.JsonHelper;

import java.util.Calendar;
import java.util.List;

public class ChaincodeImpl extends ChaincodeBase {

    private String operation;

    /**
     * Symmetric key for decrypt file
     */
    private String symmetricKey;

    /**
     * Checksum of original file;
     */
    private String originalFileChecksum;

    /**
     * Checksum of encrypted file;
     */
    private String encryptedFileChecksum;

    /**
     * timestamp of approve to receive file from receiver
     */
    private String timestamp;

    /**
     * Signature of sender
     */
    private String senderSignature;

    /**
     * Signature of receiver
     */
    private String receiverSignature;

    @Override
    public Response init(ChaincodeStub stub) {
        return createResponse(Response.Status.SUCCESS, "ok", null);
    }

    @Override
    public Response invoke(ChaincodeStub stub) {
        Response response = new Response(Response.Status.SUCCESS, "ok", null);
        if (!readParameters(stub.getParameters())) {
            return createResponse(Response.Status.INTERNAL_SERVER_ERROR, "incorrect input parameters", null);
        }

        switch (operation) {
            case "init" :
                String transactionKey = generateTransactionKey(timestamp);
                if (stub.getState(transactionKey) != null) {
                    return createResponse(Response.Status.INTERNAL_SERVER_ERROR, "state already exists", null);
                } else {
                    stub.putStringState(transactionKey, JsonHelper.toJson(generateTransaction(timestamp, Transaction.State.INITIAL)));
                    return createResponse(Response.Status.SUCCESS, "init completed", null);
                }
            case "confirm" :
                String transactionInfo = stub.getStringState(generateTransactionKey(timestamp));
                Transaction transaction = JsonHelper.fromJson(transactionInfo);
                //compare checksum from receiver and transaction in ledger
                if (encryptedFileChecksum.equals(transaction.getEncryptedFileChecksum())) {
                    //receive original symmetric key for encrypt file
                    String originalSymmetricKey = CipherHelper.decrypt(transaction.getEncryptedSymmetricKey());
                    String timestamp = String.valueOf(Calendar.getInstance().getTimeInMillis());
                    //write confirmation transaction to ledger
                    stub.putStringState(
                            generateTransactionKey(timestamp),
                            JsonHelper.toJson(generateTransaction(timestamp, Transaction.State.CONFIRMED)));
                    return createResponse(Response.Status.SUCCESS, originalSymmetricKey, null);
                }
        }

        return response;
    }

    private String generateTransactionKey(String timestamp) {
        TransactionKey key = new TransactionKey(timestamp, receiverSignature, senderSignature);
        return String.valueOf(key.hashCode());
    }

    private Transaction generateTransaction(String timestamp, Transaction.State state) {
        return new Transaction(
                timestamp,
                receiverSignature,
                senderSignature,
                originalFileChecksum,
                encryptedFileChecksum,
                CipherHelper.encrypt(symmetricKey),
                state
        );
    }

    private boolean readParameters(List<String> parameters) {
        boolean result = false;

        if (parameters == null || parameters.isEmpty()) {
            return result;
        }

        for (int i = 0; i < parameters.size(); i++) {
            //when all parameters was successful reading
            if (result)
            {
                break;
            }

            String parameter = parameters.get(i);
            if ((parameter == null || parameter.isEmpty()) && i < 7) {
                break;
            }

            switch (i) {
                case 0 :
                    operation = parameter;
                    break;
                case 1 :
                    symmetricKey = parameter;
                    break;
                case 2 :
                    originalFileChecksum = parameter;
                    break;
                case 3 :
                    encryptedFileChecksum = parameter;
                    break;
                case 4 :
                    timestamp = parameter;
                    break;
                case 5 :
                    senderSignature = parameter;
                    break;
                case 6 :
                    receiverSignature = parameter;
                    break;
                default:
                    result = true;
                    break;
            }
        }

        return result;
    }

    private Response createResponse(Response.Status status, String message, byte[] payload) {
        return new Response(status, message, payload);
    }
}
