package com.mrivanplays.skins.core;

import com.mrivanplays.skins.api.SkinsApi;

public interface SkinsPlugin {
    void enable(InitializationData initializationData);

    SkinsApi getApi();
}
