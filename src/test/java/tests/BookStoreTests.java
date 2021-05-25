package tests;

import io.qameta.allure.restassured.AllureRestAssured;
import models.AuthorizationResponse;
import models.Books;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;

public class BookStoreTests {
    @Test
    void specsTest() {
        Specs.requestSpecs
                .when()
                .get("/BookStore/v1/Books")
                .then()
                .spec(Specs.responseSpecs)
                .body("books", hasSize(greaterThan(0)))
                .log().body();
    }

    @Test
    void specsPostTest() {
        Map<String, String> data = new HashMap<>();
        data.put("userName", "alex");
        data.put("password", "asdsad#frew_DFS2");

        Specs.requestSpecs
                .body(data)
                .when()
                .log().uri()
                .log().body()
                .post("/Account/v1/GenerateToken")
                .then()
                .spec(Specs.responseSpecs)
                .log().body()
                .body("status", is("Success"))
                .body("result", is("User authorized successfully."));
    }

    @Test
    void withAllureListenerTest() {
        Specs.requestSpecs
                .filter(new AllureRestAssured())
                .body("{ \"userName\": \"alex\", \"password\": \"asdsad#frew_DFS2\" }")
                .when()
                .log().uri()
                .log().body()
                .post("/Account/v1/GenerateToken")
                .then()
                .spec(Specs.responseSpecs)
                .log().body()
                .body("status", is("Success"))
                .body("result", is("User authorized successfully."));
    }

    @Test
    void withCustomFilterTest() {
        Specs.requestSpecs
                .body("{ \"userName\": \"alex\", \"password\": \"asdsad#frew_DFS2\" }")
                .when()
                .log().uri()
                .log().body()
                .post("/Account/v1/GenerateToken")
                .then()
                .spec(Specs.responseSpecs)
                .log().body()
                .body("status", is("Success"))
                .body("result", is("User authorized successfully."));
    }

    @Test
    void withModelTest() {
        AuthorizationResponse response =
                Specs.requestSpecs
                        .body("{ \"userName\": \"alex\", \"password\": \"asdsad#frew_DFS2\" }")
                        .when()
                        .log().uri()
                        .log().body()
                        .post("Account/v1/GenerateToken")
                        .then()
                        .spec(Specs.responseSpecs)
                        .log().body()
                        .extract().as(AuthorizationResponse.class);
        /*
        {
            "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyTmFtZSI6ImFsZXgiLCJwYXNzd29yZCI6ImFzZHNhZCNmcmV3X0RGUzIiLCJpYXQiOjE2MjE1MzQ1NzJ9.IXZNuhKHg901sJXcCf66TKirqqaL0ATJXNRcj_14iDg",
            "expires": "2021-05-27T18:16:12.901Z",
            "status": "Success",
            "result": "User authorized successfully."
        }
         */
        assertThat(response.getStatus()).isEqualTo("Success");
        assertThat(response.getResult()).isEqualTo("User authorized successfully.");
    }

    @Test
    void booksModelTest() {
        Books books =
                Specs.requestSpecs
                        .log().uri()
                        .log().body()
                        .get("/BookStore/v1/Books")
                        .then()
                        .spec(Specs.responseSpecs)
                        .log().body()
                        .extract().as(Books.class);

        System.out.println(books.getBooks());
    }

    @Test
    void booksJsonSchemaTest() {
        Specs.requestSpecs
                .log().uri()
                .log().body()
                .get("/BookStore/v1/Books")
                .then()
                .spec(Specs.responseSpecs)
                .log().body()
                .body(matchesJsonSchemaInClasspath("jsonshemas/booklist_response.json"));
    }
}