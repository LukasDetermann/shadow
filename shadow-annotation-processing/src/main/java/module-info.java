import io.determann.shadow.internal.annotation_processing.ApProvider;
import io.determann.shadow.internal.annotation_processing.TypesafeUsageGenerator;

module io.determann.shadow.annotation.processing {

   requires java.compiler;
   requires io.determann.shadow.implementation.support;
   requires static org.jetbrains.annotations;

   exports io.determann.shadow.api.annotation_processing;
   exports io.determann.shadow.api.annotation_processing.adapter;
   exports io.determann.shadow.api.annotation_processing.test;

   exports io.determann.shadow.internal.annotation_processing to io.determann.shadow.annotation.processing.test;

   provides io.determann.shadow.api.query.ProviderSpi with ApProvider;
   provides javax.annotation.processing.Processor with TypesafeUsageGenerator;
}