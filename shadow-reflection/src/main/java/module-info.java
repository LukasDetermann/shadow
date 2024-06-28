import io.determann.shadow.internal.reflection.ReflectionProvider;

module io.determann.shadow.reflection {

   requires io.determann.shadow.implementation.support;
   requires transitive io.determann.shadow.api;

   exports io.determann.shadow.api.reflection;
   exports io.determann.shadow.api.reflection.shadow;
   exports io.determann.shadow.api.reflection.shadow.type;
   exports io.determann.shadow.api.reflection.shadow.structure;

   provides io.determann.shadow.api.shadow.ProviderSpi with ReflectionProvider;
}