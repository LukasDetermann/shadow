package io.determann.shadow.api.shadow;

import io.determann.shadow.api.annotation_processing.test.ProcessorTest;
import io.determann.shadow.api.converter.Converter;
import io.determann.shadow.api.converter.TypeConverter;
import io.determann.shadow.api.lang_model.LangModelQueries;
import org.junit.jupiter.api.Test;

import static io.determann.shadow.api.converter.Converter.convert;
import static io.determann.shadow.api.lang_model.LangModelQueries.query;
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
                                                      convert(query(shadowApi.getClassOrThrow("BoundsExample"))
                                                                       .getMethods("extendsExample")
                                                                       .get(0)
                                                                       .getParameterOrThrow("numbers")
                                                                       .getType())
                                                            .toInterface()
                                                            .map(anInterface -> query(anInterface).getGenericTypes().get(0))
                                                            .map(Converter::convert)
                                                            .flatMap(TypeConverter::toWildcard)
                                                            .flatMap(wildcard -> query(wildcard).getExtends())
                                                            .orElseThrow()))
                   .withCodeToCompile("BoundsExample.java", """
                         public class BoundsExample {
                            public static void extendsExample(java.util.List<? extends Number> numbers) {}
                            public static void superExample(java.util.List<? super Number> numbers) {}
                         }
                         """)
                   .compile();
   }

   @Test
   void testGetSupper()
   {
      ProcessorTest.process(shadowApi -> assertEquals(shadowApi.getClassOrThrow("java.lang.Number"),
                                                      convert(query(shadowApi.getClassOrThrow("BoundsExample"))
                                                                       .getMethods("superExample")
                                                                       .get(0)
                                                                       .getParameterOrThrow("numbers")
                                                                       .getType())
                                                            .toInterface()
                                                            .map(anInterface -> query(anInterface).getGenericTypes().get(0))
                                                            .map(Converter::convert)
                                                            .flatMap(TypeConverter::toWildcard)
                                                            .flatMap(wildcard -> LangModelQueries.query(wildcard).getSuper())
                                                            .orElseThrow()))
                   .withCodeToCompile("BoundsExample.java", """
                         public class BoundsExample {
                            public static void extendsExample(java.util.List<? extends Number> numbers) {}
                            public static void superExample(java.util.List<? super Number> numbers) {}
                         }
                         """)
                   .compile();
   }

   @Test
   void testContains()
   {
      ProcessorTest.process(shadowApi ->
                            {
                               assertTrue(query(shadowApi.asExtendsWildcard(shadowApi.getClassOrThrow("java.lang.Number")))
                                                   .contains(shadowApi.getClassOrThrow("java.lang.Long")));

                               assertFalse(query(shadowApi.asExtendsWildcard(shadowApi.getClassOrThrow("java.lang.Long")))
                                                    .contains(shadowApi.getClassOrThrow("java.lang.Number")));

                               assertTrue(query(shadowApi.asSuperWildcard(shadowApi.getClassOrThrow("java.lang.Long")))
                                                   .contains(shadowApi.getClassOrThrow("java.lang.Number")));

                               assertFalse(query(shadowApi.asSuperWildcard(shadowApi.getClassOrThrow("java.lang.Number")))
                                                    .contains(shadowApi.getClassOrThrow("java.lang.Long")));
                            })
                   .compile();
   }

   @Override
   void testRepresentsSameType()
   {
      ProcessorTest.process(shadowApi ->
                            {
                               assertFalse(query(getShadowSupplier().apply(shadowApi)).representsSameType(getShadowSupplier().apply(shadowApi)));
                               assertFalse(query(getShadowSupplier().apply(shadowApi))
                                                 .representsSameType(shadowApi.getClassOrThrow("java.util.jar.Attributes")));
                            })
                   .compile();
   }
}
