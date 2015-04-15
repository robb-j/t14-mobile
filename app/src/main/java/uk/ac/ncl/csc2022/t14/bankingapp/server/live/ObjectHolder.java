package uk.ac.ncl.csc2022.t14.bankingapp.server.live;

import android.support.annotation.Nullable;

/**
 * A simple object that holders another of a given type
 * Allows for something to be passed back to the calling function without a return
 * Created by Rob A on 15/04/15.
 */
public class ObjectHolder<T> {

    // The object being stored
    private T heldValue;


    /**
     * Creates an object holder with an initial value
     * @param object The inital value
     */
    public ObjectHolder(T object) {

        setValue(object);
    }



    /**
     * Creates an empty Object Holder
     */
    public ObjectHolder() {

    }



    /**
     * Get the value stored by this holder
     * @return An object of the generic type, can be null
     */
    @Nullable
    public T getValue() {

        return heldValue;
    }

    /**
     * Set the value stored
     */
    public void setValue(T newValue) {

        heldValue = newValue;
    }
}
