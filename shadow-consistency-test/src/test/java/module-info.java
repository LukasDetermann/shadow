module io.determann.shadow.consistency.test {

   requires io.determann.shadow.annotation.processing;
   requires io.determann.shadow.reflection;
   requires io.determann.shadow.implementation.support;
   requires io.determann.shadow.consistency;

   requires java.desktop;
   requires java.compiler;

   requires org.junit.jupiter.api;

   opens io.determann.shadow.consistency.shadow to org.junit.platform.commons;
   opens io.determann.shadow.consistency.property to org.junit.platform.commons;
   opens io.determann.shadow.consistency.renderer to org.junit.platform.commons;
}