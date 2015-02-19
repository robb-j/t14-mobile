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

    /**
     * Creates a new Product model
     * @param id The unique identifier for this Product
     * @param title The title of the Product
     * @param content Some more information about the Product, in html form
     */
    public Product(int id, String title, String content) {
        super(id);
        setTitle(title);
        setContent(content);
    }

    public String getTitle() {
        return title;
    }

    private void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    private void setContent(String content) {
        this.content = content;
    }

}
