package tests;

import io.qameta.allure.Feature;
import io.qameta.allure.Owner;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import models.Pet;
import org.junit.jupiter.api.Test;

import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestPet {

    private static final String BASE_URL = "http://5.181.109.28:9090/api/v3";

    @Test
    @Feature("Pet")
    @Severity(SeverityLevel.CRITICAL)
    @Owner("evgenij rotanov")
    public void testDeleteNonexistentPet() {
         Response response = step("Отправить DELETE запрос на удаление несушествующего питомца",() ->
             given()
                    .contentType(ContentType.JSON)
                    .header("Accept", "application/json")
                    .when()
                    .delete(BASE_URL + "/pet/9999"));

        String responseBody = response.getBody().asString();
        step("Проверить что статус-код ответа ==200",() ->
                assertEquals(200, response.getStatusCode(),
                        "Код ответа не совпал с ожидаемым. Ответ: " + responseBody)
                );

        step("Проверить что текст ответа 'Pet deleted'",() ->
                assertEquals("Pet deleted", responseBody,
                        "Текст ошибки не совпал с ожидаемым. Получен: " + responseBody)
        );


    }
    @Test
    @Feature("Pet")
    @Severity(SeverityLevel.CRITICAL)
    @Owner("evgenij rotanov")
    public void testUpdateNonexistentPet() {
        Pet pet = new Pet();
        pet.setId(9999);
        pet.setName("Non-existent Pet");
        pet.setStatus("available");

        Response response = step("Отправить put запрос на обновление несушествующего питомца",() ->
                given()
                        .contentType(ContentType.JSON)
                        .header("Accept", "application/json")
                        .body(pet)
                        .when()
                        .put(BASE_URL + "/pet"));

        String responseBody = response.getBody().asString();

        step("Проверить, что статус-код ответа == 404", () ->
                assertEquals(404, response.getStatusCode(),
                        "Код ответа не совпал с ожидаемым. Получен: "+responseBody)
        );

        step("Проверить, что текст ответа 'Pet not found'", () ->
                assertEquals("Pet not found", responseBody,
                        "Текст ошибки не совпал с ожидаемым. Получен: "+responseBody)
        );
    }
    @Test
    @Feature("Pet")
    @Severity(SeverityLevel.CRITICAL)
    @Owner("evgenij rotanov")
    public void testGetNonexistentPet() {
        Response response = step("Отправить Get запрос на получении информации о несушествующем питомце", () ->
                given()
                        .contentType(ContentType.JSON)
                        .header("Accept", "application/json")
                        .when()
                        .get(BASE_URL + "/pet/9999"));

        String responseBody = response.getBody().asString();
        step("Проверить что статус-код ответа ==404", () ->
                assertEquals(404, response.getStatusCode(),
                        "Код ответа не совпал с ожидаемым. Ответ: " + responseBody)
        );

        step("Проверить что текст ответа 'Pet not found'", () ->
                assertEquals("Pet not found", responseBody,
                        "Текст ошибки не совпал с ожидаемым. Получен: " + responseBody)
        );

    }
}