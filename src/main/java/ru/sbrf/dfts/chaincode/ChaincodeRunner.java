package ru.sbrf.dfts.chaincode;

public class ChaincodeRunner {
    public static void main(String[] args) {
        ChaincodeImpl chaincode = new ChaincodeImpl();
        chaincode.start(args);
    }
}
