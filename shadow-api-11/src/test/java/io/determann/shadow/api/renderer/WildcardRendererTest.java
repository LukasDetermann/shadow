package io.determann.shadow.api.renderer;

import io.determann.shadow.api.reflection.ReflectionAdapter;
import io.determann.shadow.api.shadow.Wildcard;
import io.determann.shadow.consistency.ConsistencyTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static io.determann.shadow.api.converter.Converter.convert;
import static io.determann.shadow.api.renderer.RenderingContext.DEFAULT;
import static org.junit.jupiter.api.Assertions.assertEquals;

class WildcardRendererTest
{
   @Test
   void type()
   {
      ConsistencyTest.compileTime(context -> context.getClassOrThrow("BoundsExample"))
                     .runtime(stringClassFunction -> ReflectionAdapter.getShadow(stringClassFunction.apply("BoundsExample")))
                     .withCode("BoundsExample.java", "public class BoundsExample {\n" +
                                                     "   public static void extendsExample(java.util.List<? extends Number> numbers) {}\n" +
                                                     "   public static void superExample(java.util.List<? super Number> numbers) {}\n" +
                                                     "   public static void unboundExample(java.util.List<?> things) {}\n" +
                                                     "}\n")
                     .test(aClass ->
                           {
                              Wildcard extendsExample = convert(convert(aClass.getMethods("extendsExample")
                                                                              .get(0)
                                                                              .getParameters()
                                                                              .get(0)
                                                                              .getType())
                                                                      .toInterfaceOrThrow()
                                                                      .getGenericTypes()
                                                                      .get(0))
                                    .toWildcardOrThrow();

                              Wildcard superExample = convert(convert(aClass.getMethods("superExample")
                                                                            .get(0)
                                                                            .getParameters()
                                                                            .get(0)
                                                                            .getType())
                                                                    .toInterfaceOrThrow()
                                                                    .getGenericTypes()
                                                                    .get(0))
                                    .toWildcardOrThrow();

                              Wildcard unboundExample = convert(convert(aClass.getMethods("unboundExample")
                                                                              .get(0)
                                                                              .getParameters()
                                                                              .get(0)
                                                                              .getType())
                                                                      .toInterfaceOrThrow()
                                                                      .getGenericTypes()
                                                                      .get(0))
                                    .toWildcardOrThrow();

                              Assertions.assertEquals("? extends Number", Renderer.render(DEFAULT, extendsExample).type());
                              assertEquals("? super Number", Renderer.render(DEFAULT, superExample).type());
                              assertEquals("?", Renderer.render(DEFAULT, unboundExample).type());
                           });
   }
}