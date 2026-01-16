package noplugin.hytale.plugins.nocohesion;

import com.hypixel.hytale.logger.HytaleLogger;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.stream.Collectors;

public enum PluginServiceRegistry {

    INSTANCE;

    private final Map<Class<? extends PluginService>, PluginService> singlesRegistry = new ConcurrentHashMap<>();
    private final Map<Class<? extends PluginService>, List<PluginService>> manyRegistry = new ConcurrentHashMap<>();

    private HytaleLogger logger;

    public static PluginServiceRegistry get() {
        return INSTANCE;
    }

    PluginServiceRegistry() {}

    /**
     * Register the implementation of a service of which we expect only one implementation
     * @param registration         the registration of a service
     * @param <T>   an interface that extends {@link PluginService}
     */
    public <T extends PluginService> void registerSingle(PluginServiceRegistration<T> registration) {
        Objects.requireNonNull(registration, "serviceInterface must not be null");
        Class<T> serviceInterface = registration.serviceInterface();
        Objects.requireNonNull(serviceInterface, "serviceInterface must not be null");
        T serviceImplementation = registration.serviceImplementation();

        PluginService currentImplementation = singlesRegistry.get(serviceInterface);
        if (currentImplementation != null) {
            var newName = serviceImplementation == null ? null : serviceImplementation.getName();
            warn("'{}' is replacing '{}' implementation of interface '{}'", newName, currentImplementation.getName(), serviceInterface.getName());
        }
        singlesRegistry.put(serviceInterface, serviceImplementation);
    }

    /**
     * Register the implementation of a service of which we accept many implementations
     * @param registration         the registration of a service
     * @param <T>   an interface that extends {@link PluginService}
     */
    public <T extends PluginService> void registerOneOfMany(PluginServiceRegistration<T> registration) {
        Objects.requireNonNull(registration, "registration must not be null");
        Class<T> serviceInterface = registration.serviceInterface();
        Objects.requireNonNull(serviceInterface, "serviceInterface must not be null");
        T serviceImplementation = registration.serviceImplementation();

        List<? super PluginService> implementations = manyRegistry.computeIfAbsent(serviceInterface, k -> new CopyOnWriteArrayList<>());
        implementations.add(serviceImplementation);
    }

    /**
     * Get the implementation of a service if it was registered with the {@link #registerSingle(PluginServiceRegistration)} method
     * @param serviceInterface The service interface you want an implementation of
     * @return an {@link Optional} that may contain the implementation of a service
     * @param <T>
     */
    public <T extends PluginService> Optional<T> getSingle(Class<T> serviceInterface) {
        PluginService pluginService = singlesRegistry.get(serviceInterface);
        if (pluginService == null) {
            return Optional.empty();
        }
        return Optional.of(serviceInterface.cast(pluginService));
    }

    /**
     * Get all the implementations of a service that were registered with the {@link #registerOneOfMany(PluginServiceRegistration)} method
     * @param serviceInterface The service interface you want implementations of
     * @return a list of all the registered implementations. May be empty but not null.
     * @param <T>
     */
    public <T extends PluginService> List<T> getMany(Class<T> serviceInterface) {
        List<? extends PluginService> pluginServices = manyRegistry.get(serviceInterface);
        if (pluginServices == null) {
            return Collections.emptyList();
        }
       return pluginServices.stream()
            .map(serviceInterface::cast)
            .toList();
    }

    void logRegisteredServices() {
        if (logger != null) {
            logger.at(Level.INFO).log("Registered {} single services for plugin service", singlesRegistry.size());
            singlesRegistry.forEach((serviceInterface, serviceImplementation) -> {
                logger.at(Level.INFO).log("- {} -> {}", serviceInterface, serviceImplementation.getClass().getName());
            });

            logger.at(Level.INFO).log("Registered {} many services for plugin service", manyRegistry.size());
            manyRegistry.forEach((serviceInterface, serviceImplementations) -> {
                logger.at(Level.INFO).log("- {} -> {}", serviceInterface, serviceImplementations.stream().map(Object::getClass).map(Class::getName).collect(Collectors.joining(",")));
            });
        }
    }

    void setLogger (HytaleLogger logger) {
        this.logger = logger;
    }

    private void warn(String message, String... params) {
        log(Level.WARNING, message, params);
    }

    private void log(Level level, String message, String... params) {
        if (logger != null) {
            logger.at(level).log(message, params);
        }
    }


}
