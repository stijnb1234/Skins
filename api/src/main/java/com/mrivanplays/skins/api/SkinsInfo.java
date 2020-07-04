package com.mrivanplays.skins.api;

import org.jetbrains.annotations.NotNull;

/**
 * Represents a skins info, providing information about the plugin's exact versioning and ran on
 * platform.
 */
public final class SkinsInfo {

  private final String version;
  private final String commit;
  private final int buildNumber;
  private final Environment environment;

  public SkinsInfo(String version, String commit, int buildNumber, Environment environment) {
    this.version = version;
    this.commit = commit;
    this.buildNumber = buildNumber;
    this.environment = environment;
  }

  /**
   * Returns the version specified in plugin.yml
   *
   * @return version
   */
  @NotNull
  public String getVersion() {
    return version;
  }

  /**
   * Returns whenever the plugin build, ran by the server, is a development build.
   *
   * @return <code>true</code> if dev build, <code>false</code> otherwise
   */
  public boolean isDevBuild() {
    return version.contains("SNAPSHOT");
  }

  /**
   * Returns the commit which triggered the build of the ran instance of the plugin
   *
   * @return commit
   */
  @NotNull
  public String getCommit() {
    return commit;
  }

  /**
   * Returns the build number of the ran plugin
   *
   * @return build number
   */
  public int getBuildNumber() {
    return buildNumber;
  }

  /**
   * Returns the environment the plugin is being ran on.
   *
   * @return environment
   */
  @NotNull
  public Environment getEnvironment() {
    return environment;
  }
}
