package com.mrivanplays.skins.core.storage;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public interface StorageProvider {

  void connect();

  void storeSkin(StoredSkin skin);

  void setAcquirer(UUID uuid, UUID skinAcquired);

  StoredSkin findByName(String name);

  StoredSkin find(UUID uuid);

  StoredSkin acquired(UUID uuid);

  Collection<UUID> getUsedBy(UUID uuid);

  List<StoredSkin> all();

  void closeConnection();
}
