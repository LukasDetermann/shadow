module io.determann.shadow.lang.model {

   requires java.compiler;
   requires io.determann.shadow.implementation.support;
   requires transitive io.determann.shadow.api;

   exports io.determann.shadow.api.lang_model;
   exports io.determann.shadow.api.lang_model.adapter;
   exports io.determann.shadow.api.lang_model.shadow;
   exports io.determann.shadow.api.lang_model.shadow.structure;
   exports io.determann.shadow.api.lang_model.shadow.type;
   exports io.determann.shadow.api.lang_model.shadow.modifier;
   exports io.determann.shadow.api.lang_model.shadow.directive;
   exports io.determann.shadow.api.lang_model.shadow.type.primitive;

   provides io.determann.shadow.api.ProviderSpi with io.determann.shadow.internal.lang_model.LangModelProvider;
}