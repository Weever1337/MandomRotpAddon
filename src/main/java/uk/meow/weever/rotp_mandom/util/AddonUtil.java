package uk.meow.weever.rotp_mandom.util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import uk.meow.weever.rotp_mandom.MandomAddon;
import uk.meow.weever.rotp_mandom.data.world.BlockData;

import javax.annotation.Nullable;

import com.github.standobyte.jojo.client.standskin.StandSkinsManager;
import com.github.standobyte.jojo.power.impl.stand.IStandPower;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class AddonUtil {
    private static final ResourceLocation MANDOM_SHADER = new ResourceLocation(MandomAddon.MOD_ID, "shaders/post/mandom.json");
    
    public static Vector3d convertFromBlockPos(BlockPos blockPos) { // what a hell
        return new Vector3d(blockPos.getX(), blockPos.getY(), blockPos.getZ());
    }

    public static <T extends Entity> List<T> entitiesAround(Class<? extends T> clazz, World level, Vector3d center, double radius, @Nullable Predicate<? super T> filter) {
        AxisAlignedBB aabb = new AxisAlignedBB(center.subtract(radius, radius, radius), center.add(radius, radius, radius));
        return level.getEntitiesOfClass(clazz, aabb, filter);
    }

    public static List<BlockData> getNearbyBlocks(PlayerEntity player, List<BlockData> blockDataList, double radius) {
        Vector3d playerPos = player.position();

        return blockDataList.stream()
                .filter(blockData -> blockData != null && blockData.pos != null)
                .filter(blockData -> blockData.pos.distSqr(playerPos.x, playerPos.y, playerPos.z, true) <= radius * radius)
                .collect(Collectors.toList());
    }

    public static ResourceLocation getShader(IStandPower power) {
        ResourceLocation texture = StandSkinsManager.getInstance().getRemappedResPath(manager -> manager
                .getStandSkin(power.getStandInstance().get()), MANDOM_SHADER);
        return texture;
    }
}
