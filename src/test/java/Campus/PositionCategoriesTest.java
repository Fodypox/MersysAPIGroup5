package Campus;

import Campus.Models.PositionCategories;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class PositionCategoriesTest extends Hooks{

//    Cookies cookies;

    PositionCategories positionCategories;
    Response response;

//    RequestSpecification requestSpec;
//    ResponseSpecification responseSpec;



    public String randomPositionsCategoriesName() {

        return RandomStringUtils.randomAlphabetic(8);

    }

    @Test(priority = 0)
    public void createPositionCategories(){
        positionCategories = new PositionCategories();
        positionCategories.setName(randomPositionsCategoriesName());
        response = given()
                .spec(requestSpec)
                .body(positionCategories)


                .when()
                .post("/school-service/api/position-category")

                .then()
                .statusCode(201)
                .spec(responseSpec)
                .extract().response();


    }

    @Test(dependsOnMethods = "createPositionCategories", priority = 1)
    public void createPositionCategoriesWithSameData(){
        positionCategories.setName(response.jsonPath().getString("name"));
        given()
                .spec(requestSpec)
                .body(positionCategories)


                .when()
                .post("/school-service/api/position-category")

                .then()
                .statusCode(400)
                .spec(responseSpec);


    }

    @Test(dependsOnMethods = "createPositionCategories", priority = 2)
    public void updatePositionCategories(){
        positionCategories.setId(response.jsonPath().getString("id"));
        positionCategories.setName(randomPositionsCategoriesName());
        response = given()
                .spec(requestSpec)
                .body(positionCategories)


                .when()
                .put("/school-service/api/position-category")

                .then()
                .statusCode(200)
                .spec(responseSpec)
                .extract().response();


    }

    @Test(dependsOnMethods = "createPositionCategories", priority = 3)
    public void getPositionCategories(){
        positionCategories.setName(response.jsonPath().getString("name"));


        given()
                .spec(requestSpec)
                .body(positionCategories)

                .when()
                .post("school-service/api/position-category/search")

                .then()
                .spec(responseSpec)
                .statusCode(200);
    }
    @Test(dependsOnMethods = "createPositionCategories", priority = 4)
    public void deletePositionCategories(){
        positionCategories.setId(response.jsonPath().getString("id"));

        given()
                .spec(requestSpec)
                .pathParam("positionCategoryId", response.jsonPath().getString("id"))


                .when()
                .delete("/school-service/api/position-category/{positionCategoryId}")

                .then()
                .statusCode(204);


    }

    @Test(dependsOnMethods = {"createPositionCategories","deletePositionCategories"}, priority = 5)
    public void deleteNonExistingPositionCategories(){
        positionCategories.setId(response.jsonPath().getString("id"));

        given()
                .spec(requestSpec)
                .pathParam("positionCategoryId", response.jsonPath().getString("id"))


                .when()
                .delete("/school-service/api/position-category/{positionCategoryId}")

                .then()
                .statusCode(400);


    }


}
