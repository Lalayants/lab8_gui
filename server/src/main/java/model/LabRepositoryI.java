package model;

public interface LabRepositoryI {
    /**
     * Adds a lab work to the database.
     *
     * @param `entity.LabWorkDTO` - the lab work to be added
     */
    <LabWorkDTO> void add(LabWorkDTO a);
    void remove(int id);


}
