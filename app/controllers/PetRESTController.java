package controllers;

import models.Pet;
import play.Logger;
import play.cache.CacheApi;
import play.mvc.Controller;
import play.mvc.Result;

import javax.inject.Inject;

/**
 * The PetRESTController class.
 * This controller exposes the app's RESTful API
 */
public class PetRESTController extends Controller {

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
        try {
            Pet pet = Pet.Gender.getGender(gender)
                                .map(petGender -> new Pet(petType, name, gender))
                                .orElseThrow(IllegalArgumentException::new);
            pet.save();
            return ok();
        } catch (IllegalArgumentException ex) {
            Logger.warn(ex.getMessage());
            return badRequest("Illegal gender, must be male or female");
        }
    }

    /**
     * This action allows to search a pet by name.
     * @param name The searched name
     * @return the Result containing search results
     */
    public Result searchByName(String name) {
        return TODO;
    }

    /**
     * This action allows to search a pet by type and gender
     * @param petType The searched type
     * @param gender  The searched gender (optional)
     * @return the Result containing search results
     */
    public Result searchByTypeAndGender(String petType, String gender) {
        return TODO;
    }

    /**
     * This action allows to delete a pet with the given id
     * @param id The pet's id
     * @return the Result containing the result code
     */
    public Result deletePet(long id) {
        return TODO;
    }
}
