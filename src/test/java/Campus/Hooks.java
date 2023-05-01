package Campus;

import Campus.Models.PositionCategories;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.http.Cookies;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.testng.annotations.BeforeClass;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;




public class Hooks {

    Cookies cookies;

    PositionCategories positionCategories;

    RequestSpecification requestSpec;
    ResponseSpecification responseSpec;

    @BeforeClass
    public void login() {

//        {
//            "username": "turkeyts",
//                "password": "TechnoStudy123",
//                "rememberMe": "true"
//        }
        baseURI = "https://test.mersys.io";
        Map<String, String> credentials = new HashMap<>();
        credentials.put("username", "turkeyts");
        credentials.put("password", "TechnoStudy123");
        credentials.put("rememberMe", "true");

        cookies = given()
                .body(credentials)
                .contentType(ContentType.JSON)

                .when()
                .post("/auth/login")

                .then()
                .statusCode(200)
                .log().body()
                .extract().response().getDetailedCookies(); // extract cookies from response

        requestSpec = new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .addCookies(cookies)
                .build();

        responseSpec = new ResponseSpecBuilder()
                .expectContentType(ContentType.JSON)
                .log(LogDetail.BODY)
                .build();
    }
}
