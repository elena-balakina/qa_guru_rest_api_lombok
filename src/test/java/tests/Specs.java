package tests;

import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

import static filters.CustomLogFilter.customLogFilter;
import static io.restassured.RestAssured.with;

public class Specs {

    public static RequestSpecification requestSpecs = with()
            .baseUri("https://demoqa.com")
            .log().all()
            .contentType(ContentType.JSON)
            .filter(customLogFilter().withCustomTemplates());

    public static ResponseSpecification responseSpecs = new ResponseSpecBuilder()
            .expectStatusCode(200)
            .build();
}
