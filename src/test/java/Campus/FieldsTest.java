package Campus;

import Campus.Models.Fields;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.*;

public class FieldsTest extends Hooks {

    public String randomPositionsCategoriesName() {

        return RandomStringUtils.randomAlphabetic(8);
    }

    public String randomPositionsCategoriesCode() {

        return RandomStringUtils.randomAlphabetic(3);
    }



    Fields fields;
    Response response;


    @Test
    public void createFields(){
        fields = new Fields();
        fields.setName(randomPositionsCategoriesName());
        fields.setCode(randomPositionsCategoriesCode());
        fields.setType("STRING");
        fields.setSchoolId("6390f3207a3bcb6a7ac977f9");

        response = given()
                .spec(requestSpec)
                .body(fields)

                .when()
                .post("/school-service/api/entity-field")

                .then()
                .spec(responseSpec)
                .statusCode(201)
                .extract().response();

    }

    @Test(dependsOnMethods = "createFields", priority = 1)
    public void getFields(){

        given()
                .spec(requestSpec)
                .pathParam("fieldsId", response.jsonPath().getString("id"))

                .when()
                .get("/school-service/api/entity-field/{fieldsId}")

                .then()
                .spec(responseSpec)
                .statusCode(200);
    }

    @Test(dependsOnMethods = "createFields", priority = 2)
    public void createFieldsNegativeTest(){

        given()
                .spec(requestSpec)
                .body(fields)

                .when()
                .post("/school-service/api/entity-field")

                .then()
                .spec(responseSpec)
                .statusCode(400);

    }

    @Test(dependsOnMethods = "createFields", priority = 3)
    public void editFields(){
        fields.setName(randomPositionsCategoriesName());
        fields.setCode(randomPositionsCategoriesCode());
        fields.setId(response.jsonPath().getString("id"));
        given()
                .spec(requestSpec)
                .body(fields)

                .when()
                .put("/school-service/api/entity-field")

                .then()
                .spec(responseSpec)
                .statusCode(200);

    }

    @Test(dependsOnMethods = "createFields", priority = 4)
    public void deleteFields(){

        given()
                .spec(requestSpec)
                .pathParam("fieldsId", response.jsonPath().getString("id"))

                .when()
                .delete("/school-service/api/entity-field/{fieldsId}")

                .then()
                .statusCode(204);
    }

    @Test(dependsOnMethods = {"createFields","deleteFields"},priority = 5)
    public void getFieldsAfterDeleted(){
        given()
                .spec(requestSpec)
                .pathParam("fieldsId", response.jsonPath().getString("id"))

                .when()
                .get("/school-service/api/entity-field/{fieldsId}")

                .then()
                .spec(responseSpec)
                .statusCode(400);

    }

    @Test(dependsOnMethods = {"createFields","deleteFields"}, priority = 6)
    public void deleteFieldsAfterDeleted(){
        given()
                .spec(requestSpec)
                .pathParam("fieldsId", response.jsonPath().getString("id"))

                .when()
                .delete("/school-service/api/entity-field/{fieldsId}")

                .then()
                .spec(responseSpec)
                .statusCode(400);

    }

}
