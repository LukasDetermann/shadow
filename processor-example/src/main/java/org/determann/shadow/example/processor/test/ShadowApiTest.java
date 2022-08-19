package org.determann.shadow.example.processor.test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.function.UnaryOperator;

import static org.determann.shadow.example.processor.test.TestProcessor.SHADOW_API;

public class ShadowApiTest
{
   @Test
   void to_UpperCamelCaseTest()
   {
      testStringConversion("ExampleName", SHADOW_API::to_UpperCamelCase);
   }

   @Test
   void to_lowerCamelCaseTest()
   {
      testStringConversion("exampleName", SHADOW_API::to_lowerCamelCase);
   }

   @Test
   void to_SCREAMING_SNAKE_CASE_Test()
   {
      testStringConversion("EXAMPLE_NAME", SHADOW_API::to_SCREAMING_SNAKE_CASE);
   }

   private void testStringConversion(String expected, UnaryOperator<String> conversion)
   {
      Assertions.assertEquals(expected, conversion.apply("ExampleName"));
      Assertions.assertEquals(expected, conversion.apply("org.example.ExampleName"));
      Assertions.assertEquals(expected, conversion.apply("exampleName"));
      Assertions.assertEquals(expected, conversion.apply("org.example.exampleName"));
      Assertions.assertEquals(expected, conversion.apply("EXAMPLE_NAME"));
      Assertions.assertEquals(expected, conversion.apply("org.example.EXAMPLE_NAME"));
   }
}

