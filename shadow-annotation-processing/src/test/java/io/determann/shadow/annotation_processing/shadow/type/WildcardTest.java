package io.determann.shadow.annotation_processing.shadow.type;

import io.determann.shadow.api.annotation_processing.Ap;
import io.determann.shadow.api.annotation_processing.test.ProcessorTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class WildcardTest
{
   @Test
   void testGetExtends()
   {
      ProcessorTest.process(context ->
                            {
                               Ap.Class boundsExample = context.getClassOrThrow("BoundsExample");
                               Ap.Method extendsExample = boundsExample.getMethods("extendsExample").get(0);
                               Ap.Parameter parameter = extendsExample.getParameterOrThrow("numbers");
                               Ap.Interface parameterType = ((Ap.Interface) parameter.getType());
                               Ap.Wildcard wildcard = (Ap.Wildcard) parameterType.getGenericUsages().get(0);

                               assertEquals(context.getClassOrThrow("java.lang.Number"), wildcard.getExtends().orElseThrow());
                            })
                   .withCodeToCompile("BoundsExample.java", """
                                                            public class BoundsExample {
                                                               public static void extendsExample(java.util.List<? extends Number> numbers) {}
                                                            }
                                                            """)
                   .compile();
   }

   @Test
   void testGetSupper()
   {
      ProcessorTest.process(context ->
                            {
                               Ap.Class boundsExample = context.getClassOrThrow("BoundsExample");
                               Ap.Method extendsExample = boundsExample.getMethods("superExample").get(0);
                               Ap.Parameter parameter = extendsExample.getParameterOrThrow("numbers");
                               Ap.Interface parameterType = ((Ap.Interface) parameter.getType());
                               Ap.Wildcard wildcard = (Ap.Wildcard) parameterType.getGenericUsages().get(0);

                               assertEquals(context.getClassOrThrow("java.lang.Number"), wildcard.getSuper().orElseThrow());
                            })
                   .withCodeToCompile("BoundsExample.java", """
                                                            public class BoundsExample {
                                                               public static void superExample(java.util.List<? super Number> numbers) {}
                                                            }
                                                            """)
                   .compile();
   }
}
