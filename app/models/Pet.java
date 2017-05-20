package models;


import com.avaje.ebean.Model;
import play.data.validation.Constraints;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

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
    public Long id;

    @Constraints.Required
    public String type;

    @Constraints.Required
    public String name;

    @Constraints.Required
    public String gender;

    @Constraints.Required
    public Date timestamp;

    private static Finder<Long, Pet> find = new Finder<>(Pet.class);

    /**
     * Creates a new instance of Pet.
     * @param type   The pet's type (i.e. dog, cat, ...)
     * @param name   The pet's name
     * @param gender The pet's gender (i.e. male, female)
     */
    public Pet(String type, String name, String gender) {
        this.type = type;
        this.name = name;
        this.gender = gender;
        timestamp = new Date();
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

    /**
     * The Gender enum.
     * This models the possible values for a pet's gender.
     */
    public enum Gender {
        MALE    ("male"),
        FEMALE  ("female");

        private String value;

        /**
         * Constructor for Gender.
         * @param value Gender value: male/female
         */
        Gender(String value) {
            this.value = value;
        }

        /**
         * @return the value
         */
        public String getValue() {
            return value;
        }

        /**
         * Returns a Gender for the given value.
         * @param value Gender value: male/female
         * @return the corresponding Gender
         */
        public static Optional<Gender> getGender(String value) {
            return Arrays.stream(Gender.values()).filter(gender -> gender.getValue().equals(value)).findFirst();
        }
    }
}
