package me.catalysmrl.catamines.utils.helper;

import org.bukkit.Material;

import java.util.List;
import java.util.stream.Stream;

public class BlockUtil {

    private BlockUtil() {
    }

    public static List<Material> getAllPlaceableBlocks() {
        return Stream.of(Material.values())
                .filter(Material::isBlock)
                .toList();
    }

    public static List<String> getAllPlaceableBlockNames() {
        return getAllPlaceableBlocks().stream()
                .map(Material::name)
                .toList();
    }
}
