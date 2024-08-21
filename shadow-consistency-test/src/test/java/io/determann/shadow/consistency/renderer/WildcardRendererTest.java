package io.determann.shadow.consistency.renderer;

import io.determann.shadow.api.reflection.R_Adapter;
import io.determann.shadow.api.renderer.Renderer;
import io.determann.shadow.api.shadow.structure.C_Method;
import io.determann.shadow.api.shadow.structure.C_Parameter;
import io.determann.shadow.api.shadow.type.C_Class;
import io.determann.shadow.api.shadow.type.C_Interface;
import io.determann.shadow.api.shadow.type.C_Wildcard;
import io.determann.shadow.consistency.test.ConsistencyTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static io.determann.shadow.api.Operations.*;
import static io.determann.shadow.api.Provider.requestOrThrow;
import static io.determann.shadow.api.renderer.RenderingContext.DEFAULT;
import static org.junit.jupiter.api.Assertions.assertEquals;

class WildcardRendererTest
{
   @Test
   void type()
   {
      ConsistencyTest.<C_Class>compileTime(context -> context.getClassOrThrow("BoundsExample"))
                     .runtime(stringClassFunction -> R_Adapter.generalize(stringClassFunction.apply("BoundsExample")))
                     .withCode("BoundsExample.java", """
                           public class BoundsExample {
                              public static void extendsExample(java.util.List<? extends Number> numbers) {}
                              public static void superExample(java.util.List<? super Number> numbers) {}
                              public static void unboundExample(java.util.List<?> things) {}
                           }
                           """)
                     .test(aClass ->
                           {
                              C_Method extendsMethod = requestOrThrow(aClass, DECLARED_GET_METHOD, "extendsExample").get(0);
                              C_Parameter extendsParameter = requestOrThrow(extendsMethod, EXECUTABLE_GET_PARAMETERS).get(0);
                              C_Interface extendsParameterType = (C_Interface) requestOrThrow(extendsParameter, VARIABLE_GET_TYPE);
                              C_Wildcard extendsExample = ((C_Wildcard) requestOrThrow(extendsParameterType, INTERFACE_GET_GENERIC_TYPES).get(0));

                              C_Method superMethod = requestOrThrow(aClass, DECLARED_GET_METHOD, "superExample").get(0);
                              C_Parameter superParameter = requestOrThrow(superMethod, EXECUTABLE_GET_PARAMETERS).get(0);
                              C_Interface superParameterType = ((C_Interface) requestOrThrow(superParameter, VARIABLE_GET_TYPE));
                              C_Wildcard superExample = ((C_Wildcard) requestOrThrow(superParameterType, INTERFACE_GET_GENERIC_TYPES).get(0));

                              C_Method unboundMethod = requestOrThrow(aClass, DECLARED_GET_METHOD, "unboundExample").get(0);
                              C_Parameter unboundParameter = requestOrThrow(unboundMethod, EXECUTABLE_GET_PARAMETERS).get(0);
                              C_Interface unboundParameterType = ((C_Interface) requestOrThrow(unboundParameter, VARIABLE_GET_TYPE));
                              C_Wildcard unboundExample = ((C_Wildcard) requestOrThrow(unboundParameterType, INTERFACE_GET_GENERIC_TYPES).get(0));

                              Assertions.assertEquals("? extends Number", Renderer.render(DEFAULT, extendsExample).type());
                              assertEquals("? super Number", Renderer.render(DEFAULT, superExample).type());
                              assertEquals("?", Renderer.render(DEFAULT, unboundExample).type());
                           });
   }
}