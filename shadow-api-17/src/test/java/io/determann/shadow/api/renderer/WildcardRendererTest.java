package io.determann.shadow.api.renderer;

import io.determann.shadow.api.ShadowApi;
import io.determann.shadow.api.shadow.Class;
import io.determann.shadow.api.shadow.Wildcard;
import io.determann.shadow.api.test.ProcessorTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static io.determann.shadow.api.ShadowApi.convert;
import static org.junit.jupiter.api.Assertions.assertEquals;

class WildcardRendererTest
{
   @Test
   void type()
   {
      ProcessorTest.process(shadowApi ->
                            {
                               Class boundsExample = shadowApi.getClassOrThrow("BoundsExample");

                               Wildcard extendsExample = convert(convert(boundsExample.getMethods("extendsExample")
                                                                                      .get(0)
                                                                                      .getParameters()
                                                                                      .get(0)
                                                                                      .getType())
                                                                       .toInterfaceOrThrow()
                                                                       .getGenerics()
                                                                       .get(0))
                                     .toWildcardOrThrow();

                               Wildcard superExample = convert(convert(boundsExample.getMethods("superExample")
                                                                                      .get(0)
                                                                                      .getParameters()
                                                                                      .get(0)
                                                                                      .getType())
                                                                       .toInterfaceOrThrow()
                                                                       .getGenerics()
                                                                       .get(0))
                                     .toWildcardOrThrow();

                               Wildcard unboundExample = convert(convert(boundsExample.getMethods("unboundExample")
                                                                                      .get(0)
                                                                                      .getParameters()
                                                                                      .get(0)
                                                                                      .getType())
                                                                       .toInterfaceOrThrow()
                                                                       .getGenerics()
                                                                       .get(0))
                                     .toWildcardOrThrow();

                               Assertions.assertEquals("? extends Number", ShadowApi.render(extendsExample).type());
                               assertEquals("? super Number", ShadowApi.render(superExample).type());
                               assertEquals("?", ShadowApi.render(unboundExample).type());
                            })
                   .withCodeToCompile("BoundsExample.java", """
                           public class BoundsExample {
                              public static void extendsExample(java.util.List<? extends Number> numbers) {}
                              public static void superExample(java.util.List<? super Number> numbers) {}
                              public static void unboundExample(java.util.List<?> things) {}
                           }
                           """)
                   .compile();
   }
}