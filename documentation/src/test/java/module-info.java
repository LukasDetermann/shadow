module documentation {

   exports io.determann.shadow.javadoc;
   exports io.determann.shadow.builder;

   requires io.determann.shadow.api;
   requires io.determann.shadow.reflection;
   requires io.determann.shadow.annotation.processing;

   requires org.junit.jupiter.api;
   requires org.apache.commons.lang3;

   requires java.compiler;
   requires io.determann.shadow.tck;

   opens io.determann.shadow to org.junit.platform.commons;
   opens io.determann.shadow.article to org.junit.platform.commons;
   opens io.determann.shadow.article.consistency_test to org.junit.platform.commons;
   opens io.determann.shadow.javadoc to org.junit.platform.commons;
   opens io.determann.shadow.builder to org.junit.platform.commons;
}