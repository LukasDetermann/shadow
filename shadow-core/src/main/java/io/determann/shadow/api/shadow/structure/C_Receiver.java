package io.determann.shadow.api.shadow.structure;

import io.determann.shadow.api.dsl.receiver.ReceiverRenderable;
import io.determann.shadow.api.renderer.RenderingContext;
import io.determann.shadow.api.shadow.C_Annotationable;

import static io.determann.shadow.api.Operations.ANNOTATIONABLE_GET_DIRECT_ANNOTATION_USAGES;
import static io.determann.shadow.api.Provider.requestOrEmpty;
import static io.determann.shadow.api.dsl.Dsl.receiver;
import static java.util.Collections.emptyList;

/// A Receiver is a way to annotate `this` in an inner class [Constructor][C_Constructor] or instance [Method][C_Method]
///
/// @see io.determann.shadow.api.Operations#EXECUTABLE_GET_RECEIVER
/// @see io.determann.shadow.api.Operations#RECEIVER_GET_TYPE
/// @see io.determann.shadow.api.Operations#ANNOTATIONABLE_GET_ANNOTATION_USAGES
public interface C_Receiver
      extends C_Annotationable,
              ReceiverRenderable
{

   @Override
   default String renderDeclaration(RenderingContext renderingContext)
   {
      return receiver().annotate(requestOrEmpty(this, ANNOTATIONABLE_GET_DIRECT_ANNOTATION_USAGES).orElse(emptyList()))
                       .renderDeclaration(renderingContext);
   }
}