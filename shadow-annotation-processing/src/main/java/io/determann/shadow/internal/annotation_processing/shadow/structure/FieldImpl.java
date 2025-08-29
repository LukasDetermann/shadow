package io.determann.shadow.internal.annotation_processing.shadow.structure;

import io.determann.shadow.api.annotation_processing.Ap;
import io.determann.shadow.api.annotation_processing.adapter.Adapters;

import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;

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
}
