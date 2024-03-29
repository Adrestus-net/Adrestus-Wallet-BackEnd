package io.Adrestus.Backend.Util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import io.Adrestus.Backend.DTO.LimitTransactionsDetailsDTO;
import lombok.SneakyThrows;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class JsonConvertUtil {


    @SneakyThrows
    public static String ObjtoString(Object object) {
        StringWriter jsonString = new StringWriter();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writeValue(jsonString, object);
        return jsonString.toString();
    }

    @SneakyThrows
    public static String ArrayObjectToString(List<LimitTransactionsDetailsDTO> objects) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
        StringWriter stringWriter = new StringWriter();
        objectMapper.writeValue(stringWriter, objects);
        return stringWriter.toString();
    }
}
