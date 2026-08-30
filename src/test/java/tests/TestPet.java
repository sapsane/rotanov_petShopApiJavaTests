package tests;

import io.qameta.allure.Feature;
import io.qameta.allure.Owner;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import models.Pet;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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

    @ParameterizedTest(name = "Добавление питомца со статусом: {2}")
    @CsvSource({
            "200, Kiwi, available, 200",
            "201, Buddy, pending, 200",
            "202, Garfield, sold, 200",
            "203, Kisa, sold1, 400"
    })
    @Feature("Pet")
    @Severity(SeverityLevel.CRITICAL)
    @Owner("Evgenij Rotanov")
    public void testAddNewPet(int id, String name, String status,int statusCode) {
        Pet pet = new Pet();
        pet.setId(id);
        pet.setName(name);
        pet.setStatus(status);
        pet.setstatusCode(statusCode);


        Response response = step("Отправить POST запрос на добавление питомца", () ->
                given()
                        .contentType(ContentType.JSON)
                        .header("Accept", "application/json")
                        .body(pet)
                        .when()
                        .post(BASE_URL + "/pet"));

        String responseBody = response.getBody().asString();
        int expectedCode=pet.getstatusCode();

        step("Проверить, что статус-код ответа == 200", () ->
                assertEquals(expectedCode, response.getStatusCode(),
                        "Код ответа не совпал с ожидаемым. Ответ: " + responseBody)
        );
        if (expectedCode == 200) {
            step("Проверка параметров созданного питомца", () -> {
                        Pet createdPet = response.as(Pet.class);
                        assertEquals(pet.getId(), createdPet.getId(), "id питомца не совпадает с ожидаемым");
                        assertEquals(pet.getName(), createdPet.getName(), "имя питомца не совпадает с ожидаемым");
                        assertEquals(pet.getStatus(), createdPet.getStatus(), "статус питомца не совпадает с ожидаемым");
                    }
            );
        }else {
            assertTrue(response.asString().contains("Invalid pet status"));
        }


    }


    @ParameterizedTest(name = "Получение питомца со статусом: {2}")
    @CsvSource({
            "200, Kiwi, available, 200",
            "201, Buddy, pending, 200",
            "202, Garfield, sold, 200",
            "9999, Kisa, sold1, 404"
    })
    @Feature("Pet")
    @Severity(SeverityLevel.CRITICAL)
    @Owner("evgenij rotanov")
    public void testGetCreatePet_1(int id, String name, String status,int statusCode) {
        Pet pet = new Pet();
        pet.setId(id);
        pet.setName(name);
        pet.setStatus(status);
        pet.setstatusCode(statusCode);

        Response response = step("Отправить Get запрос на получении информации о питомце", () ->
                given()
                        .contentType(ContentType.JSON)
                        .header("Accept", "application/json")
                        .when()
                        .get(BASE_URL + "/pet/"+id));

        int expectedCode=pet.getstatusCode();
        String responseBody = response.getBody().asString();
        step("Проверить что статус-код ответа =="+expectedCode, () ->
                assertEquals(expectedCode, response.getStatusCode(),
                        "Код ответа не совпал с ожидаемым. Ответ: " + responseBody)
        );
        if (expectedCode==200){
            step("Проверка параметров полученного питомца", () -> {
                        Pet createdPet = response.as(Pet.class);
                        assertEquals(pet.getId(), createdPet.getId(), "id питомца не совпадает с ожидаемым");
                        assertEquals(pet.getName(), createdPet.getName(), "имя питомца не совпадает с ожидаемым");
                        assertEquals(pet.getStatus(), createdPet.getStatus(), "статус питомца не совпадает с ожидаемым");
                    }
            );
        }else{
            step("Проверить что текст ответа 'Pet not found'", () ->
                    assertEquals("Pet not found", responseBody,
                            "Текст ошибки не совпал с ожидаемым. Получен: " + responseBody)
            );
        }


    }


}