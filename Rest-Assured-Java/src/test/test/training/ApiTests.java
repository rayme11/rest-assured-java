package test.training;
import io.restassured.http.ContentType;
import models.Product;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class ApiTests {

    @Test
    public void getCategories(){
        String endpoint = "http://localhost:8888/api_testing/category/read.php";
        var response = given().when().get(endpoint).then();
        response.log().body();
    }

    @Test
    public void  getProduct(){
        String endpoint = "http://localhost:8888/api_testing/product/read_one.php";
        var response =
                given()
                        .queryParam("id", 1)
                        .when()
                        .get(endpoint)
                        .then()
                        .assertThat().statusCode(200)
                        .body("id", equalTo("1")).
                        body("name", equalTo("Bamboo Thermal Ski Coat"));
        response.log().body();

    }

    @Test
    public void  getProducts(){
        String endpoint = "http://localhost:8888/api_testing/product/read.php";
        var response =
                given()
                        .when()
                        .get(endpoint)
                        .then()
                        .assertThat().statusCode(200)
                        .body("records.size()",greaterThan(4))
                        .body("records.id", everyItem(notNullValue()));

        response.log().body();

    }

    @Test
        public void createSerializedProduct(){

            String endpoint = "http://localhost:8888/api_testing/product/create.php";
            Product product = new Product(
                    "Water Bottle",
                    "Blue Corn Bottle - Large New Edition",
                    12.00,
                    3
            );

            var response = given()
                    .body(product).when().post(endpoint).then()
                    .log().headers();
            response.log().body();
        }

    @Test
    public void updateSerializedProduct(){

        String endpoint = "http://localhost:8888/api_testing/product/update.php";
        Product product = new Product(
                21,
                "Water Bottle",
                "Blue Corn Bottle - Large Update",
                15.00,
                3,
                "Supplements"
        );

        var response = given()
                .body(product).when().put(endpoint).then();
        response.log().body();
    }

    @Test
        public void deleteSerializedProduct(){
            String endpoint = "http://localhost:8888/api_testing/product/delete.php";
          Product product = new Product(
                  22
          );


            var response = given()
                    .body(product).when().delete(endpoint).then();
            response.log().body();
        }

    @Test
    public void  getDeserializedProduct(){
        String endpoint = "http://localhost:8888/api_testing/product/read_one.php";
       Product expectedProduct = new Product(
               2,
               "Cross-Back Training Tank",
               "The most awesome phone of 2013!",
               299.00,
               2,
               "Active Wear - Women"
       );

        Product actualProduct =
                given()
                        .queryParam("id", 2).
                        when().
                        get(endpoint).as(Product.class);

        assertThat(actualProduct, samePropertyValuesAs(expectedProduct));



    }


}
