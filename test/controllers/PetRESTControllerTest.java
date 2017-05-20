package controllers;

import org.junit.Test;
import play.Application;
import play.inject.guice.GuiceApplicationBuilder;
import play.mvc.Http;
import play.mvc.Result;
import play.test.WithApplication;

import static org.junit.Assert.assertEquals;
import static play.mvc.Http.Status.BAD_REQUEST;
import static play.mvc.Http.Status.OK;
import static play.test.Helpers.POST;
import static play.test.Helpers.route;

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
}
