module io.determann.shadow.annotation.processing.test {

   requires java.compiler;

   requires org.junit.jupiter.api;

   requires io.determann.shadow.annotation.processing;
   requires io.determann.shadow.api;

   opens io.determann.shadow.annotation_processing to  org.junit.platform.commons;
   opens io.determann.shadow.annotation_processing.shadow to  org.junit.platform.commons;
   opens io.determann.shadow.annotation_processing.shadow.type to  org.junit.platform.commons;
   opens io.determann.shadow.annotation_processing.shadow.structure to  org.junit.platform.commons;
}