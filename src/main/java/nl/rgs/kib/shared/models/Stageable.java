package nl.rgs.kib.shared.models;

/**
 * Interface for objects that have a stage field as an integer.
 **/
public interface Stageable {
    Integer getStage();

    void setStage(Integer stage);
}
