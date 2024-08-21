package io.determann.shadow.consistency.shadow;

import io.determann.shadow.api.annotation_processing.test.ProcessorTest;
import io.determann.shadow.api.lang_model.shadow.type.LM_Interface;
import io.determann.shadow.api.lang_model.shadow.type.LM_Wildcard;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class WildcardTest extends ShadowTest<LM_Wildcard>
{
   WildcardTest()
   {
      super(context -> context.getConstants().getUnboundWildcard());
   }

   @Test
   void testGetExtends()
   {
      ProcessorTest.process(context -> assertEquals(context.getClassOrThrow("java.lang.Number"),
                                                      Optional.of(((LM_Interface) context.getClassOrThrow("BoundsExample")
                                                                                         .getMethods("extendsExample")
                                                                                         .get(0)
                                                                                         .getParameterOrThrow("numbers")
                                                                                         .getType()))
                                                              .map(anInterface -> anInterface.getGenericTypes().get(0))
                                                              .map(LM_Wildcard.class::cast)
                                                              .flatMap(LM_Wildcard::getExtends)
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
      ProcessorTest.process(context -> assertEquals(context.getClassOrThrow("java.lang.Number"),
                                                      Optional.of(((LM_Interface) context.getClassOrThrow("BoundsExample")
                                                                                         .getMethods("superExample")
                                                                                         .get(0)
                                                                                         .getParameterOrThrow("numbers")
                                                                                         .getType()))
                                                              .map(anInterface -> anInterface.getGenericTypes().get(0))
                                                              .map(LM_Wildcard.class::cast)
                                                              .flatMap(LM_Wildcard::getSuper)
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
      ProcessorTest.process(context ->
                            {
                               assertTrue(context.asExtendsWildcard(context.getClassOrThrow("java.lang.Number"))
                                                   .contains(context.getClassOrThrow("java.lang.Long")));

                               assertFalse(context.asExtendsWildcard(context.getClassOrThrow("java.lang.Long"))
                                                    .contains(context.getClassOrThrow("java.lang.Number")));

                               assertTrue(context.asSuperWildcard(context.getClassOrThrow("java.lang.Long"))
                                                   .contains(context.getClassOrThrow("java.lang.Number")));

                               assertFalse(context.asSuperWildcard(context.getClassOrThrow("java.lang.Number"))
                                                    .contains(context.getClassOrThrow("java.lang.Long")));
                            })
                   .compile();
   }

   @Override
   void testRepresentsSameType()
   {
      ProcessorTest.process(context ->
                            {
                               assertFalse(getShadowSupplier().apply(context).representsSameType(getShadowSupplier().apply(context)));
                               assertFalse(getShadowSupplier().apply(context)
                                                 .representsSameType(context.getClassOrThrow("java.util.jar.Attributes")));
                            })
                   .compile();
   }
}
