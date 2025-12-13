package btw.community.betteritemframemaps.mixin;

import btw.community.betteritemframemaps.BFMConfig;
import net.minecraft.src.*;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RenderItemFrame.class)
public class RenderItemFrameMixin {
    // Depth offset: use 0.495f (slightly in front of 0.5f) to avoid Z-fighting
    private static final float WALL_OFFSET = 0.495F;

    // Scale: 128px map == 1.0×1.0 block (128 px per unit)
    private static final float MAP_SCALE = 1.0F / 128.0F;

    // Center offset: translate -64 so map center (64,64) -> GL (0,0)
    private static final float MAP_CENTER_OFFSET = -64.0F;

    /**
     * Preventing the rendering frame for map.
     */
    @Inject(method = "renderFrameItemAsBlock", at = @At("HEAD"), cancellable = true)
    private void onRenderFrameItemAsBlock(EntityItemFrame entity, CallbackInfo ci) {
        ItemStack stack = entity.getDisplayedItem();
        if (stack != null && stack.itemID == Item.map.itemID) {
            ci.cancel();
        }
    }

    @Inject(method = "func_82402_b", at = @At("HEAD"), cancellable = true)
    private void onRenderItemInFrame(EntityItemFrame entity, CallbackInfo ci) {
        ItemStack stack = entity.getDisplayedItem();

        if (stack != null && stack.itemID == Item.map.itemID) {
            ci.cancel();

            MapData mapData = ((ItemMap) stack.getItem()).getMapData(stack, entity.worldObj);

            if (mapData != null) {
                this.renderSeamlessMap(entity, mapData);
            }
        }
    }

    /**
     * Method responsible for drawing the map on a full block.
     */
    private void renderSeamlessMap(EntityItemFrame entity, MapData mapData) {
        GL11.glPushMatrix();

        GL11.glRotatef(entity.rotationYaw, 0.0F, 1.0F, 0.0F);

        GL11.glTranslatef(0.0F, 0.0F, WALL_OFFSET);

        if (BFMConfig.enableRotation) {
            GL11.glRotatef((float)(-90 * entity.getRotation()), 0.0F, 0.0F, 1.0F);
        }

        GL11.glRotatef(180.0F, 0.0F, 0.0F, 1.0F);

        GL11.glScalef(MAP_SCALE, MAP_SCALE, MAP_SCALE);

        GL11.glTranslatef(MAP_CENTER_OFFSET, MAP_CENTER_OFFSET, 0.0F);

        if (RenderManager.instance.itemRenderer.mapItemRenderer != null) {
            RenderManager.instance.itemRenderer.mapItemRenderer.renderMap(
                    null,
                    RenderManager.instance.renderEngine,
                    mapData
            );
        }

        GL11.glPopMatrix();
    }
}