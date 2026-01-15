package noplugin.cohesion.api;

import noplugin.nocohesion.PluginService;
import noplugin.nocohesion.PluginServiceRegistration;
import noplugin.nocohesion.PluginServiceRegistry;

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
        PluginServiceRegistration<MyInterface, MyService> registration = new PluginServiceRegistration<>(MyInterface.class, myService);

        PluginServiceRegistry.get().registerSingle(registration);
        Optional<MyInterface> single = PluginServiceRegistry.get().getSingle(MyInterface.class);
        System.out.println(single);

    }
}
