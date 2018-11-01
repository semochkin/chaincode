package ru.sbrf.dfts.chaincode.utilities;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import ru.sbrf.dfts.chaincode.Transaction;

import java.io.IOException;

public class JsonHelper {
    private static final Log LOG = LogFactory.getLog(JsonHelper.class);

    private JsonHelper() {}

    public static String toJson(Transaction transaction) {
        ObjectMapper objectMapper = new ObjectMapper();
        String result = null;
        try {
            result = objectMapper.writeValueAsString(transaction);
        } catch (JsonProcessingException e) {
            LOG.error(e);
        }
        return result;
    }

    public static Transaction fromJson(String json) {
        ObjectMapper objectMapper = new ObjectMapper();
        Transaction result = null;
        try {
            result = objectMapper.readValue(json, Transaction.class);
        } catch (IOException e) {
            LOG.error(e);
        }
        return result;
    }
}
