package org.determann.shadow.api.shadow;

import org.determann.shadow.api.converter.ShadowConverter;
import org.determann.shadow.api.test.CompilationTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WildcardTest extends ShadowTest<Wildcard>
{
   WildcardTest()
   {
      super(shadowApi -> shadowApi.getConstants().getUnboundWildcard());
   }

   @Test
   void testGetExtends()
   {
      CompilationTest.process(shadowApi -> assertEquals(shadowApi.getClass("java.lang.Number"),
                                                        shadowApi.convert(shadowApi.getClass("BoundsExample")
                                                                                   .getMethods("extendsExample")
                                                                                   .get(0)
                                                                                   .getParameter("numbers")
                                                                                   .getType())
                                                                 .toOptionalInterface()
                                                                 .map(anInterface -> anInterface.getGenerics().get(0))
                                                                 .map(shadowApi::convert)
                                                                 .flatMap(ShadowConverter::toOptionalWildcard)
                                                                 .flatMap(Wildcard::getExtends)
                                                                 .orElseThrow(IllegalStateException::new)))
                     .withCodeToCompile("BoundsExample.java",
                                        " public class BoundsExample {\n" +
                                        "                              public static void extendsExample(java.util.List<? extends Number> numbers) {}\n" +
                                        "                              public static void superExample(java.util.List<? super Number> numbers) {}\n" +
                                        "                           }")
                     .compile();
   }

   @Test
   void testGetSupper()
   {
      CompilationTest.process(shadowApi -> assertEquals(shadowApi.getClass("java.lang.Number"),
                                                        shadowApi.convert(shadowApi.getClass("BoundsExample")
                                                                                   .getMethods("superExample")
                                                                                   .get(0)
                                                                                   .getParameter("numbers")
                                                                                   .getType())
                                                                 .toOptionalInterface()
                                                                 .map(anInterface -> anInterface.getGenerics().get(0))
                                                                 .map(shadowApi::convert)
                                                                 .flatMap(ShadowConverter::toOptionalWildcard)
                                                                 .flatMap(Wildcard::getSuper)
                                                                 .orElseThrow(IllegalStateException::new)))
                     .withCodeToCompile("BoundsExample.java",
                                        "                      public class BoundsExample {\n" +
                                        "                              public static void extendsExample(java.util.List<? extends Number> numbers) {}\n" +
                                        "                              public static void superExample(java.util.List<? super Number> numbers) {}\n" +
                                        "                           }")
                     .compile();
   }

   @Test
   void testContains()
   {
      CompilationTest.process(shadowApi ->
                              {
                                 assertTrue(shadowApi.convert(shadowApi.getClass("java.lang.Number"))
                                                     .asExtendsWildcard()
                                                     .contains(shadowApi.getClass("java.lang.Long")));

                                 assertFalse(shadowApi.convert(shadowApi.getClass("java.lang.Long"))
                                                      .asExtendsWildcard()
                                                      .contains(shadowApi.getClass("java.lang.Number")));

                                 assertTrue(shadowApi.convert(shadowApi.getClass("java.lang.Long"))
                                                     .asSuperWildcard()
                                                     .contains(shadowApi.getClass("java.lang.Number")));

                                 assertFalse(shadowApi.convert(shadowApi.getClass("java.lang.Number"))
                                                      .asSuperWildcard()
                                                      .contains(shadowApi.getClass("java.lang.Long")));
                              })
                     .compile();
   }

   @Override
   void testRepresentsSameType()
   {
      CompilationTest.process(shadowApi ->
                              {
                                 assertFalse(getShadowSupplier().apply(shadowApi).representsSameType(getShadowSupplier().apply(shadowApi)));
                                 assertFalse(getShadowSupplier().apply(shadowApi)
                                                                .representsSameType(shadowApi.getClass("java.util.jar.Attributes")));
                              })
                     .compile();
   }
}
