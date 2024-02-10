package io.determann.shadow.builder;

import io.determann.shadow.api.annotation_processing.test.ProcessorTest;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.nio.file.Paths;

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
      assertDoesNotThrow(() -> ProcessorTest.process(ShadowBuilderProcessor.class)
                                            .withCodeToCompile(DIR.resolve("BuilderPattern.java"))
                                            .withCodeToCompile(DIR.resolve("Customer.java"))
                                            .compile());
   }

   @Test
   void jdk()
   {
      assertDoesNotThrow(() -> ProcessorTest.process(JdkBuilderProcessor.class)
                                            .withCodeToCompile(DIR.resolve("BuilderPattern.java"))
                                            .withCodeToCompile(DIR.resolve("Customer.java"))
                                            .compile());
   }
}