package uk.ac.ncl.csc2022.t14.bankingapp.models;

/**
 * Created by Jack on 13/02/2015.
 */

/**
 * Class used to represent a product to be offered to a user
 */
public class Product extends ModelObject {

    private String title;
    private String content;

    public Product(int id, String title, String content) {
        super(id);
        this.title = title;
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}
