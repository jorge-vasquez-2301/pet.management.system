package controllers;

import com.avaje.ebean.Ebean;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import models.Pet;
import org.junit.Test;
import play.Application;
import play.inject.guice.GuiceApplicationBuilder;
import play.mvc.Http;
import play.mvc.Result;
import play.test.WithApplication;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.*;
import static play.mvc.Http.Status.BAD_REQUEST;
import static play.mvc.Http.Status.OK;
import static play.test.Helpers.*;


public class PetRESTControllerTest extends WithApplication {

    @Override
    protected Application provideApplication() {
        return new GuiceApplicationBuilder().build();
    }

    @Test
    public void testCreateMalePet() {
        Http.RequestBuilder request = new Http.RequestBuilder().method(POST).uri("/pets/new/dog/Spike/male");

        Result result = route(app, request);
        assertEquals(OK, result.status());
    }

    @Test
    public void testCreateFemalePet() {
        Http.RequestBuilder request = new Http.RequestBuilder().method(POST).uri("/pets/new/cat/Whiskers/female");

        Result result = route(app, request);
        assertEquals(OK, result.status());
    }

    @Test
    public void testCreatePetWithErroneusGender() {
        Http.RequestBuilder request = new Http.RequestBuilder().method(POST).uri("/pets/new/cat/Whiskers/none");

        Result result = route(app, request);
        assertEquals(BAD_REQUEST, result.status());
    }

    @Test
    public void testSearchExistingPetByName() throws IOException {
        Http.RequestBuilder request = new Http.RequestBuilder().method(GET).uri("/pets/name/spike");

        Result result = route(app, request);
        ObjectMapper mapper = new ObjectMapper();
        TypeReference<List<Pet>> mapType = new TypeReference<List<Pet>>() {
        };
        List<Pet> pets = mapper.readValue(contentAsString(result), mapType);
        assertEquals(OK, result.status());
        assertEquals(3, pets.size());
        assertTrue(pets.stream().allMatch(pet -> pet.name.contains("Spike")));
    }

    @Test
    public void testSearchNonExistingPetByName() throws IOException {
        Http.RequestBuilder request = new Http.RequestBuilder().method(GET).uri("/pets/name/pluto");

        Result result = route(app, request);
        ObjectMapper mapper = new ObjectMapper();
        TypeReference<List<Pet>> mapType = new TypeReference<List<Pet>>() {
        };
        List<Pet> pets = mapper.readValue(contentAsString(result), mapType);
        assertEquals(OK, result.status());
        assertEquals(0, pets.size());
    }

    @Test
    public void testSearchExistingPetByType() throws IOException {
        Http.RequestBuilder request = new Http.RequestBuilder().method(GET).uri("/pets/type/dog");

        Result result = route(app, request);
        ObjectMapper mapper = new ObjectMapper();
        TypeReference<List<Pet>> mapType = new TypeReference<List<Pet>>() {
        };
        List<Pet> pets = mapper.readValue(contentAsString(result), mapType);
        assertEquals(OK, result.status());
        assertEquals(3, pets.size());
        assertTrue(pets.stream().allMatch(pet -> pet.type.contains("DOG")));
    }

    @Test
    public void testSearchNonExistingPetByType() throws IOException {
        Http.RequestBuilder request = new Http.RequestBuilder().method(GET).uri("/pets/type/cat");

        Result result = route(app, request);
        ObjectMapper mapper = new ObjectMapper();
        TypeReference<List<Pet>> mapType = new TypeReference<List<Pet>>() {
        };
        List<Pet> pets = mapper.readValue(contentAsString(result), mapType);
        assertEquals(OK, result.status());
        assertEquals(0, pets.size());
    }

    @Test
    public void testSearchExistingPetByTypeAndGender() throws IOException {
        Http.RequestBuilder request = new Http.RequestBuilder().method(GET).uri("/pets/type/dog?gender=male");

        Result result = route(app, request);
        ObjectMapper mapper = new ObjectMapper();
        TypeReference<List<Pet>> mapType = new TypeReference<List<Pet>>() {
        };
        List<Pet> pets = mapper.readValue(contentAsString(result), mapType);
        assertEquals(OK, result.status());
        assertEquals(3, pets.size());
        assertTrue(pets.stream().allMatch(pet -> pet.type.contains("DOG") && pet.gender.contains("male")));
    }

    @Test
    public void testSearchNonExistingPetByTypeAndGender() throws IOException {
        Http.RequestBuilder request = new Http.RequestBuilder().method(GET).uri("/pets/type/dog?gender=female");

        Result result = route(app, request);
        ObjectMapper mapper = new ObjectMapper();
        TypeReference<List<Pet>> mapType = new TypeReference<List<Pet>>() {
        };
        List<Pet> pets = mapper.readValue(contentAsString(result), mapType);
        assertEquals(OK, result.status());
        assertEquals(0, pets.size());
    }

    @Test
    public void testDeleteExistingPet() {
        Pet pet = Ebean.find(Pet.class, 1);
        assertNotNull(pet);
        Http.RequestBuilder request = new Http.RequestBuilder().method(DELETE).uri("/pets/delete/" + pet.id);

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
