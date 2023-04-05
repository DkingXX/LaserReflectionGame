package objects;


/**
 * Material type for identifying tiles.
 */
@SuppressWarnings("PMD.DataflowAnomalyAnalysis")
public enum Material {
    AIR(77), BRICK(456), MIRROR(44), LASER(143), LAMP(81), BOMB(78);

    // tile id of the material.
    private int id;

    Material(int id) {
        this.id = id;
    }

    /**
     * Gets the tile id for the material.
     * @return the tile id
     */
    public int getId() {
        return id;
    }

    /**
     * Gets the material associated with a given id.
     * @param id the id of the material.
     * @return the material or AIR if it is unknown.
     */
    public static Material getMaterial(int id) {
        for (Material mat : Material.values()) {
            if (id == mat.getId()) {
                return mat;
            }
        }

        return Material.AIR;
    }
}
