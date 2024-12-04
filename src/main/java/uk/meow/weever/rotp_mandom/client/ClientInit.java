package uk.meow.weever.rotp_mandom.client;

import com.github.standobyte.jojo.client.render.entity.layerrenderer.HamonBurnLayer;
import com.github.standobyte.jojo.client.ui.standstats.StandStatsRenderer;
import com.github.standobyte.jojo.client.ui.standstats.StandStatsRenderer.StandStat;
import com.github.standobyte.jojo.power.impl.stand.IStandPower;
import com.github.standobyte.jojo.power.impl.stand.stats.StandStats;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.client.renderer.entity.PlayerRenderer;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.entity.LivingEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import org.jetbrains.annotations.NotNull;
import uk.meow.weever.rotp_mandom.MandomAddon;
import uk.meow.weever.rotp_mandom.client.render.MandomLayerRenderer;
import uk.meow.weever.rotp_mandom.client.render.MandomRenderer;
import uk.meow.weever.rotp_mandom.client.render.item.RingoLayerModel;
import uk.meow.weever.rotp_mandom.client.render.item.RingoLayerRenderer;
import uk.meow.weever.rotp_mandom.init.InitStands;

import java.util.Map;

@EventBusSubscriber(modid = MandomAddon.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientInit {
    @SubscribeEvent
    public static void onFMLClientSetup(FMLClientSetupEvent event) {
        Minecraft mc = event.getMinecraftSupplier().get();
        Map<String, PlayerRenderer> skinMap = mc.getEntityRenderDispatcher().getSkinMap();

        RenderingRegistry.registerEntityRenderingHandler(
                InitStands.MANDOM.getEntityType(), MandomRenderer::new
        );

        StandStatsRenderer.overrideCosmeticStats(
                InitStands.MANDOM.getStandType().getRegistryName(),
            new StandStatsRenderer.ICosmeticStandStats() {
                @Override public double statConvertedValue(StandStat stat, IStandPower standData, StandStats stats, float statLeveling) {
                    switch (stat) {
                    case RANGE:
                        return 0;
                    case DEV_POTENTIAL:
                        return 3;
                    default:
                        return StandStatsRenderer.ICosmeticStandStats.super.statConvertedValue(stat, standData, stats, statLeveling);
                    }
                }
            });

        event.enqueueWork(() -> {
            ClientEventHandler.init(mc);
            addLayers(skinMap.get("default"), false);
            addLayers(skinMap.get("slim"), true);
            mc.getEntityRenderDispatcher().renderers.values().forEach(ClientInit::addLayersToEntities);
        });
    }


    private static void addLayers(PlayerRenderer renderer, boolean slim) {
        renderer.addLayer(new MandomLayerRenderer<>(renderer));
        renderer.addLayer(new RingoLayerRenderer<>(renderer, new RingoLayerModel<>(0.3f, true)));
        addLivingLayers(renderer);
        addBipedLayers(renderer);
    }

    private static <T extends LivingEntity, M extends BipedModel<T>> void addLayersToEntities(EntityRenderer<?> renderer) {
        if (renderer instanceof LivingRenderer<?, ?>) {
            addLivingLayers((LivingRenderer<T, ?>) renderer);
            if (((LivingRenderer<?, ?>) renderer).getModel() instanceof BipedModel<?>) {
                addBipedLayers((LivingRenderer<T, M>) renderer);
            }
        }
    }

    private static <T extends LivingEntity, M extends EntityModel<T>> void addLivingLayers(@NotNull LivingRenderer<T, M> renderer) {
        renderer.addLayer(new HamonBurnLayer<>(renderer));
    }

    private static <T extends LivingEntity, M extends BipedModel<T>> void addBipedLayers(LivingRenderer<T, M> renderer) {
    }
}