package io.determann.shadow.api.shadow.structure;

import io.determann.shadow.api.dsl.method.MethodReceiverStep;
import io.determann.shadow.api.dsl.method.MethodRenderable;
import io.determann.shadow.api.renderer.RenderingContext;
import io.determann.shadow.api.shadow.modifier.*;

import static io.determann.shadow.api.Operations.*;
import static io.determann.shadow.api.Provider.requestOrEmpty;
import static io.determann.shadow.api.Provider.requestOrThrow;
import static io.determann.shadow.api.dsl.Dsl.method;
import static java.util.Collections.emptyList;
import static java.util.Collections.emptySet;

public interface C_Method
      extends C_Executable,
              C_StaticModifiable,
              C_DefaultModifiable,
              C_AccessModifiable,
              C_AbstractModifiable,
              C_FinalModifiable,
              C_StrictfpModifiable,
              C_NativeModifiable,
              MethodRenderable
{
   @Override
   default String renderDeclaration(RenderingContext renderingContext)
   {
      MethodReceiverStep receiverStep = method()
            .annotate(requestOrEmpty(this, ANNOTATIONABLE_GET_DIRECT_ANNOTATION_USAGES).orElse(emptyList()))
            .modifier(requestOrEmpty(this, MODIFIABLE_GET_MODIFIERS).orElse(emptySet()))
            .generic(requestOrEmpty(this, EXECUTABLE_GET_GENERICS).orElse(emptyList()))
            .result(requestOrThrow(this, METHOD_GET_RESULT))
            .name(requestOrThrow(this, NAMEABLE_GET_NAME));

      return requestOrEmpty(this, EXECUTABLE_GET_RECEIVER).map(receiverStep::receiver).orElse(receiverStep)
                                                          .parameter(requestOrThrow(this, EXECUTABLE_GET_PARAMETERS))
                                                          .throws_(requestOrEmpty(this, EXECUTABLE_GET_THROWS).orElse(emptyList()))
                                                          .renderDeclaration(renderingContext);
   }
}