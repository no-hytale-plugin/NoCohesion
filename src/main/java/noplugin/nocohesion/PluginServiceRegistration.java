package noplugin.nocohesion;

public record PluginServiceRegistration<T extends PluginService> (
        Class<T> serviceInterface,
        T serviceImplementation
) {
}
