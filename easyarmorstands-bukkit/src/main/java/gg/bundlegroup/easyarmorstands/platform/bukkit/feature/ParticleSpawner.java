package gg.bundlegroup.easyarmorstands.platform.bukkit.feature;

import net.kyori.adventure.util.RGBLike;
import org.bukkit.entity.Player;

public interface ParticleSpawner {
    Object getData(RGBLike color);

    void spawnParticle(Player player, double x, double y, double z, Object data);

    interface Provider extends FeatureProvider<ParticleSpawner> {
    }
}
