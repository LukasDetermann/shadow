import io.determann.shadow.tck.Tck;

module io.determann.shadow.annotation.processing.test {

   requires transitive io.determann.shadow.lang.model;
   requires java.compiler;

   requires org.junit.jupiter.api;

   requires io.determann.shadow.annotation.processing;
   requires io.determann.shadow.tck;

   provides Tck with io.determann.shadow.annotation_processing.TckTest;

   opens io.determann.shadow.annotation_processing to  org.junit.platform.commons;
   opens io.determann.shadow.annotation_processing.shadow.type to  org.junit.platform.commons;
}