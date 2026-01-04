package io.determann.shadow.internal.annotation_processing.shadow.structure;

import io.determann.shadow.api.annotation_processing.Ap;
import io.determann.shadow.api.annotation_processing.adapter.Adapters;
import io.determann.shadow.api.annotation_processing.dsl.RenderingContext;
import io.determann.shadow.api.annotation_processing.dsl.field.FieldInitializationStep;

import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;

import static io.determann.shadow.api.annotation_processing.dsl.Dsl.field;

public class FieldImpl extends VariableImpl implements Ap.Field
{
   public FieldImpl(Ap.Context context, VariableElement variableElement)
   {
      super(context, variableElement);
   }

   @Override
   public Ap.Declared getSurrounding()
   {
      return Adapters.adapt(getApi(), ((TypeElement) getElement().getEnclosingElement()));
   }

   @Override
   public boolean isConstant()
   {
      return getElement().getConstantValue() != null;
   }

   @Override
   public Object getConstantValue()
   {
      return getElement().getConstantValue();
   }

   @Override
   public String renderDeclaration(RenderingContext renderingContext)
   {
      FieldInitializationStep initializationStep = field()
            .annotate(getDirectAnnotationUsages())
            .modifier(getModifiers())
            .type(getType())
            .name(getName());

      if (!isConstant())
      {
         return initializationStep.renderDeclaration(renderingContext);
      }
      return initializationStep.initializer(getConstantValue().toString())
                               .renderDeclaration(renderingContext);
   }

   @Override
   public String renderName(RenderingContext renderingContext)
   {
      return getName();
   }
}
