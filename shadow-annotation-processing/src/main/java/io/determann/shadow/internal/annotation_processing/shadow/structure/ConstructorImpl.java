package io.determann.shadow.internal.annotation_processing.shadow.structure;

import io.determann.shadow.api.annotation_processing.Ap;
import io.determann.shadow.api.annotation_processing.dsl.RenderingContext;
import io.determann.shadow.api.annotation_processing.dsl.constructor.ConstructorGenericStep;
import io.determann.shadow.api.annotation_processing.dsl.constructor.ConstructorParameterStep;
import io.determann.shadow.api.annotation_processing.dsl.constructor.ConstructorReceiverStep;

import javax.lang.model.element.ExecutableElement;

import static io.determann.shadow.api.annotation_processing.dsl.JavaDsl.constructor;

public class ConstructorImpl
      extends ExecutableImpl
      implements Ap.Constructor
{
   public ConstructorImpl(Ap.Context context, ExecutableElement executableElement)
   {
      super(context, executableElement);
   }

   @Override
   public String renderDeclaration(RenderingContext renderingContext)
   {
      ConstructorGenericStep genericStep = constructor()
            .annotate(getDirectAnnotationUsages())
            .modifier(getModifiers()).
            genericDeclaration(getGenericDeclarations());

      Ap.Declared type = getSurrounding();
      ConstructorReceiverStep receiverStep = switch (type)
      {
         case Ap.Class cClass -> genericStep.type(cClass);
         case Ap.Enum cEnum -> genericStep.type(cEnum);
         case Ap.Record cRecord -> genericStep.type(cRecord);
         default -> throw new IllegalStateException("Unexpected value: " + type);
      };

      ConstructorParameterStep parameterStep = getReceiver()
            .map(receiverStep::receiver)
            .orElse(receiverStep);

      return parameterStep.parameter(getParameters())
                          .throws_(getThrows())
                          .renderDeclaration(renderingContext);
   }
}
