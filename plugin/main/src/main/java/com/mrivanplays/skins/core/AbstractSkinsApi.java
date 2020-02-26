package com.mrivanplays.skins.core;

import com.mrivanplays.skins.api.MojangResponse;
import com.mrivanplays.skins.api.Skin;
import com.mrivanplays.skins.api.SkinsApi;
import java.io.File;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Logger;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractSkinsApi implements SkinsApi {

  private final SkinFetcher skinFetcher;
  private final SkinStorage skinStorage;

  public AbstractSkinsApi(File dataFolder, Logger logger) {
    skinStorage = new SkinStorage(dataFolder);
    skinFetcher = new SkinFetcher(skinStorage, logger);
  }

  @Override
  public Optional<Skin> getSetSkin(@NotNull Player player) {
    return skinStorage.getPlayerSetSkin(player.getUniqueId()).map(StoredSkin::getSkin);
  }

  @Override
  @NotNull
  public MojangResponse getSkin(@NotNull String username) {
    MojangResponseHolder holder = skinFetcher.getSkin(username);
    return holder.getResponse();
  }

  public MojangResponseHolder getSkinHolder(String username) {
    return skinFetcher.getSkin(username);
  }

  @Override
  public boolean setSkin(@NotNull Player player, @NotNull MojangResponse skin) {
    if (skin.getSkin().isPresent()) {
      setSkin(player, skin.getSkin().get(), skin.getNickname());
      return true;
    } else {
      return false;
    }
  }

  public void setSkin(Player player, Skin skin, String name) {
    modifyStoredSkin(player.getUniqueId(), skin, name);
    setNPCSkin(player, skin);
  }

  public void modifyStoredSkin(UUID uuid, Skin skin, String name) {
    Optional<StoredSkin> newStoredSkin = skinStorage.getStoredSkin(skin.getOwner());
    if (newStoredSkin.isPresent()) {
      StoredSkin skinStored = newStoredSkin.get();
      skinStorage.modifyStoredSkin(uuid, skinStored);
    } else {
      Set<String> keys = skinStorage.getKeys();
      OptionalInt biggestNumberStored = keys.stream().mapToInt(Integer::parseInt).max();
      int biggestNumber;
      if (!biggestNumberStored.isPresent()) {
        biggestNumber = 0;
      } else {
        biggestNumber = biggestNumberStored.getAsInt();
      }
      keys.clear();
      StoredSkin skinStored = new StoredSkin(skin, Integer.toString(biggestNumber + 1), name);
      skinStorage.modifyStoredSkin(uuid, skinStored);
    }
  }

  protected void setNPCSkin(Player player, Skin skin) {}

  public SkinFetcher getSkinFetcher() {
    return skinFetcher;
  }

  public SkinStorage getSkinStorage() {
    return skinStorage;
  }
}
