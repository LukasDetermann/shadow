package io.determann.shadow.consistency.renderer;

import io.determann.shadow.api.reflection.ReflectionAdapter;
import io.determann.shadow.api.renderer.Renderer;
import io.determann.shadow.api.shadow.type.Wildcard;
import io.determann.shadow.consistency.test.ConsistencyTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static io.determann.shadow.api.converter.Converter.convert;
import static io.determann.shadow.api.renderer.RenderingContext.DEFAULT;
import static io.determann.shadow.api.shadow.Operations.*;
import static io.determann.shadow.api.shadow.Provider.requestOrThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

class WildcardRendererTest
{
   @Test
   void type()
   {
      ConsistencyTest.compileTime(context -> context.getClassOrThrow("BoundsExample"))
                     .runtime(stringClassFunction -> ReflectionAdapter.generalize(stringClassFunction.apply("BoundsExample")))
                     .withCode("BoundsExample.java", """
                           public class BoundsExample {
                              public static void extendsExample(java.util.List<? extends Number> numbers) {}
                              public static void superExample(java.util.List<? super Number> numbers) {}
                              public static void unboundExample(java.util.List<?> things) {}
                           }
                           """)
                     .test(aClass ->
                           {
                              Wildcard extendsExample = convert(requestOrThrow(convert(
                                    requestOrThrow(requestOrThrow(requestOrThrow(aClass,
                                                                  DECLARED_GET_METHOD,
                                                                  "extendsExample").get(0), EXECUTABLE_GET_PARAMETERS).get(0),
                                                   VARIABLE_GET_TYPE)).toInterfaceOrThrow(), INTERFACE_GET_GENERIC_TYPES)
                                                                      .get(0))
                                    .toWildcardOrThrow();

                              Wildcard superExample = convert(requestOrThrow(convert(requestOrThrow(requestOrThrow(requestOrThrow(aClass, DECLARED_GET_METHOD, "superExample").get(0), EXECUTABLE_GET_PARAMETERS)
                                                                            .get(0), VARIABLE_GET_TYPE))
                                                                    .toInterfaceOrThrow(), INTERFACE_GET_GENERIC_TYPES)
                                                                    .get(0))
                                    .toWildcardOrThrow();

                              Wildcard unboundExample = convert(requestOrThrow(convert(requestOrThrow(requestOrThrow(requestOrThrow(aClass, DECLARED_GET_METHOD, "unboundExample").get(0), EXECUTABLE_GET_PARAMETERS)
                                                                              .get(0), VARIABLE_GET_TYPE))
                                                                      .toInterfaceOrThrow(), INTERFACE_GET_GENERIC_TYPES)
                                                                      .get(0))
                                    .toWildcardOrThrow();

                              Assertions.assertEquals("? extends Number", Renderer.render(DEFAULT, extendsExample).type());
                              assertEquals("? super Number", Renderer.render(DEFAULT, superExample).type());
                              assertEquals("?", Renderer.render(DEFAULT, unboundExample).type());
                           });
   }
}