package noplugin.hytale.plugins.nocohesion;

public record PluginServiceRegistration<T extends PluginService> (
        Class<T> serviceInterface,
        T serviceImplementation
) {
}
