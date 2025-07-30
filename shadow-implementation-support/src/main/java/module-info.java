import io.determann.shadow.api.query.ProviderSpi;
import io.determann.shadow.implementation.support.internal.SupportProvider;

module io.determann.shadow.implementation.support {

   exports io.determann.shadow.implementation.support.api.provider;
   exports io.determann.shadow.implementation.support.api.shadow;
   exports io.determann.shadow.implementation.support.api.shadow.directive;
   exports io.determann.shadow.implementation.support.api.shadow.type;
   exports io.determann.shadow.implementation.support.api.shadow.structure;

   requires transitive io.determann.shadow.api;

   provides ProviderSpi with SupportProvider;
}