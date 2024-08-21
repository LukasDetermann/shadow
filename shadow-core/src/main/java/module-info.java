import io.determann.shadow.api.ProviderSpi;

module io.determann.shadow.api {

   exports io.determann.shadow.api;
   exports io.determann.shadow.api.renderer;
   exports io.determann.shadow.api.shadow;
   exports io.determann.shadow.api.shadow.modifier;
   exports io.determann.shadow.api.shadow.directive;
   exports io.determann.shadow.api.shadow.structure;
   exports io.determann.shadow.api.shadow.type;

   uses ProviderSpi;
}