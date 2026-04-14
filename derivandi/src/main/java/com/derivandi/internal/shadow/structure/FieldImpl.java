package com.derivandi.internal.shadow.structure;

import com.derivandi.api.Ap;
import com.derivandi.api.adapter.Adapters;
import com.derivandi.api.dsl.RenderingContext;
import com.derivandi.api.dsl.field.FieldInitializationStep;
import com.derivandi.api.processor.SimpleContext;

import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;

import static com.derivandi.api.dsl.JavaDsl.field;

public class FieldImpl extends VariableImpl implements Ap.Field
{
   public FieldImpl(SimpleContext context, VariableElement variableElement)
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
