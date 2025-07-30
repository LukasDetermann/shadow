package io.determann.shadow.internal.lang_model.shadow.structure;

import io.determann.shadow.api.lang_model.LM;
import io.determann.shadow.api.lang_model.adapter.Adapters;

import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;

public class FieldImpl extends VariableImpl implements LM.Field
{
   public FieldImpl(LM.Context context, VariableElement variableElement)
   {
      super(context, variableElement);
   }

   @Override
   public LM.Declared getSurrounding()
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
