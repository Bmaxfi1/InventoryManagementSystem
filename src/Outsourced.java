/**
 * The Outsourced class is a subclass of Part.  It may be instantiated.
 */

public class Outsourced extends Part {

    private String companyName;

    /**
     *
     * @param id the part id
     * @param name the name of the part
     * @param price the price or cost of the part
     * @param stock the stock or inventory of the part
     * @param min the minimum acceptable quantity in stock
     * @param max the maximum acceptable quantity in stock
     * @param companyName the name of the company the part comes from
     */
    public Outsourced(int id, String name, double price, int stock, int min, int max, String companyName){
        super(id, name, price, stock, min, max);
        this.companyName = companyName;
    };

    /**
     *
     * @param companyName the name to set of the company the part comes from
     */
    public void setCompanyName(String companyName){

        this.companyName = companyName;
    }

    /**
     *
     * @return the name of the company the part comes from
     */
    public String getCompanyName() {

        return companyName;
    }
}
