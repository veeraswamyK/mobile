//package api;
//
//import io.restassured.response.Response;
//import org.testng.Assert;
//
//public class BackendValidator {
//
//    public static void validateProductsAPI() {
//        Response response = ApiClient.get("/products");
//
//        Assert.assertEquals(response.statusCode(), 200);
//        Assert.assertTrue(response.jsonPath().getList("$").size() > 0);
//
//        System.out.println("Backend product validation successful");
//    }
//}