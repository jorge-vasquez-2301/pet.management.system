package models;


import com.avaje.ebean.Model;
import play.data.validation.Constraints;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;
import java.util.List;

import static com.avaje.ebean.Expr.ilike;

/**
 * The Pet class.
 * This class models the information about a pet.
 * @author Jorge Vasquez
 * @since 1.8
 */
@Entity
public class Pet extends Model {

    /* constants for columns. */
    private static final String TYPE = "TYPE";
    private static final String NAME = "NAME";
    private static final String GENDER = "GENDER";
    private static final String TIMESTAMP = "TIMESTAMP";

    /* constants for ordering. */
    private static final String DESC = "desc";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Constraints.Required
    private String type;

    @Constraints.Required
    private String name;

    @Constraints.Required
    private String gender;

    @Constraints.Required
    private Date timestamp;

    private static Finder<Long, Pet> find = new Finder<>(Pet.class);

    /**
     * Creates a new instance of Pet.
     * @param type      The pet's type (i.e. dog, cat, ...)
     * @param name      The pet's name
     * @param gender    The pet's gender (i.e. male, female)
     * @param timestamp The pet's creation timestamp
     */
    public Pet(String type, String name, String gender, Date timestamp) {
        this.type = type;
        this.name = name;
        this.gender = gender;
        this.timestamp = timestamp;
    }

    /**
     * @return pet's id
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the pet's id.
     * @param id the new id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return pet's type
     */
    public String getType() {
        return type;
    }

    /**
     * Sets the pet's type.
     * @param type the new type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return pet's name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the pet's name.
     * @param name the new name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return pet's gender
     */
    public String getGender() {
        return gender;
    }

    /**
     * Sets the pet's gender.
     * @param gender the new gender
     */
    public void setGender(String gender) {
        this.gender = gender;
    }

    /**
     * @return pet's creation timestamp
     */
    public Date getTimestamp() {
        return timestamp;
    }

    /**
     * Sets the pet's gender.
     * @param timestamp the new timestamp
     */
    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * Finds pets by name.
     * @param name The pet's name
     * @return A list containing found pets, sorted alphabetically
     */
    public static List<Pet> findByName(String name) {
        return find.where()
                   .ilike(NAME, "%" + name + "%")
                   .orderBy(NAME)
                   .findList();
    }

    /**
     * Finds pets by type.
     * @param type The pet's type (i.e. dog, cat, ...)
     * @return A list containing found pets, sorted by date from most to least recent
     */
    public static List<Pet> findByType(String type) {
        return find.where()
                   .ilike(TYPE, type)
                   .orderBy(TIMESTAMP + " " + DESC)
                   .findList();
    }

    /**
     * Finds pets by type.
     * @param type   The pet's type (i.e. dog, cat, ...)
     * @param gender The pet's gender (i.e. male, female)
     * @return A list containing found pets, sorted by date from most to least recent
     */
    public static List<Pet> findByTypeAndGender(String type, String gender) {
        return find.where()
                   .and(ilike(TYPE, type),
                        ilike(GENDER, gender))
                   .orderBy(TIMESTAMP + " " + DESC)
                   .findList();
    }
}
