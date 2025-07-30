import io.determann.shadow.api.query.ProviderSpi;
import io.determann.shadow.internal.reflection.ReflectionProvider;

module io.determann.shadow.reflection {

   requires io.determann.shadow.implementation.support;
   requires transitive io.determann.shadow.api;

   exports io.determann.shadow.api.reflection;

   provides ProviderSpi with ReflectionProvider;
}