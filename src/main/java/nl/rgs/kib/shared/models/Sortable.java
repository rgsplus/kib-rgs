package nl.rgs.kib.shared.models;

/**
 * Interface for sorting domain object properties.
 */
public interface Sortable {

    /**
     * Applies the sort logic to all properties that implements Comparable.
     */
    void applySort();
}