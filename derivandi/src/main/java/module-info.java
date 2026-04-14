import com.derivandi.internal.TypesafeUsageGenerator;

module com.derivandi {

   requires java.compiler;
   requires static org.jetbrains.annotations;

   exports com.derivandi.api;
   exports com.derivandi.api.adapter;
   exports com.derivandi.api.processor;
   exports com.derivandi.api.test;

   exports com.derivandi.internal to com.derivandi.test;

   exports com.derivandi.api.dsl;
   exports com.derivandi.api.dsl.annotation;
   exports com.derivandi.api.dsl.annotation_usage;
   exports com.derivandi.api.dsl.annotation_value;
   exports com.derivandi.api.dsl.array;
   exports com.derivandi.api.dsl.class_;
   exports com.derivandi.api.dsl.constructor;
   exports com.derivandi.api.dsl.declared;
   exports com.derivandi.api.dsl.enum_;
   exports com.derivandi.api.dsl.enum_constant;
   exports com.derivandi.api.dsl.exports;
   exports com.derivandi.api.dsl.field;
   exports com.derivandi.api.dsl.generic;
   exports com.derivandi.api.dsl.import_;
   exports com.derivandi.api.dsl.interface_;
   exports com.derivandi.api.dsl.method;
   exports com.derivandi.api.dsl.module;
   exports com.derivandi.api.dsl.opens;
   exports com.derivandi.api.dsl.package_;
   exports com.derivandi.api.dsl.parameter;
   exports com.derivandi.api.dsl.provides;
   exports com.derivandi.api.dsl.receiver;
   exports com.derivandi.api.dsl.record;
   exports com.derivandi.api.dsl.record_component;
   exports com.derivandi.api.dsl.requires;
   exports com.derivandi.api.dsl.result;
   exports com.derivandi.api.dsl.uses;

   exports com.derivandi.internal.dsl to com.derivandi.test;
   exports com.derivandi.internal.processor to com.derivandi.test;

   provides javax.annotation.processing.Processor with TypesafeUsageGenerator;
}