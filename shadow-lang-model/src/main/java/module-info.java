import io.determann.shadow.internal.lang_model.LangModelProvider;

module io.determann.shadow.lang.model {

   requires java.compiler;
   requires io.determann.shadow.implementation.support;
   requires transitive io.determann.shadow.api;

   exports io.determann.shadow.api.lang_model;
   exports io.determann.shadow.api.lang_model.shadow;
   exports io.determann.shadow.api.lang_model.shadow.structure;
   exports io.determann.shadow.api.lang_model.shadow.type;
   exports io.determann.shadow.api.lang_model.shadow.modifier;

   provides io.determann.shadow.api.shadow.ProviderSpi with LangModelProvider;
}