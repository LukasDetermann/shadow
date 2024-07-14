module io.determann.shadow.consistency {

   requires io.determann.shadow.reflection;
   requires io.determann.shadow.annotation.processing;

   requires java.compiler;

   exports io.determann.shadow.consistency.test;
}