package com.mrivanplays.skins.api;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

/**
 * Represents a data provider, providing needed plugin data.
 */
public interface DataProvider {

    /**
     * Skins calls this method whenever it needs the original uuid of the specified player name. You
     * are able to apply caching strategies on this.
     *
     * @param name name of the player we need the uuid of
     * @return uuid, or null if not found
     */
    @Nullable
    UUID retrieveUuid(@NotNull String name);

    /**
     * Skins calls this method whenever it needs to get a skin directly from the api. We recommend to
     * not cache any data as Skins already caches further in the code.
     *
     * @param name name of the player we want the skin of
     * @param uuid uuid of the player we want the skin of
     * @return response
     */
    @NotNull
    MojangResponse retrieveSkinResponse(@NotNull String name, @NotNull UUID uuid);
}
