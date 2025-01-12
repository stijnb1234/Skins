package com.mrivanplays.skins;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class UpdateCheckerSetup implements Listener {

    private final SkinsBukkitPlugin plugin;
    private final String permission;

    public UpdateCheckerSetup(SkinsBukkitPlugin plugin, String permission) {
        this.plugin = plugin;
        this.permission = permission;
    }

    public void setup() {
        int spigotResourceId = 70829;
        String updateUrl = "https://www.spigotmc.org/resources/" + spigotResourceId + "/";
        if (plugin.getConfig().getBoolean("update-check")) {
            UpdateChecker.init(
                    plugin,
                    spigotResourceId,
                    (first, second) -> second.equalsIgnoreCase(first) ? second : first)
                    .requestUpdateCheck()
                    .whenComplete(
                            (result, throwable) -> {
                                UpdateChecker.UpdateReason reason = result.getReason();
                                if (reason == UpdateChecker.UpdateReason.NEW_UPDATE) {
                                    plugin
                                            .getLogger()
                                            .warning(
                                                    String.format(
                                                            "An update is available! Skins %s may be downloaded from here: %s",
                                                            result.getNewestVersion(), updateUrl));
                                    plugin
                                            .getServer()
                                            .getPluginManager()
                                            .registerEvents(UpdateCheckerSetup.this, plugin);
                                } else if (reason == UpdateChecker.UpdateReason.UNRELEASED_VERSION) {
                                    String currentVersion = plugin.getDescription().getVersion();
                                    if (currentVersion.contains("SNAPSHOT")) {
                                        plugin.getLogger().severe("!! RUNNING DEV BUILD OF Skins !!");
                                    } else {
                                        plugin
                                                .getLogger()
                                                .severe("!! RUNNING UNRELEASED VERSION OF Skins !! (Is this a dev build?)");
                                    }
                                    plugin
                                            .getServer()
                                            .getPluginManager()
                                            .registerEvents(UpdateCheckerSetup.this, plugin);
                                } else if (reason != UpdateChecker.UpdateReason.UP_TO_DATE) {
                                    plugin
                                            .getLogger()
                                            .warning(
                                                    "Could not check for updates. Reason: "
                                                            + reason.name().toLowerCase().replace("_", " "));
                                }
                            });
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (player.hasPermission(permission)) {
            UpdateChecker.UpdateResult result = UpdateChecker.get().getLastResult();
            UpdateChecker.UpdateReason reason = result.getReason();
            if (reason == UpdateChecker.UpdateReason.NEW_UPDATE) {
                plugin
                        .getServer()
                        .getScheduler()
                        .scheduleSyncDelayedTask(plugin, () -> message(player, result.getNewestVersion()), 100);
            } else if (reason == UpdateChecker.UpdateReason.UNRELEASED_VERSION) {
                plugin
                        .getServer()
                        .getScheduler()
                        .scheduleSyncDelayedTask(
                                plugin,
                                () -> {
                                    String version = plugin.getDescription().getVersion();
                                    if (version.contains("SNAPSHOT")) {
                                        player.sendMessage(plugin.color("&4&l!! RUNNING DEV BUILD OF Skins !!"));
                                    } else {
                                        player.sendMessage(
                                                plugin.color(
                                                        "&4&l!! RUNNING UNRELEASED VERSION OF Skins !! &e(Is this a dev build?)"));
                                    }
                                });
            }
        }
    }

    private void message(Player player, String newVersion) {
        ComponentBuilder baseMessage =
                new ComponentBuilder("Update found for " + plugin.getName() + " . \n")
                        .color(ChatColor.GRAY);
        BaseComponent[] versionMessage =
                TextComponent.fromLegacyText(
                        plugin.color(
                                "&7- &cOld version (current): "
                                        + plugin.getDescription().getVersion()
                                        + " &7; &aNew version: "
                                        + newVersion
                                        + "\n"));
        baseMessage.append(versionMessage);
        TextComponent downloadCommand = new TextComponent("- Download via clicking ");
        downloadCommand.setColor(ChatColor.GRAY);
        TextComponent clickLink = new TextComponent("here");
        clickLink.setColor(ChatColor.AQUA);
        clickLink.setBold(true);
        clickLink.setClickEvent(
                new ClickEvent(ClickEvent.Action.OPEN_URL, "https://www.spigotmc.org/resources/70829/"));
        BaseComponent[] hoverMessage =
                new ComponentBuilder("Click here to be redirect to download page")
                        .color(ChatColor.DARK_GREEN)
                        .create();
        clickLink.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, hoverMessage));
        downloadCommand.addExtra(clickLink);
        downloadCommand.addExtra(plugin.color("&7 ."));
        baseMessage.append(downloadCommand);
        player.spigot().sendMessage(baseMessage.create());
    }
}
