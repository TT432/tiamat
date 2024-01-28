package io.github.tt432.tiamat.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import io.github.tt432.eyelib.capability.AnimatableCapability;
import io.github.tt432.eyelib.capability.EyelibCapabilities;
import io.github.tt432.eyelib.client.animation.bedrock.BrAnimation;
import io.github.tt432.eyelib.client.animation.component.AnimationControllerComponent;
import io.github.tt432.eyelib.client.loader.BrAnimationControllerLoader;
import io.github.tt432.eyelib.client.loader.BrAnimationLoader;
import io.github.tt432.eyelib.client.loader.BrModelLoader;
import io.github.tt432.eyelib.client.model.bedrock.BrModel;
import io.github.tt432.eyelib.client.render.renderer.BrModelRenderer;
import io.github.tt432.eyelib.client.render.visitor.BlankEntityModelRenderVisit;
import io.github.tt432.tiamat.common.data.FamiliarData;
import io.github.tt432.tiamat.common.entity.FamiliarEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

import java.util.List;
import java.util.Objects;

/**
 * @author TT432
 */
public class FamiliarEntityRenderer extends EntityRenderer<FamiliarEntity> {
    private static final BlankEntityModelRenderVisit visitor = new BlankEntityModelRenderVisit();

    protected FamiliarEntityRenderer(EntityRendererProvider.Context pContext) {
        super(pContext);
    }

    @Override
    public void render(FamiliarEntity pEntity, float pEntityYaw, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight) {
        AnimatableCapability<Object> data = pEntity.getData(EyelibCapabilities.ANIMATABLE);

        // TODO animation controller

        var familiarData = pEntity.getFamiliarData();

        if (familiarData == null) return;

        List<FamiliarData.DisplayEntry> display = familiarData.display();

        if (display.isEmpty()) return;

        FamiliarData.DisplayEntry currentDisplayData = display.get(0);

        AnimationControllerComponent animationControllerComponent = data.getAnimationControllerComponent();
        BrAnimation targetAnimations = BrAnimationLoader.getAnimation(new ResourceLocation(currentDisplayData.animations()));

        BrModel model = BrModelLoader.getModel(new ResourceLocation(currentDisplayData.models()));

        if (model == null) return;

        if (targetAnimations != null && !Objects.equals(animationControllerComponent.getTargetAnimation(), targetAnimations)) {
            data.getModelComponent().setModel(model);

            animationControllerComponent.setup(
                    BrAnimationControllerLoader.getController(new ResourceLocation(currentDisplayData.animationControllers())),
                    targetAnimations);
        }

        VertexConsumer buffer = switch (currentDisplayData.render_type()) {
            case "cutout" -> pBuffer.getBuffer(RenderType.entityCutout(getTextureLocation(pEntity)));
            default -> pBuffer.getBuffer(RenderType.entitySolid(getTextureLocation(pEntity)));
        };

        visitor.setLight(pPackedLight);

        BrModelRenderer.render(model, data.getModelComponent().getInfos(), pPoseStack, buffer, visitor);

        super.render(pEntity, pEntityYaw, pPartialTick, pPoseStack, pBuffer, pPackedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(FamiliarEntity pEntity) {
        var familiarData = pEntity.getFamiliarData();

        FamiliarData.DisplayEntry currentDisplayData = familiarData.display().get(0);

        ResourceLocation currentTexture = currentDisplayData.textures().get(0);
        return new ResourceLocation(currentTexture.getNamespace(), "textures/" + currentTexture.getPath() + ".png");
    }
}
