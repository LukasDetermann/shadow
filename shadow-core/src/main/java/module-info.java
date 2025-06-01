module io.determann.shadow.api {

   exports io.determann.shadow.api;
   exports io.determann.shadow.api.renderer;
   exports io.determann.shadow.api.operation;

   exports io.determann.shadow.api.shadow;
   exports io.determann.shadow.api.shadow.modifier;
   exports io.determann.shadow.api.shadow.directive;
   exports io.determann.shadow.api.shadow.structure;
   exports io.determann.shadow.api.shadow.type;
   exports io.determann.shadow.api.shadow.type.primitive;

   exports io.determann.shadow.api.dsl;
   exports io.determann.shadow.api.dsl.annotation;
   exports io.determann.shadow.api.dsl.annotation_usage;
   exports io.determann.shadow.api.dsl.annotation_value;
   exports io.determann.shadow.api.dsl.class_;
   exports io.determann.shadow.api.dsl.constructor;
   exports io.determann.shadow.api.dsl.declared;
   exports io.determann.shadow.api.dsl.enum_constant;
   exports io.determann.shadow.api.dsl.exports;
   exports io.determann.shadow.api.dsl.field;
   exports io.determann.shadow.api.dsl.generic;
   exports io.determann.shadow.api.dsl.interface_;
   exports io.determann.shadow.api.dsl.method;
   exports io.determann.shadow.api.dsl.module;
   exports io.determann.shadow.api.dsl.opens;
   exports io.determann.shadow.api.dsl.package_;
   exports io.determann.shadow.api.dsl.parameter;
   exports io.determann.shadow.api.dsl.provides;
   exports io.determann.shadow.api.dsl.receiver;
   exports io.determann.shadow.api.dsl.record_component;
   exports io.determann.shadow.api.dsl.requires;
   exports io.determann.shadow.api.dsl.result;
   exports io.determann.shadow.api.dsl.uses;

   uses io.determann.shadow.api.ProviderSpi;
}