package nl.rgs.kib.shared.models;

/**
 * Interface for objects that have an index field as an integer.
 * This is used for objects that are part of a list.
 * The index field is used to determine the order of the objects in the list.
 * The index field should be unique within the list.
 * The index field should be zero-based.
 * The index field should be a positive integer.
 */
public interface Indexable {
    Integer getIndex();

    void setIndex(Integer index);
}
