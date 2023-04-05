package objects;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

class MaterialTest {

    @Test
    void get() {
        assertEquals(77, Material.AIR.getId());
        assertEquals(456, Material.BRICK.getId());
        assertEquals(44, Material.MIRROR.getId());
        assertEquals(143, Material.LASER.getId());
        assertEquals(81, Material.LAMP.getId());
    }

    @ParameterizedTest
    @EnumSource(Material.class)
    void getMaterial(Material material) {
        assertEquals(material, Material.getMaterial(material.getId()));
    }

    @Test
    void unknownId() {
        assertEquals(Material.AIR, Material.getMaterial(-1));
    }
}