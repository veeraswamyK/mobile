//package api;
//
//import io.restassured.RestAssured;
//import io.restassured.response.Response;
//
//public class ApiClient {
//
//    public static Response get(String endpoint) {
//        return RestAssured
//                .given()
//                .baseUri("https://fakestoreapi.com")
//                .when()
//                .get(endpoint);
//    }
//
//    public static Response post(String endpoint, Object body) {
//        return RestAssured
//                .given()
//                .baseUri("https://fakestoreapi.com")
//                .body(body)
//                .when()
//                .post(endpoint);
//    }
//}