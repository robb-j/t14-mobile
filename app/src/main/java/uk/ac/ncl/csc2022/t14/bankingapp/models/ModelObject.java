package uk.ac.ncl.csc2022.t14.bankingapp.models;

/**
 * Created by Jack on 13/02/2015.
 */

/**
 * Superclass for each model to inherit from
 * */
public abstract class ModelObject {

    private int id;

    /**
     * Creates a new ModelObject, should only be called
     * in the use of super(id) from a subclass
     * @param id The unique identifier for this object
     */
    public ModelObject(int id) {
        setId(id);
    }

    public ModelObject() {};

    private void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
