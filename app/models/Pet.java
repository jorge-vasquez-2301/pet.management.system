package models;


import com.avaje.ebean.Model;
import play.data.validation.Constraints;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Arrays;
import java.util.Optional;

/**
 * The Pet class.
 * This class models the information about a pet.
 */
@Entity
public class Pet extends Model {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Constraints.Required
    public String type;

    @Constraints.Required
    public String name;

    @Constraints.Required
    public String gender;

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
