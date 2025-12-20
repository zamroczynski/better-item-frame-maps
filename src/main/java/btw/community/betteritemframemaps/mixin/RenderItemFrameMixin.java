package btw.community.betteritemframemaps.mixin;

import btw.community.betteritemframemaps.BetterItemFrameMapsAddon;
import net.minecraft.src.*;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RenderItemFrame.class)
public class RenderItemFrameMixin {

    // Depth offset: 0.495f to avoid Z-fighting (slightly in front of the block)
    private static final float WALL_OFFSET = 0.495F;
    // Scale: 128px map == 1.0x1.0 block (128 px per unit)
    private static final float MAP_SCALE = 1.0F / 128.0F;
    // Center offset: translate -64 so map center (64,64) -> GL (0,0)
    private static final float MAP_CENTER_OFFSET = -64.0F;

    /**
     * Preventing the rendering of the wood frame for maps.
     */
    @Inject(method = "renderFrameItemAsBlock", at = @At("HEAD"), cancellable = true)
    private void onRenderFrameItemAsBlock(EntityItemFrame entity, CallbackInfo ci) {
        ItemStack stack = entity.getDisplayedItem();
        if (stack != null && stack.itemID == Item.map.itemID) {
            // BetterItemFrameMapsAddon.debug("Cancelling wood frame render for EntityID: " + entity.entityId);
            ci.cancel();
        }
    }

    /**
     * Replacing the rendering of the item inside the frame (the map content).
     */
    @Inject(method = "func_82402_b", at = @At("HEAD"), cancellable = true)
    private void onRenderItemInFrame(EntityItemFrame entity, CallbackInfo ci) {
        ItemStack stack = entity.getDisplayedItem();

        if (stack != null && stack.itemID == Item.map.itemID) {
            ci.cancel();

            try {
                if (stack.getItem() instanceof ItemMap) {
                    MapData mapData = ((ItemMap) stack.getItem()).getMapData(stack, entity.worldObj);

                    if (mapData != null) {
                        this.renderSeamlessMap(entity, mapData);
                    } else {
                        BetterItemFrameMapsAddon.debug("Missing MapData for Item Frame EntityID: " + entity.entityId + " | Item Damage: " + stack.getItemDamage());
                    }
                } else {
                    BetterItemFrameMapsAddon.logWarning("Item is ID of map, but not instanceof ItemMap! ID: " + stack.itemID);
                }
            } catch (Exception e) {
                BetterItemFrameMapsAddon.logWarning("Exception in onRenderItemInFrame for EntityID: " + entity.entityId);
                e.printStackTrace();
            }
        }
    }

    /**
     * Custom method responsible for drawing the map on a full block.
     */
    private void renderSeamlessMap(EntityItemFrame entity, MapData mapData) {
        try {
            GL11.glPushMatrix();

            GL11.glRotatef(entity.rotationYaw, 0.0F, 1.0F, 0.0F);
            GL11.glTranslatef(0.0F, 0.0F, WALL_OFFSET);

            if (BetterItemFrameMapsAddon.enableRotation) {
                GL11.glRotatef((float)(-90 * entity.getRotation()), 0.0F, 0.0F, 1.0F);
            }

            GL11.glRotatef(180.0F, 0.0F, 0.0F, 1.0F);

            GL11.glScalef(MAP_SCALE, MAP_SCALE, MAP_SCALE);

            GL11.glTranslatef(MAP_CENTER_OFFSET, MAP_CENTER_OFFSET, 0.0F);

            RenderManager rm = RenderManager.instance;

            if (rm != null && rm.itemRenderer != null && rm.itemRenderer.mapItemRenderer != null && rm.renderEngine != null) {
                rm.itemRenderer.mapItemRenderer.renderMap(
                        null, // Player null, bo renderujemy w świecie
                        rm.renderEngine,
                        mapData
                );
            } else {
                BetterItemFrameMapsAddon.logWarning("Critical: RenderManager or sub-components are null. Cannot render map for EntityID: " + entity.entityId);
            }

            GL11.glPopMatrix();
        } catch (Exception e) {
            BetterItemFrameMapsAddon.logWarning("OpenGL Error in renderSeamlessMap for EntityID: " + entity.entityId);
            e.printStackTrace();
            try { GL11.glPopMatrix(); } catch (Exception ignored) {}
        }
    }
}