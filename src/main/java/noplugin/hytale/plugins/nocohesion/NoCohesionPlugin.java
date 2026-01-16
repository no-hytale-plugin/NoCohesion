package noplugin.hytale.plugins.nocohesion;

import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

public class NoCohesionPlugin extends JavaPlugin {

    public NoCohesionPlugin(@NonNullDecl JavaPluginInit init) {
        super(init);
        PluginServiceRegistry.INSTANCE.setLogger(getLogger());
    }

    @Override
    protected void start() {
        PluginServiceRegistry.INSTANCE.logRegisteredServices();
    }
}
