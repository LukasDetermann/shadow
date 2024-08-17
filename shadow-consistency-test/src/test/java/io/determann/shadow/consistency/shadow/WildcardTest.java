package io.determann.shadow.consistency.shadow;

import io.determann.shadow.api.annotation_processing.test.ProcessorTest;
import io.determann.shadow.api.lang_model.shadow.type.InterfaceLangModel;
import io.determann.shadow.api.lang_model.shadow.type.WildcardLangModel;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class WildcardTest extends ShadowTest<WildcardLangModel>
{
   WildcardTest()
   {
      super(context -> context.getConstants().getUnboundWildcard());
   }

   @Test
   void testGetExtends()
   {
      ProcessorTest.process(context -> assertEquals(context.getClassOrThrow("java.lang.Number"),
                                                      Optional.of(((InterfaceLangModel) context.getClassOrThrow("BoundsExample")
                                                                                                 .getMethods("extendsExample")
                                                                                                 .get(0)
                                                                                                 .getParameterOrThrow("numbers")
                                                                                                 .getType()))
                                                              .map(anInterface -> anInterface.getGenericTypes().get(0))
                                                              .map(WildcardLangModel.class::cast)
                                                              .flatMap(WildcardLangModel::getExtends)
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
                                                      Optional.of(((InterfaceLangModel) context.getClassOrThrow("BoundsExample")
                                                                                                 .getMethods("superExample")
                                                                                                 .get(0)
                                                                                           .getParameterOrThrow("numbers")
                                                                    .getType()))
                                                              .map(anInterface -> anInterface.getGenericTypes().get(0))
                                                              .map(WildcardLangModel.class::cast)
                                                              .flatMap(WildcardLangModel::getSuper)
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
