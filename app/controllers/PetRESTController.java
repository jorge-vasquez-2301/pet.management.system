package controllers;

import com.avaje.ebean.Ebean;
import models.Pet;
import play.cache.CacheApi;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

/**
 * The PetRESTController class.
 * This controller exposes the app's RESTful API
 * @author Jorge Vasquez
 * @since 1.8
 */
public class PetRESTController extends Controller {

    /* constants. */
    private static final String SEARCH_BY_NAME_KEY = "name:";
    private static final String SEARCH_BY_TYPE_KEY = "type:";
    private static final String SEARCH_BY_GENDER_KEY = "gender:";

    private CacheApi cache;

    /**
     * Creates a new instance of PetRESTController.
     * @param cache Application's cache
     */
    @Inject
    public PetRESTController(CacheApi cache) {
        this.cache = cache;
    }

    /**
     * This action creates a new pet for the given parameters.
     * @param petType The pet's type (i.e. dog, cat, ...)
     * @param name    The pet's name
     * @param gender  The pet's gender (i.e. male, female)
     * @return the Result containing the result code
     */
    public Result createPet(String petType, String name, String gender) {
        Pet pet = new Pet(petType, name, gender);
        pet.save();
        return ok();
    }

    /**
     * This action allows to search a pet by name.
     * @param name The searched name
     * @return the Result containing search results
     */
    public CompletionStage<Result> searchByName(String name) {
        return CompletableFuture.supplyAsync(() -> cache.getOrElse(SEARCH_BY_NAME_KEY + name.toLowerCase(),
                                                                   () -> Pet.findByName(name)))
                                .thenApply(pets -> ok(Json.toJson(pets)));
    }

    /**
     * This action allows to search a pet by type and gender
     * @param petType The searched type
     * @param gender  The searched gender (optional)
     * @return the Result containing search results
     */
    public CompletionStage<Result> searchByTypeAndGender(String petType, String gender) {
        String cacheKey = SEARCH_BY_TYPE_KEY + petType + ((gender != null) ? "," + SEARCH_BY_GENDER_KEY + gender : "");
        Callable<List<Pet>> actionToExecute = () -> (gender != null) ? Pet.findByTypeAndGender(petType, gender)
                                                                     : Pet.findByType(petType);
        return CompletableFuture.supplyAsync(() -> cache.getOrElse(cacheKey, actionToExecute))
                                .thenApply(pets -> ok(Json.toJson(pets)));
    }

    /**
     * This action allows to delete a pet with the given id
     * @param id The pet's id
     * @return the Result containing the result code
     */
    public Result deletePet(long id) {
        Pet pet = Ebean.find(Pet.class, id);
        if (pet != null) {
            pet.delete();
        }
        return ok();
    }
}
