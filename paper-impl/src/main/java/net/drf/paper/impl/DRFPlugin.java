package net.drf.paper.impl;

import org.bukkit.plugin.java.JavaPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DRFPlugin extends JavaPlugin {
    private static final Logger log = LoggerFactory.getLogger("DRF");
    @Override
    public void onEnable() {
        log.info("Initializing DRF...");
    }

    @Override
    public void onDisable() {
        log.info("Stopping DRF...");
    }
}
