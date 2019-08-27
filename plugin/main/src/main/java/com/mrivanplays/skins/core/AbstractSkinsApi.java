/*
 * Copyright 2019 Ivan Pekov (MrIvanPlays)
 * Copyright 2019 contributors

 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in the
 * Software without restriction, including without limitation the rights to use, copy,
 * modify, merge, publish, distribute, sublicense, and/or sell copies of the Software,
 * and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

 * The above copyright notice and this permission notice shall be included in all copies
 * or substantial portions of the Software.

 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
 * ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 **/
package com.mrivanplays.skins.core;

import com.mrivanplays.skins.api.Skin;
import com.mrivanplays.skins.api.SkinsApi;
import java.io.File;
import java.util.Optional;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractSkinsApi implements SkinsApi {

    private final SkinFetcher skinFetcher;
    private final SkinStorage skinStorage;

    public AbstractSkinsApi(File dataFolder) {
        skinFetcher = new SkinFetcher();
        skinStorage = new SkinStorage(dataFolder);
    }

    @Override
    public Optional<Skin> getSetSkin(@NotNull Player player) {
        return skinStorage.getStoredSkin(player).map(StoredSkin::getSkin);
    }

    @Override
    public Optional<Skin> getOriginalSkin(@NotNull Player player) {
        Skin skin = skinFetcher.getSkin(player.getName());
        return Optional.ofNullable(skin.getTexture().contains("exception") ? null : skin);
    }

    public SkinFetcher getSkinFetcher() {
        return skinFetcher;
    }

    public SkinStorage getSkinStorage() {
        return skinStorage;
    }
}
