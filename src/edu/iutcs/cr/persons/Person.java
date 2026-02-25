package edu.iutcs.cr.persons;

import edu.iutcs.cr.util.InputReader;

import java.io.Serializable;
import java.util.Objects;

/**
 * @author Raian Rahman
 * @since 4/18/2024
 */
public class Person implements Serializable {

    private String name;
    private String id;
    private String email;

    public Person() {
        readName();
        readId();
        readEmail();
    }

    public Person(String id) {
        this.id = id;
    }

    public String getName()  { return name; }
    public String getId()    { return id;   }
    public String getEmail() { return email; }

    public void setName(String name)   { this.name  = name;  }
    public void setId(String id)       { this.id    = id;    }
    public void setEmail(String email) { this.email = email; }

    private void readName() {
        InputReader reader = InputReader.getInstance();
        while (this.name == null || this.name.isBlank()) {
            System.out.print("Enter name: ");
            this.name = reader.nextLine();
            if (name.isBlank()) System.out.println("Name is mandatory!");
        }
    }

    private void readId() {
        InputReader reader = InputReader.getInstance();
        while (this.id == null || this.id.isBlank()) {
            System.out.print("Enter id: ");
            this.id = reader.nextLine();
            if (id.isBlank()) System.out.println("Id is mandatory!");
        }
    }

    private void readEmail() {
        InputReader reader = InputReader.getInstance();
        while (this.email == null || this.email.isBlank()) {
            System.out.print("Enter email: ");
            this.email = reader.nextLine();
            if (email.isBlank()) System.out.println("Email is mandatory!");
        }
    }

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
