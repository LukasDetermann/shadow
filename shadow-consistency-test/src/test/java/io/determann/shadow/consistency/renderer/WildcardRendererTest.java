package io.determann.shadow.consistency.renderer;

import io.determann.shadow.api.reflection.ReflectionAdapter;
import io.determann.shadow.api.renderer.Renderer;
import io.determann.shadow.api.shadow.structure.Method;
import io.determann.shadow.api.shadow.structure.Parameter;
import io.determann.shadow.api.shadow.type.Class;
import io.determann.shadow.api.shadow.type.Interface;
import io.determann.shadow.api.shadow.type.Wildcard;
import io.determann.shadow.consistency.test.ConsistencyTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static io.determann.shadow.api.renderer.RenderingContext.DEFAULT;
import static io.determann.shadow.api.shadow.Operations.*;
import static io.determann.shadow.api.shadow.Provider.requestOrThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

class WildcardRendererTest
{
   @Test
   void type()
   {
      ConsistencyTest.<Class>compileTime(context -> context.getClassOrThrow("BoundsExample"))
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
                              Method extendsMethod = requestOrThrow(aClass, DECLARED_GET_METHOD, "extendsExample").get(0);
                              Parameter extendsParameter = requestOrThrow(extendsMethod, EXECUTABLE_GET_PARAMETERS).get(0);
                              Interface extendsParameterType = (Interface) requestOrThrow(extendsParameter, VARIABLE_GET_TYPE);
                              Wildcard extendsExample = ((Wildcard) requestOrThrow(extendsParameterType, INTERFACE_GET_GENERIC_TYPES).get(0));

                              Method superMethod = requestOrThrow(aClass, DECLARED_GET_METHOD, "superExample").get(0);
                              Parameter superParameter = requestOrThrow(superMethod, EXECUTABLE_GET_PARAMETERS).get(0);
                              Interface superParameterType = ((Interface) requestOrThrow(superParameter, VARIABLE_GET_TYPE));
                              Wildcard superExample = ((Wildcard) requestOrThrow(superParameterType, INTERFACE_GET_GENERIC_TYPES).get(0));

                              Method unboundMethod = requestOrThrow(aClass, DECLARED_GET_METHOD, "unboundExample").get(0);
                              Parameter unboundParameter = requestOrThrow(unboundMethod, EXECUTABLE_GET_PARAMETERS).get(0);
                              Interface unboundParameterType = ((Interface) requestOrThrow(unboundParameter, VARIABLE_GET_TYPE));
                              Wildcard unboundExample = ((Wildcard) requestOrThrow(unboundParameterType, INTERFACE_GET_GENERIC_TYPES).get(0));

                              Assertions.assertEquals("? extends Number", Renderer.render(DEFAULT, extendsExample).type());
                              assertEquals("? super Number", Renderer.render(DEFAULT, superExample).type());
                              assertEquals("?", Renderer.render(DEFAULT, unboundExample).type());
                           });
   }
}