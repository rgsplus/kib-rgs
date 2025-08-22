package nl.rgs.kib.shared.models;

/**
 * Interface for objects with sortable properties in the domain model.
 * <p>
 * This interface should be implemented by domain objects that need to maintain
 * internal ordering of their properties (like collections or hierarchical data).
 * It provides a standard method to organize object contents according to predefined
 * sorting criteria.
 * <p>
 * Implementing classes should define the specific sorting logic in the {@code applySort()}
 * method, typically by sorting any internal collections or comparable properties.
 * <p>
 * Common use cases include:
 * <ul>
 *   <li>Sorting child elements within a parent object</li>
 *   <li>Maintaining ordered collections when loading from storage</li>
 *   <li>Preparing ordered data for presentation</li>
 * </ul>
 *
 * @see Comparable
 * @see java.util.Collections#sort(java.util.List)
 */
public interface Sortable {

    /**
     * Applies the sort logic to all applicable properties of the implementing object.
     * <p>
     * This method should sort any internal collections or fields that implement
     * {@link Comparable}, ensuring consistent ordering of object data. Implementation
     * details depend on the specific requirements of each implementing class.
     */
    void applySort();
}