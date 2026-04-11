package io.determann.shadow.builder;

import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.nio.file.Paths;

import static io.determann.shadow.api.annotation_processing.test.ProcessorTest.processorTest;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class BuilderTest
{
   private static final Path DIR = Paths.get("src",
                                             "test",
                                             "java",
                                             "io",
                                             "determann",
                                             "shadow",
                                             "builder");

   @Test
   void shadow()
   {
      assertDoesNotThrow(() -> processorTest().withCodeToCompile(DIR.resolve("BuilderPattern.java"))
                                              .withCodeToCompile(DIR.resolve("Customer.java"))
                                              .process(new ShadowBuilderProcessor()));
   }

   @Test
   void jdk()
   {
      assertDoesNotThrow(() -> processorTest().withCodeToCompile(DIR.resolve("BuilderPattern.java"))
                                              .withCodeToCompile(DIR.resolve("Customer.java"))
                                              .process(new JdkBuilderProcessor()));
   }
}