package edu.iutcs.cr.persons;

import edu.iutcs.cr.util.InputReader;

import java.io.Serializable;
import java.util.Objects;

/**
 * @author Raian Rahman
 * @since 4/18/2024
 *
 * <p><strong>Refactoring notes:</strong>
 * <ul>
 *   <li>Removed per-method {@code new Scanner(System.in)} and replaced with
 *       the shared {@link InputReader} singleton (see that class for rationale).</li>
 *   <li>Renamed no-arg I/O methods {@code setName()}, {@code setId()}, {@code setEmail()}
 *       to {@code readName()}, {@code readId()}, {@code readEmail()}.
 *       Java convention dictates that {@code setX()} accepts a value – methods that
 *       <em>prompt the console and then assign</em> are a different responsibility
 *       and deserve a distinct name (Single Responsibility Principle).</li>
 *   <li>Added proper value-based setters {@code setName(String)}, {@code setId(String)},
 *       {@code setEmail(String)} so the class can be used without console interaction.</li>
 * </ul>
 */
public class Person implements Serializable {

    private String name;
    private String id;
    private String email;

    /** Full constructor: prompts the console for each field. */
    public Person() {
        readName();
        readId();
        readEmail();
    }

    /** Lookup constructor: creates a partial Person used only for equality checks. */
    public Person(String id) {
        this.id = id;
    }

    // -------------------------------------------------------------------------
    // Getters
    // -------------------------------------------------------------------------

    public String getName()  { return name; }
    public String getId()    { return id;   }
    public String getEmail() { return email; }

    // -------------------------------------------------------------------------
    // Value-based setters (programmatic use – no console I/O)
    // -------------------------------------------------------------------------

    public void setName(String name)   { this.name  = name;  }
    public void setId(String id)       { this.id    = id;    }
    public void setEmail(String email) { this.email = email; }

    // -------------------------------------------------------------------------
    // Console-reading helpers (called by constructors)
    // -------------------------------------------------------------------------

    /** Prompts the user until a non-blank name is entered. */
    private void readName() {
        InputReader reader = InputReader.getInstance();
        while (this.name == null || this.name.isBlank()) {
            System.out.print("Enter name: ");
            this.name = reader.nextLine();
            if (name.isBlank()) System.out.println("Name is mandatory!");
        }
    }

    /** Prompts the user until a non-blank id is entered. */
    private void readId() {
        InputReader reader = InputReader.getInstance();
        while (this.id == null || this.id.isBlank()) {
            System.out.print("Enter id: ");
            this.id = reader.nextLine();
            if (id.isBlank()) System.out.println("Id is mandatory!");
        }
    }

    /** Prompts the user until a non-blank email is entered. */
    private void readEmail() {
        InputReader reader = InputReader.getInstance();
        while (this.email == null || this.email.isBlank()) {
            System.out.print("Enter email: ");
            this.email = reader.nextLine();
            if (email.isBlank()) System.out.println("Email is mandatory!");
        }
    }

    // -------------------------------------------------------------------------
    // Object overrides
    // -------------------------------------------------------------------------

    @Override
    public String toString() {
        return "name='" + name + '\'' +
                ", id='" + id + '\'' +
                ", email='" + email + '\'';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Person person)) return false;
        return Objects.equals(id, person.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
