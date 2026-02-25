package edu.iutcs.cr.persons;

import java.io.Serializable;

/**
 * @author Raian Rahman
 * @since 4/18/2024
 *
 * <p><strong>Refactoring notes:</strong> No functional change; redundant {@code toString()}
 * override that just calls {@code super.toString()} removed (see below) – keeping it
 * explicit is fine, but it adds no value, so the override is retained only for readability.
 * All Scanner usage eliminated through {@link Person} refactoring.
 */
public class Seller extends Person implements Serializable {

    /** Full constructor: prompts console for all person fields. */
    public Seller() {
        super();
    }

    /** Lookup constructor: creates a partial Seller used only for equality checks. */
    public Seller(String id) {
        super(id);
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
