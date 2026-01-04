import io.determann.shadow.internal.annotation_processing.TypesafeUsageGenerator;

module io.determann.shadow.annotation.processing {

   requires java.compiler;
   requires static org.jetbrains.annotations;

   exports io.determann.shadow.api.annotation_processing;
   exports io.determann.shadow.api.annotation_processing.adapter;
   exports io.determann.shadow.api.annotation_processing.test;

   exports io.determann.shadow.internal.annotation_processing to io.determann.shadow.annotation.processing.test;

   exports io.determann.shadow.api.annotation_processing.dsl;
   exports io.determann.shadow.api.annotation_processing.dsl.annotation;
   exports io.determann.shadow.api.annotation_processing.dsl.annotation_usage;
   exports io.determann.shadow.api.annotation_processing.dsl.annotation_value;
   exports io.determann.shadow.api.annotation_processing.dsl.array;
   exports io.determann.shadow.api.annotation_processing.dsl.class_;
   exports io.determann.shadow.api.annotation_processing.dsl.constructor;
   exports io.determann.shadow.api.annotation_processing.dsl.declared;
   exports io.determann.shadow.api.annotation_processing.dsl.enum_;
   exports io.determann.shadow.api.annotation_processing.dsl.enum_constant;
   exports io.determann.shadow.api.annotation_processing.dsl.exports;
   exports io.determann.shadow.api.annotation_processing.dsl.field;
   exports io.determann.shadow.api.annotation_processing.dsl.generic;
   exports io.determann.shadow.api.annotation_processing.dsl.import_;
   exports io.determann.shadow.api.annotation_processing.dsl.interface_;
   exports io.determann.shadow.api.annotation_processing.dsl.method;
   exports io.determann.shadow.api.annotation_processing.dsl.module;
   exports io.determann.shadow.api.annotation_processing.dsl.opens;
   exports io.determann.shadow.api.annotation_processing.dsl.package_;
   exports io.determann.shadow.api.annotation_processing.dsl.parameter;
   exports io.determann.shadow.api.annotation_processing.dsl.provides;
   exports io.determann.shadow.api.annotation_processing.dsl.receiver;
   exports io.determann.shadow.api.annotation_processing.dsl.record;
   exports io.determann.shadow.api.annotation_processing.dsl.record_component;
   exports io.determann.shadow.api.annotation_processing.dsl.requires;
   exports io.determann.shadow.api.annotation_processing.dsl.result;
   exports io.determann.shadow.api.annotation_processing.dsl.uses;

   exports io.determann.shadow.internal.annotation_processing.dsl to io.determann.shadow.annotation.processing.test;

   provides javax.annotation.processing.Processor with TypesafeUsageGenerator;
}