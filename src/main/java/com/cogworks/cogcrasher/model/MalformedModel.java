package com.cogworks.cogcrasher.model;

import com.cogworks.cogcrasher.entity.MalformedEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;

public class MalformedModel<T extends MalformedEntity> extends HierarchicalModel<T> {

	private final ModelPart root;
	private final ModelPart head;
	private final ModelPart upperBody;
	private final ModelPart leftLeg;
	private final ModelPart rightLeg;
	private final ModelPart leftArm;
	private final ModelPart rightArm;

	public MalformedModel(ModelPart root) {
		this.root = root;
		this.head = root.getChild("head");
		this.upperBody = root.getChild("upper_body");
		this.leftLeg = root.getChild("left_leg");
		this.rightLeg = root.getChild("right_leg");
		this.leftArm = root.getChild("left_arm");
		this.rightArm = root.getChild("right_arm");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition head = partdefinition.addOrReplaceChild(
				"head",
				CubeListBuilder.create()
						.texOffs(0, 0)
						.addBox(-5.0F, -21.0F, -3.0F, 11.0F, 21.0F, 8.0F)
						.texOffs(54, 0)
						.addBox(-9.0F, -21.0F, 1.0F, 19.0F, 21.0F, 0.0F),
				PartPose.offset(-0.5F, -10.0F, -1.0F)
		);

		PartDefinition upperBody = partdefinition.addOrReplaceChild(
				"upper_body",
				CubeListBuilder.create()
						.texOffs(0, 29)
						.addBox(-5.0F, 0.0F, -3.0F, 11.0F, 16.0F, 8.0F),
				PartPose.offset(-0.5F, -10.0F, -1.0F)
		);

		PartDefinition leftLeg = partdefinition.addOrReplaceChild(
				"left_leg",
				CubeListBuilder.create()
						.texOffs(38, 0)
						.addBox(-2.0F, -2.0F, -2.0F, 4.0F, 21.0F, 4.0F),
				PartPose.offset(3.5F, 5.0F, 0.0F)
		);

		leftLeg.addOrReplaceChild(
				"cube_r1",
				CubeListBuilder.create()
						.texOffs(31, 25)
						.addBox(-3.5F, 0.0F, -3.5F, 7.0F, 0.0F, 7.0F),
				PartPose.offsetAndRotation(0.0F, 19.0F, 0.0F, 0.0F, 1.5708F, 0.0F)
		);

		PartDefinition rightLeg = partdefinition.addOrReplaceChild(
				"right_leg",
				CubeListBuilder.create()
						.texOffs(38, 0)
						.addBox(-2.0F, -2.0F, -2.0F, 4.0F, 21.0F, 4.0F)
						.texOffs(31, 25)
						.addBox(-3.5F, 19.0F, -3.5F, 7.0F, 0.0F, 7.0F),
				PartPose.offset(-3.5F, 5.0F, 0.0F)
		);

		partdefinition.addOrReplaceChild(
				"left_arm",
				CubeListBuilder.create()
						.texOffs(38, 0)
						.addBox(-2.0F, -2.0F, -2.0F, 4.0F, 21.0F, 4.0F),
				PartPose.offset(7.5F, -8.0F, 0.0F)
		);

		partdefinition.addOrReplaceChild(
				"right_arm",
				CubeListBuilder.create()
						.texOffs(38, 0)
						.addBox(-2.0F, -2.0F, -2.0F, 4.0F, 21.0F, 4.0F),
				PartPose.offset(-7.5F, -2.0F, 0.0F)
		);

		return LayerDefinition.create(meshdefinition, 128, 128);
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

		this.animate(
				entity.walkAnimationState,
				MalformedAnimation.MALFORMED_WALK,
				ageInTicks
		);

		this.animate(
				entity.attackAnimationState,
				MalformedAnimation.MALFORMED_ATTACK,
				ageInTicks
		);

		this.animate(
				entity.invulnerableAnimationState,
				MalformedAnimation.MALFORMED_INVULNERABLE,
				ageInTicks
		);

		this.animate(
				entity.deathAnimationState,
				MalformedAnimation.MALFORMED_DEATH,
				ageInTicks
		);
	}

	@Override
	public void renderToBuffer(
			PoseStack poseStack,
			VertexConsumer vertexConsumer,
			int packedLight,
			int packedOverlay,
			int color
	) {
		this.root.render(
				poseStack,
				vertexConsumer,
				packedLight,
				packedOverlay,
				color
		);
	}
}