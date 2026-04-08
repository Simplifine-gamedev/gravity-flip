package com.orca.gravityflip;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class GravityFlipMod implements ModInitializer {
    public static final String MOD_ID = "gravity-flip";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static final Set<UUID> FLIPPED_PLAYERS = new HashSet<>();

    public static final Item GRAVITY_FLIPPER = new GravityFlipperItem(new Item.Settings().maxCount(1));

    @Override
    public void onInitialize() {
        Registry.register(Registries.ITEM, Identifier.of(MOD_ID, "gravity_flipper"), GRAVITY_FLIPPER);

        ServerTickEvents.END_SERVER_TICK.register(server -> {
            for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
                if (FLIPPED_PLAYERS.contains(player.getUuid())) {
                    if (!player.hasStatusEffect(StatusEffects.LEVITATION)) {
                        player.addStatusEffect(new StatusEffectInstance(StatusEffects.LEVITATION, 40, 10, false, false, true));
                    }
                    if (!player.hasStatusEffect(StatusEffects.SLOW_FALLING)) {
                        player.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOW_FALLING, 40, 0, false, false, true));
                    }
                }
            }
        });

        LOGGER.info("Gravity Flip mod initialized! Use the Gravity Flipper item to flip gravity.");
    }

    public static class GravityFlipperItem extends Item {
        public GravityFlipperItem(Settings settings) {
            super(settings);
        }

        @Override
        public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
            if (!world.isClient) {
                UUID playerId = user.getUuid();

                if (FLIPPED_PLAYERS.contains(playerId)) {
                    FLIPPED_PLAYERS.remove(playerId);
                    user.removeStatusEffect(StatusEffects.LEVITATION);
                    user.removeStatusEffect(StatusEffects.SLOW_FALLING);
                    user.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOW_FALLING, 100, 0, false, false, true));
                    user.sendMessage(Text.literal("Gravity restored to normal!"), true);
                } else {
                    FLIPPED_PLAYERS.add(playerId);
                    user.addStatusEffect(new StatusEffectInstance(StatusEffects.LEVITATION, 40, 10, false, false, true));
                    user.sendMessage(Text.literal("Gravity flipped! You now float upward!"), true);
                }
            }

            return TypedActionResult.success(user.getStackInHand(hand), world.isClient);
        }
    }
}
