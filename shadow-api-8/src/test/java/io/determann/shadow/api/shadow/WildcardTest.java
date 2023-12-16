package io.determann.shadow.api.shadow;

import io.determann.shadow.api.converter.Converter;
import io.determann.shadow.api.converter.ShadowConverter;
import io.determann.shadow.api.test.ProcessorTest;
import org.junit.jupiter.api.Test;

import static io.determann.shadow.api.converter.Converter.convert;
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
      ProcessorTest.process(shadowApi -> assertEquals(shadowApi.getClassOrThrow("java.lang.Number"),
                                                      convert(shadowApi.getClassOrThrow("BoundsExample")
                                                                       .getMethods("extendsExample")
                                                                       .get(0)
                                                                       .getParameterOrThrow("numbers")
                                                                       .getType())
                                                            .toInterface()
                                                            .map(anInterface -> anInterface.getGenericTypes().get(0))
                                                            .map(Converter::convert)
                                                            .flatMap(ShadowConverter::toWildcard)
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
      ProcessorTest.process(shadowApi -> assertEquals(shadowApi.getClassOrThrow("java.lang.Number"),
                                                      convert(shadowApi.getClassOrThrow("BoundsExample")
                                                                       .getMethods("superExample")
                                                                       .get(0)
                                                                       .getParameterOrThrow("numbers")
                                                                       .getType())
                                                            .toInterface()
                                                            .map(anInterface -> anInterface.getGenericTypes().get(0))
                                                            .map(Converter::convert)
                                                            .flatMap(ShadowConverter::toWildcard)
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
      ProcessorTest.process(shadowApi ->
                            {
                               assertTrue(shadowApi.asExtendsWildcard(shadowApi.getClassOrThrow("java.lang.Number"))
                                                   .contains(shadowApi.getClassOrThrow("java.lang.Long")));

                               assertFalse(shadowApi.asExtendsWildcard(shadowApi.getClassOrThrow("java.lang.Long"))
                                                    .contains(shadowApi.getClassOrThrow("java.lang.Number")));

                               assertTrue(shadowApi.asSuperWildcard(shadowApi.getClassOrThrow("java.lang.Long"))
                                                   .contains(shadowApi.getClassOrThrow("java.lang.Number")));

                               assertFalse(shadowApi.asSuperWildcard(shadowApi.getClassOrThrow("java.lang.Number"))
                                                    .contains(shadowApi.getClassOrThrow("java.lang.Long")));
                            })
                   .compile();
   }

   @Override
   void testRepresentsSameType()
   {
      ProcessorTest.process(shadowApi ->
                            {
                               assertFalse(getShadowSupplier().apply(shadowApi).representsSameType(getShadowSupplier().apply(shadowApi)));
                               assertFalse(getShadowSupplier().apply(shadowApi)
                                                              .representsSameType(shadowApi.getClassOrThrow("java.util.jar.Attributes")));
                            })
                   .compile();
   }
}
