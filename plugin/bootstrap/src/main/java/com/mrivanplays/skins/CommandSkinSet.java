package com.mrivanplays.skins;

import com.mrivanplays.skins.api.MojangResponse;
import com.mrivanplays.skins.core.StoredSkin;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;

public class CommandSkinSet implements TabExecutor {
    private final SkinsBukkitPlugin plugin;
    private final Map<UUID, Long> cooldownMap = new HashMap<>();

    public CommandSkinSet(SkinsBukkitPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(
            @NotNull CommandSender sender,
            @NotNull Command command,
            @NotNull String s,
            @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(plugin.color(plugin.getConfig().getString("messages.no-console")));
            return true;
        }
        Player player = (Player) sender;
        if (args.length == 0) {
            player.sendMessage(plugin.color(plugin.getConfig().getString("messages.command-usage")));
            return true;
        }
        return invokeSkinSet(player, plugin.getApi().getSkin(args[0]));
    }

    public boolean invokeSkinSet(Player player, MojangResponse response) {
        Long timeLeft = cooldownMap.get(player.getUniqueId());
        if (timeLeft != null) {
            long remainingTime = (timeLeft - System.currentTimeMillis()) / 1000;
            if (remainingTime > 0) {
                player.sendMessage(
                        plugin.color(
                                plugin
                                        .getConfig()
                                        .getString("messages.cooldown")
                                        .replace("%timeLeft%", Long.toString(remainingTime))));
                return true;
            }
        }
        if (!response.getSkin().isPresent()) {
            player.sendMessage(plugin.color(plugin.getConfig().getString("messages.not-premium")));
            return true;
        }
        plugin.getApi().setSkin(player, response);
        player.sendMessage(
                plugin.color(plugin.getConfig().getString("messages.skin-set-successfully")));
        long cooldown = 1000 * 5;
        cooldownMap.put(player.getUniqueId(), System.currentTimeMillis() + cooldown);
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(
            @NotNull CommandSender sender,
            @NotNull Command command,
            @NotNull String s,
            @NotNull String[] args) {
        if (args.length == 1) {
            List<String> matches = plugin.getApi().getSkinStorage().deserialize().stream()
                    .map(StoredSkin::getName).collect(Collectors.toList());
            List<OfflinePlayer> playersCombined = Arrays.stream(Bukkit.getOfflinePlayers()).collect(Collectors.toList());

            Collection<? extends Player> onlinePlayers = Bukkit.getOnlinePlayers();
            for (Player player : onlinePlayers) {
                if (!playersCombined.contains(player)) {
                    playersCombined.add(player);
                }
            }

            matches.addAll(
                    playersCombined.stream()
                            .map(OfflinePlayer::getName)
                            .filter(Objects::nonNull)
                            .collect(Collectors.toList()));

            return matches.stream()
                    .filter(match -> match.toLowerCase().startsWith(args[0].toLowerCase()))
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }
}
