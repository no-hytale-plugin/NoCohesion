package noplugin.cohesion;

import noplugin.hytale.plugins.nocohesion.PluginService;
import noplugin.hytale.plugins.nocohesion.PluginServiceRegistration;
import noplugin.hytale.plugins.nocohesion.PluginServiceRegistry;

import java.util.Optional;

public class RegistryTest {

    interface MyInterface extends PluginService {}
    static class MyService implements MyInterface {

        @Override
        public String getName() {
            return "Test";
        }
    }


    static void main() {
        MyService myService = new MyService();
        var registration = new PluginServiceRegistration<>(MyInterface.class, myService);

        PluginServiceRegistry.get().registerSingle(registration);
        Optional<MyInterface> single = PluginServiceRegistry.get().getSingle(MyInterface.class);
        System.out.println(single);

    }
}
