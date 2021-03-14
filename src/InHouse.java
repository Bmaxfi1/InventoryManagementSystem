/**
 * The InHouse class is a subclass of Part.  It may be instantiated.
 */

public class InHouse extends Part{

    private int machineId;

    /**
     *
     * @param id the id of the part
     * @param name the name of the part
     * @param price the price or cost of the part
     * @param stock the inventory or stock of the part
     * @param min the minimum allowable inventory
     * @param max the maximum allowable inventory
     * @param machineId the machineId specific to an InHouse part
     */
    public InHouse(int id, String name, double price, int stock, int min, int max, int machineId) {
        super(id, name, price, stock, min, max);
        this.machineId = machineId;
    }

    /**
     *
     * @param machineId the machineId to set
     */
    public void setMachineId(int machineId) {
        this.machineId = machineId;
    }

    /**
     *
     * @return the machineId
     */
    public int getMachineId() {
        return machineId;
    }
}
