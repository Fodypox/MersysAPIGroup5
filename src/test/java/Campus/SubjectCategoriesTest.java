package Campus;

import Campus.Models.SubjectCategories;
import io.restassured.http.ContentType;
import io.restassured.http.Cookies;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.*;

public class SubjectCategoriesTest extends Hooks {
    SubjectCategories subjectCategories;
    Response response;

    public String randomCategoryName() {
        return RandomStringUtils.randomAlphabetic(8);
    }

    public String randomCategoryCode() {
        return RandomStringUtils.randomAlphabetic(3);
    }

    @Test(priority = 0)
    public void createSubjectCategory() {
        subjectCategories = new SubjectCategories();
        subjectCategories.setName(randomCategoryName());
        subjectCategories.setCode(randomCategoryCode());

        response = given()
                .spec(requestSpec)
                .body(subjectCategories)

                .when()
                .post("/school-service/api/subject-categories")

                .then()
                .statusCode(201)
                .spec(responseSpec)
                .extract().response();
    }

    @Test(dependsOnMethods = "createSubjectCategory", priority = 1)
    public void createSubjectCategoryWithSameData() {
        subjectCategories.setName(response.jsonPath().getString("name"));
        subjectCategories.setCode(response.jsonPath().getString("code"));
        given()
                .spec(requestSpec)
                .body(subjectCategories)

                .when()
                .post("/school-service/api/subject-categories")

                .then()
                .statusCode(400)
                .spec(responseSpec);
    }

    @Test(dependsOnMethods = "createSubjectCategory", priority = 2)
    public void editSubjectCategory() {
        subjectCategories.setId(response.jsonPath().getString("id"));
        subjectCategories.setName(randomCategoryName());
        response = given()
                .spec(requestSpec)
                .body(subjectCategories)

                .when()
                .put("/school-service/api/subject-categories")
                .then()
                .statusCode(200)
                .spec(responseSpec)
                .extract().response();
    }

    @Test(dependsOnMethods = "createSubjectCategory", priority = 3)
    public void getSubjectCategory() {
        subjectCategories.setName(response.jsonPath().getString("name"));

        given()
                .spec(requestSpec)
                .body(subjectCategories)

                .when()
                .get("/school-service/api/subject-categories")

                .then()
                .spec(responseSpec)
                .statusCode(200);
    }

    @Test(dependsOnMethods = "createSubjectCategory", priority = 4)
    public void deleteSubjectCategory() {
        subjectCategories.setName(response.jsonPath().getString("id"));

        given()
                .spec(requestSpec)
                .pathParam("subjectCategoryId", response.jsonPath().getString("id"))

                .when()
                .delete("/school-service/api/subject-categories/{subjectCategoryId}")

                .then()
                .statusCode(200);
    }

    @Test(dependsOnMethods = {"createSubjectCategory", "deleteSubjectCategory"}, priority = 5)
    public void deleteSubjectCategoryNegativeTest() {
        subjectCategories.setName(response.jsonPath().getString("id"));

        given()
                .spec(requestSpec)
                .pathParam("subjectCategoryId", response.jsonPath().getString("id"))

                .when()
                .delete("/school-service/api/subject-categories/{subjectCategoryId}")

                .then()
                .statusCode(400);
    }
}
