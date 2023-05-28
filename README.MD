# SkyCommon

A project containing all utilities we use in our projects

# Modules

- ~~Command: an annotation command framework for [Minestom](https://github.com/Minestom/Minestom) with planned support
  for [Brigadier](https://github.com/Mojang/brigadier)~~ (Currently private/WIP)
- Event: [Minestom](https://github.com/Minestom/Minestom)'s event system used across various modules
- I18N: Modular internationalization system supporting [Adventure](https://github.com/KyoriPowered/adventure)'s
  component or Strings
- Object: A parsing library allowing to easily parse and navigate trough JSON/YAML objects.
- Util: Generic utility classes used pretty much evereywhere

# Usage

```kotlin
repositories {
    maven { url = uri("https://repo.mc-skyplex.net/releases") }
}

dependencies {
    // to include a specific module use:
    implementation("net.skyplex.SkyCommon:<module>:<version>")
    // or to include all the modules at once include the root project:
    implementation("net.skyplex:SkyCommon:<version>")
}
```
