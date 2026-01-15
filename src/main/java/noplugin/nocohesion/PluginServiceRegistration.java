package noplugin.nocohesion;

public record PluginServiceRegistration<T extends PluginService, U extends T> (
        Class<T> serviceInterface,
        U serviceImplementation
) {
}
