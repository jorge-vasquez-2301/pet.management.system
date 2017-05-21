package controllers;

import com.avaje.ebean.Ebean;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import models.Pet;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import play.Application;
import play.db.Database;
import play.db.evolutions.Evolution;
import play.db.evolutions.Evolutions;
import play.inject.guice.GuiceApplicationBuilder;
import play.mvc.Http;
import play.mvc.Result;
import play.test.WithApplication;

import java.io.IOException;
import java.util.List;
import java.util.function.BinaryOperator;
import java.util.function.Predicate;

import static org.junit.Assert.*;
import static play.mvc.Http.Status.OK;
import static play.test.Helpers.*;


public class PetRESTControllerTest extends WithApplication {

    private static final BinaryOperator<Pet> COMPARING_BY_TIMESTAMP = (pet1, pet2) -> pet1.getTimestamp().after(pet2.getTimestamp()) ? pet1 : pet2;
    private static final BinaryOperator<Pet> COMPARING_BY_NAME = (pet1, pet2) -> pet1.getName().compareTo(pet2.getName()) < 0 ? pet1 : pet2;

    Database database;

    private Predicate<Pet> filteringByTimestamp(List<Pet> pets) {
        return pet -> pet.getTimestamp().equals(pets.get(0).getTimestamp());
    }

    private Predicate<Pet> filteringByName(List<Pet> pets) {
        return pet -> pet.getName().equals(pets.get(0).getName());
    }

    private List<Pet> parseJson(Result result) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        TypeReference<List<Pet>> mapType = new TypeReference<List<Pet>>() {
        };
        JsonFactory jsonFactory = new JsonFactory();
        JsonParser parser = jsonFactory.createParser(contentAsBytes(result).toArray());
        return mapper.readValue(parser, mapType);
    }

    private boolean isInOrder(List<Pet> pets, BinaryOperator<Pet> comparingFunction, Predicate<Pet> filteringFunction) {
        return pets.stream()
                   .reduce(comparingFunction)
                   .filter(filteringFunction)
                   .isPresent();
    }

    @Override
    protected Application provideApplication() {
        return new GuiceApplicationBuilder().build();
    }

    @Before
    public void createDatabase() {
        database = app.injector().instanceOf(Database.class);
        Evolutions.applyEvolutions(database, Evolutions.forDefault(
                new Evolution(
                        2,
                        "INSERT INTO PET (TYPE, NAME, GENDER, TIMESTAMP) VALUES \n" +
                                "('DOG', 'Spike', 'male', '2017-05-20 12:00:00'),\n" +
                                "('DOG', 'Spike Jr.', 'male', '2017-05-20 12:00:00'),\n" +
                                "('DOG', 'Spike Third', 'male', '2017-05-20 12:00:00');",
                        "DELETE FROM PET;"
                )
        ));
    }

    @After
    public void shutdownDatabase() {
        Evolutions.cleanupEvolutions(database);
    }

    @Test
    public void testCreateMalePet() {
        Http.RequestBuilder request = new Http.RequestBuilder().method(POST).uri("/pets/new/dog/Spike/male/20170521-120000");

        Result result = route(app, request);
        assertEquals(OK, result.status());
    }

    @Test
    public void testCreateFemalePet() {
        Http.RequestBuilder request = new Http.RequestBuilder().method(POST).uri("/pets/new/cat/Whiskers/female/20170521-120000");

        Result result = route(app, request);
        assertEquals(OK, result.status());
    }

    @Test
    public void testCreatePetWithInvalidTimestamp() {
        Http.RequestBuilder request = new Http.RequestBuilder().method(POST).uri("/pets/new/cat/Whiskers/female/20170521120000");

        Result result = route(app, request);
        assertEquals(BAD_REQUEST, result.status());
    }

    @Test
    public void testSearchExistingPetByName() throws IOException {
        Http.RequestBuilder request = new Http.RequestBuilder().method(GET).uri("/pets/name/spike");

        Result result = route(app, request);
        List<Pet> pets = parseJson(result);
        assertEquals(OK, result.status());
        assertEquals(3, pets.size());
        assertTrue(pets.stream().allMatch(pet -> pet.getName().toLowerCase().contains("spike")));
        assertTrue(isInOrder(pets, COMPARING_BY_NAME, filteringByName(pets)));
    }

    @Test
    public void testSearchNonExistingPetByName() throws IOException {
        Http.RequestBuilder request = new Http.RequestBuilder().method(GET).uri("/pets/name/pluto");

        Result result = route(app, request);
        List<Pet> pets = parseJson(result);
        assertEquals(OK, result.status());
        assertEquals(0, pets.size());
    }

    @Test
    public void testSearchExistingPetByType() throws IOException {
        Http.RequestBuilder request = new Http.RequestBuilder().method(GET).uri("/pets/type/dog");

        Result result = route(app, request);
        List<Pet> pets = parseJson(result);
        assertEquals(OK, result.status());
        assertEquals(3, pets.size());
        assertTrue(pets.stream().allMatch(pet -> pet.getType().toLowerCase().contains("dog")));
        assertTrue(isInOrder(pets, COMPARING_BY_TIMESTAMP, filteringByTimestamp(pets)));
    }

    @Test
    public void testSearchNonExistingPetByType() throws IOException {
        Http.RequestBuilder request = new Http.RequestBuilder().method(GET).uri("/pets/type/cat");

        Result result = route(app, request);
        List<Pet> pets = parseJson(result);
        assertEquals(OK, result.status());
        assertEquals(0, pets.size());
    }

    @Test
    public void testSearchExistingPetByTypeAndGender() throws IOException {
        Http.RequestBuilder request = new Http.RequestBuilder().method(GET).uri("/pets/type/dog?gender=male");

        Result result = route(app, request);
        List<Pet> pets = parseJson(result);
        assertEquals(3, pets.size());
        assertTrue(pets.stream().allMatch(pet -> pet.getType().toLowerCase().contains("dog") && pet.getGender().toLowerCase().contains("male")));
        assertTrue(isInOrder(pets, COMPARING_BY_TIMESTAMP, filteringByTimestamp(pets)));
    }

    @Test
    public void testSearchNonExistingPetByTypeAndGender() throws IOException {
        Http.RequestBuilder request = new Http.RequestBuilder().method(GET).uri("/pets/type/dog?gender=female");

        Result result = route(app, request);
        List<Pet> pets = parseJson(result);
        assertEquals(OK, result.status());
        assertEquals(0, pets.size());
    }

    @Test
    public void testDeleteExistingPet() {
        Pet pet = Ebean.find(Pet.class, 1);
        assertNotNull(pet);
        Http.RequestBuilder request = new Http.RequestBuilder().method(DELETE).uri("/pets/delete/" + pet.getId());

        Result result = route(app, request);
        assertEquals(OK, result.status());
    }

    @Test
    public void testDeleteNonExistingPet() {
        Pet pet = Ebean.find(Pet.class, -1);
        assertNull(pet);
        Http.RequestBuilder request = new Http.RequestBuilder().method(DELETE).uri("/pets/delete/2");

        Result result = route(app, request);
        assertEquals(OK, result.status());
    }
}
