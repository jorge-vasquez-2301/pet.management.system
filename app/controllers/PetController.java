package controllers;

import play.mvc.Controller;
import play.mvc.Result;

/**
 * This controller contains an action to handle HTTP requests
 * to the application's home page.
 */
public class PetController extends Controller {

    /**
     * This action renders the index page.
     * @return the Result containing the information to render the index page
     */
    public Result index() {
        return ok(views.html.index.render());
    }

    /**
     * This action creates a new pet for the given parameters.
     * @param petType The pet's type (i.e. dog, cat, ...)
     * @param name    The pet's name
     * @param gender  The pet's gender (i.e. male, female)
     * @return the Result containing the information to render the form with pet's information
     */
    public Result createPet(String petType, String name, String gender) {
        return TODO;
    }

    /**
     * This action allows to search a pet by name.
     * @param name The searched name
     * @return the Result containing the information to render the page with search results
     */
    public Result searchByName(String name) {
        return TODO;
    }

    /**
     * This action allows to search a pet by type and gender
     * @param petType The searched type
     * @param gender  The searched gender (optional)
     * @return the Result containing the information to render the page with search results
     */
    public Result searchByTypeAndGender(String petType, String gender) {
        return TODO;
    }

    /**
     * This action allows to delete a pet with the given id
     * @param id The pet's id
     * @return the Result containing the information to render the page with the deletion result
     */
    public Result deletePet(long id) {
        return TODO;
    }
}
