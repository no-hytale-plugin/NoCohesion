A simple plugin to help interaction between Hytale plugins by registering offered services into a registry.

# How does it work ? 
You have an api:
```java
package yourplugin.api;
public interface YourServiceInterface {}
```
You have an implementation:
```java
package yourplugin;
public class YourServiceImplementation implements YourServiceInterface {
}
```
You offer them to other plugin developers:

```java
package yourplugin;

import noplugin.nocohesion.PluginServiceRegistration;
import noplugin.nocohesion.PluginServiceRegistry;

public class YourPlugin extends JavaPlugin {
    // Your constructor...
    @Override
    protected void setup() {
        // Your initialization
        var yourImplementation = new YourServiceImplementation();
        PluginServiceRegistry.get().registerSingle(
            new PluginServiceRegistration<>(YourServiceInterface.class, yourImplementation)
        );
    }
}
```

Other developers can access your service by doing:
```java
package theirplugin;
public class TheirClass {
    public void theirMethod() {
        var yourService = PluginServiceRegistry.get().getSingle(YourServiceInterface.class);
    }
}
```

# Why would I do that ?
It helps your structure your code by decoupling the API and the implementation. That's a good thing for many reasons :
- Developer that uses your API don't need to add your complete code in their own and can use a lighter subset of it
- If you decide you want to stop developing your implementation or don't want to put feature in it, but you API is used by other plugins, 
other developers can create their own implementation that does it while keeping compatibility.