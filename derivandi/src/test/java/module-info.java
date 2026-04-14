module com.derivandi.test {

   requires java.compiler;

   requires org.junit.jupiter.api;

   requires com.derivandi;

   opens com.derivandi to  org.junit.platform.commons;
   opens com.derivandi.dsl to  org.junit.platform.commons;
   opens com.derivandi.javadoc to org.junit.platform.commons;
   opens com.derivandi.shadow to  org.junit.platform.commons;
   opens com.derivandi.shadow.type to  org.junit.platform.commons;
   opens com.derivandi.shadow.structure to  org.junit.platform.commons;
}