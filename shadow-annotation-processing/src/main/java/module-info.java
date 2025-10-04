import io.determann.shadow.internal.annotation_processing.ApProvider;

module io.determann.shadow.annotation.processing {

   requires java.compiler;
   requires io.determann.shadow.implementation.support;

   exports io.determann.shadow.api.annotation_processing;
   exports io.determann.shadow.api.annotation_processing.adapter;
   exports io.determann.shadow.api.annotation_processing.test;

   provides io.determann.shadow.api.query.ProviderSpi with ApProvider;
}