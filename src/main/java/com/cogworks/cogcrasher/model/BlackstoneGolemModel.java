package com.cogworks.cogcrasher.model;

import com.cogworks.cogcrasher.entity.BlackstoneGolemEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;

public class BlackstoneGolemModel<T extends BlackstoneGolemEntity> extends HierarchicalModel<T> {

	private final ModelPart root;
	private final ModelPart body;
	private final ModelPart rightleg;
	private final ModelPart leftleg;
	private final ModelPart righthind;
	private final ModelPart lefthind;

	public BlackstoneGolemModel(ModelPart root) {
		this.root = root;
		this.body = root.getChild("body");
		this.rightleg = this.body.getChild("rightleg");
		this.leftleg = this.body.getChild("leftleg");
		this.righthind = this.body.getChild("righthind");
		this.lefthind = this.body.getChild("lefthind");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition body = partdefinition.addOrReplaceChild(
				"body",
				CubeListBuilder.create()
						.texOffs(0, 0)
						.addBox(-24.0F, -24.0F, -24.0F, 48.0F, 48.0F, 48.0F, new CubeDeformation(0.0F))
						.texOffs(0, 96)
						.addBox(-24.0F, -24.0F, -24.0F, 48.0F, 48.0F, 48.0F, new CubeDeformation(0.05F)),
				PartPose.offset(0.0F, -10.0F, 0.0F)
		);

		body.addOrReplaceChild(
				"rightleg",
				CubeListBuilder.create()
						.texOffs(0, 192)
						.addBox(-15.5F, 0.0F, -7.5F, 9.0F, 10.0F, 9.0F, new CubeDeformation(0.0F)),
				PartPose.offset(0.0F, 24.0F, -15.0F)
		);

		body.addOrReplaceChild(
				"leftleg",
				CubeListBuilder.create()
						.texOffs(0, 192)
						.addBox(6.5F, 0.0F, -7.5F, 9.0F, 10.0F, 9.0F, new CubeDeformation(0.0F)),
				PartPose.offset(0.0F, 24.0F, -15.0F)
		);

		body.addOrReplaceChild(
				"righthind",
				CubeListBuilder.create()
						.texOffs(0, 192)
						.addBox(-15.5F, -1.0F, -1.5F, 9.0F, 10.0F, 9.0F, new CubeDeformation(0.0F)),
				PartPose.offset(0.0F, 25.0F, 15.0F)
		);

		body.addOrReplaceChild(
				"lefthind",
				CubeListBuilder.create()
						.texOffs(0, 192)
						.addBox(6.5F, -1.0F, -1.5F, 9.0F, 10.0F, 9.0F, new CubeDeformation(0.0F)),
				PartPose.offset(0.0F, 25.0F, 15.0F)
		);

		return LayerDefinition.create(meshdefinition, 256, 256);
	}

	@Override
	public ModelPart root() {
		return this.root;
	}

	@Override
	public void setupAnim(
			T entity,
			float limbSwing,
			float limbSwingAmount,
			float ageInTicks,
			float netHeadYaw,
			float headPitch
	) {
		this.root().getAllParts().forEach(ModelPart::resetPose);

		if (!entity.rollStartAnimationState.isStarted() && !entity.rollingLoopAnimationState.isStarted()) {
			this.rightleg.xRot = Mth.cos(limbSwing * 0.6662F) * limbSwingAmount;
			this.leftleg.xRot = Mth.cos(limbSwing * 0.6662F + Mth.PI) * limbSwingAmount;

			this.righthind.xRot = Mth.cos(limbSwing * 0.6662F + Mth.PI) * limbSwingAmount;
			this.lefthind.xRot = Mth.cos(limbSwing * 0.6662F) * limbSwingAmount;
		}

		this.animate(entity.rollStartAnimationState, BlackstoneGolemModelAnimation.roll, ageInTicks);
		this.animate(entity.rollingLoopAnimationState, BlackstoneGolemModelAnimation.rolling, ageInTicks);
	}

	@Override
	public void renderToBuffer(
			PoseStack poseStack,
			VertexConsumer vertexConsumer,
			int packedLight,
			int packedOverlay,
			int color
	) {
		this.root.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
	}
}