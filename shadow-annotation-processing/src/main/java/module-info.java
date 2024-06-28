module io.determann.shadow.annotation.processing {

   requires transitive io.determann.shadow.lang.model;
   requires java.compiler;

   exports io.determann.shadow.api.annotation_processing;
   exports io.determann.shadow.api.annotation_processing.test;
}