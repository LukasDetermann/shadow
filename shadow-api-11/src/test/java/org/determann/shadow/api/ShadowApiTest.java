package org.determann.shadow.api;

import org.determann.shadow.api.test.CompilationTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.function.UnaryOperator;


class ShadowApiTest
{
   @Test
   void to_UpperCamelCaseTest()
   {
      CompilationTest.process(shadowApi -> testStringConversion("ExampleName", shadowApi::to_UpperCamelCase))
                     .compile();
   }

   @Test
   void to_lowerCamelCaseTest()
   {
      CompilationTest.process(shadowApi -> testStringConversion("exampleName", shadowApi::to_lowerCamelCase))
                     .compile();
   }

   @Test
   void to_SCREAMING_SNAKE_CASE_Test()
   {
      CompilationTest.process(shadowApi -> testStringConversion("EXAMPLE_NAME", shadowApi::to_SCREAMING_SNAKE_CASE))
                     .compile();
   }

   private void testStringConversion(String expected, UnaryOperator<String> conversion)
   {
      CompilationTest.process(shadowApi ->
                              {
                                 Assertions.assertEquals(expected, conversion.apply("ExampleName"));
                                 Assertions.assertEquals(expected, conversion.apply("org.example.ExampleName"));
                                 Assertions.assertEquals(expected, conversion.apply("exampleName"));
                                 Assertions.assertEquals(expected, conversion.apply("org.example.exampleName"));
                                 Assertions.assertEquals(expected, conversion.apply("EXAMPLE_NAME"));
                                 Assertions.assertEquals(expected, conversion.apply("org.example.EXAMPLE_NAME"));
                              })
                     .compile();
   }
}

