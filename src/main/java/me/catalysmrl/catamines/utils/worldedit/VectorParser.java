package me.catalysmrl.catamines.utils.worldedit;

import com.sk89q.worldedit.math.BlockVector2;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.math.Vector2;
import com.sk89q.worldedit.math.Vector3;

public class VectorParser {

    private VectorParser() {
    }

    public static Vector2 asVector2(String input) {
        String[] parts = input.split(",");

        if (parts.length != 2) throw new IllegalArgumentException("Illegal argument size for Vector2");

        double x = Double.parseDouble(parts[0].trim());
        double y = Double.parseDouble(parts[1].trim());

        return Vector2.at(x, y);
    }

    public static Vector3 asVector3(String input) {
        String[] parts = input.split(",");

        if (parts.length != 3) throw new IllegalArgumentException("Illegal argument size for Vector2");

        double x = Double.parseDouble(parts[0].trim());
        double y = Double.parseDouble(parts[1].trim());
        double z = Double.parseDouble(parts[2].trim());

        return Vector3.at(x, y, z);
    }

    public static BlockVector2 asBlockVector2(String input) {
        return asVector2(input).toBlockPoint();
    }

    public static BlockVector3 asBlockVector3(String input) {
        return asVector3(input).toBlockPoint();
    }

}
