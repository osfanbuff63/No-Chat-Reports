package com.aizistral.nochatreports.mixins.client;

import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Supplier;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.aizistral.nochatreports.config.NCRConfig;
import com.aizistral.nochatreports.core.ServerSafetyLevel;
import com.aizistral.nochatreports.core.ServerSafetyState;
import com.aizistral.nochatreports.gui.AdvancedImageButton;
import com.aizistral.nochatreports.gui.InvisibleButton;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.social.PlayerEntry;
import net.minecraft.client.gui.screens.social.SocialInteractionsScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;

@Mixin(PlayerEntry.class)
public class MixinPlayerEntry {
	private static final Component NCR_BUTTON_TOOLTIP = Component.translatable("gui.nochatreports.no_reporting");
	@Shadow @Final private static ResourceLocation REPORT_BUTTON_LOCATION;
	@Shadow private Button reportButton;

	/**
	 * @reason Disable (or hide if configured respectively) chat report button on client.
	 * @author Aizistral
	 */

	@Inject(method = "<init>", at = @At("RETURN"))
	private void onConstructed(Minecraft minecraft, SocialInteractionsScreen screen, UUID uuid, String name, Supplier<ResourceLocation> skinGetter, boolean reportable, CallbackInfo info) {
		if (NCRConfig.getClient().alwaysHideReportButton()) {
			this.reportButton = new InvisibleButton();
			this.reportButton.active = this.reportButton.visible = false;
		} else if (ServerSafetyState.getCurrent() == ServerSafetyLevel.SECURE && this.reportButton != null) {
			this.reportButton = new AdvancedImageButton(0, 0, 20, 20, 0, 0, 20, REPORT_BUTTON_LOCATION, 64, 64,
					button -> {}, Component.translatable("gui.socialInteractions.report"), screen);
			this.reportButton.setTooltip(Tooltip.create(NCR_BUTTON_TOOLTIP));
			this.reportButton.setTooltipDelay(10);
			this.reportButton.active = false;
		}
	}

}
