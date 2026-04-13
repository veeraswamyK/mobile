//package utils;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import java.io.File;
//import java.util.Map;
//
//public class JsonUtils {
//
//    public static Map<String, Object> readJson(String path) {
//        try {
//            ObjectMapper mapper = new ObjectMapper();
//            return mapper.readValue(new File(path), Map.class);
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }
//}