module io.determann.shadow.implementation.support {

   exports io.determann.shadow.implementation.support.api;
   exports io.determann.shadow.implementation.support.api.provider;
   exports io.determann.shadow.implementation.support.api.shadow;
   exports io.determann.shadow.implementation.support.api.shadow.directive;
   exports io.determann.shadow.implementation.support.api.shadow.type;

   exports io.determann.shadow.implementation.support.internal.property to io.determann.shadow.consistency.test;

   requires transitive io.determann.shadow.api;
}