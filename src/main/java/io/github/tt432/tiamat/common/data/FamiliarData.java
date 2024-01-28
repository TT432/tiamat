package io.github.tt432.tiamat.common.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * @author TT432
 */
public record FamiliarData(
        FamiliarType familiarType,
        List<DisplayEntry> display
) {

    public static final Codec<FamiliarData> CODEC = RecordCodecBuilder.create(ins -> ins.group(
            FamiliarType.CODEC.fieldOf("familiar_type").forGetter(o -> o.familiarType),
            DisplayEntry.CODEC.listOf().fieldOf("display").forGetter(o -> o.display)
    ).apply(ins, FamiliarData::new));

    public record DisplayEntry(
            String name,
            String models,
            String render_type,
            List<ResourceLocation> textures,
            String animations,
            String animationControllers
    ) {
        public static final Codec<DisplayEntry> CODEC = RecordCodecBuilder.create(ins -> ins.group(
                Codec.STRING.fieldOf("name").forGetter(o -> o.name),
                Codec.STRING.fieldOf("models").forGetter(o -> o.models),
                Codec.STRING.fieldOf("render_type").forGetter(o -> o.render_type),
                ResourceLocation.CODEC.listOf().fieldOf("textures").forGetter(o -> o.textures),
                Codec.STRING.fieldOf("animations").forGetter(o -> o.animations),
                Codec.STRING.fieldOf("animation_controllers").forGetter(o -> o.animationControllers)
        ).apply(ins, DisplayEntry::new));
    }

    public enum FamiliarType implements StringRepresentable {
        GROUND, FLIGHT, EQUIPMENT;

        public static final Codec<FamiliarType> CODEC = StringRepresentable.fromEnum(FamiliarType::values);

        @Override
        @NotNull
        public String getSerializedName() {
            return name();
        }
    }
}
