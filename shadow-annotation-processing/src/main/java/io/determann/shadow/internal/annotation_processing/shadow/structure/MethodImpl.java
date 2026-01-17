package io.determann.shadow.internal.annotation_processing.shadow.structure;

import io.determann.shadow.api.annotation_processing.Ap;
import io.determann.shadow.api.annotation_processing.dsl.RenderingContext;
import io.determann.shadow.api.annotation_processing.dsl.method.MethodReceiverStep;

import javax.lang.model.element.ExecutableElement;

import static io.determann.shadow.api.annotation_processing.dsl.JavaDsl.method;

public class MethodImpl
      extends ExecutableImpl
      implements Ap.Method
{
   public MethodImpl(Ap.Context context, ExecutableElement executableElement)
   {
      super(context, executableElement);
   }

   @Override
   public String renderDeclaration(RenderingContext renderingContext)
   {
      MethodReceiverStep receiverStep = method()
            .annotate(getDirectAnnotationUsages())
            .modifier(getModifiers())
            .genericDeclaration(getGenericDeclarations())
            .result(getResult())
            .name(getName());

      return getReceiver().map(receiverStep::receiver).orElse(receiverStep)
                          .parameter(getParameters())
                          .throws_(getThrows())
                          .renderDeclaration(renderingContext);
   }

   @Override
   public String renderName(RenderingContext renderingContext)
   {
      return getName();
   }
}
