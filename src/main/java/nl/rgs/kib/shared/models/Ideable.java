package nl.rgs.kib.shared.models;

/**
 * Interface for entities requiring a string identifier.
 * <p>
 * This interface should be implemented by domain objects that need a unique
 * string identifier within the system. Implementing classes must provide
 * methods to get and set the ID value.
 * <p>
 * The ID is typically used for:
 * <ul>
 *   <li>Primary identification</li>
 *   <li>Object reference and retrieval</li>
 *   <li>Maintaining relationships between entities</li>
 * </ul>
 * <p>
 * When implementing this interface, ensure the ID remains unique within
 * the scope of the object type to maintain data integrity.
 *
 * @see nl.rgs.kib.model.list.InspectionListItem
 */
public interface Ideable {
    String getId();

    void setId(String id);
}
