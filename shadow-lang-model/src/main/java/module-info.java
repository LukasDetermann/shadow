import io.determann.shadow.api.query.ProviderSpi;

module io.determann.shadow.lang.model {

   requires java.compiler;
   requires io.determann.shadow.implementation.support;
   requires transitive io.determann.shadow.api;

   exports io.determann.shadow.api.lang_model;
   exports io.determann.shadow.api.lang_model.adapter;

   provides ProviderSpi with io.determann.shadow.internal.lang_model.LangModelProvider;
}