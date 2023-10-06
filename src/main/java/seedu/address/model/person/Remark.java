package seedu.address.model.person;

import static java.util.Objects.requireNonNull;

/**
 * Represents a Person's remark in the address book.
 * Guarantees: immutable; is always valid
 */
public class Remark {
    /**
     * The value of the remark.
     */
    public final String value;

    /**
     * Constructs a `Remark` object with the specified string value.
     *
     * @param remark The string value of the remark. Must not be null.
     * @throws NullPointerException if `remark` is null.
     */
    public Remark(String remark) {
        requireNonNull(remark);
        value = remark;
    }

    /**
     * Returns the string representation of the `Remark` object.
     *
     * @return The string value of the `Remark` object.
     */
    @Override
    public String toString() {
        return value;
    }

    /**
     * Compares this `Remark` object with another object for equality.
     * Two `Remark` objects are considered equal if their string values are
     * equal.
     *
     * @param other The object to compare to.
     * @return `true` if the objects are equal, `false` otherwise.
     */
    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Remark // instanceof handles nulls
                && value.equals(((Remark) other).value)); // state check
    }

    /**
     * Returns the hash code of the `Remark` object. The hash code is
     * based on the hash code of its string value.
     *
     * @return The hash code of the `Remark` object.
     */
    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
