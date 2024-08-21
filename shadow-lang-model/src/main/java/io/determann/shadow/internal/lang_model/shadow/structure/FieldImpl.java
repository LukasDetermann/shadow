package io.determann.shadow.internal.lang_model.shadow.structure;

import io.determann.shadow.api.lang_model.LM_Adapter;
import io.determann.shadow.api.lang_model.LM_Context;
import io.determann.shadow.api.lang_model.shadow.structure.LM_Field;
import io.determann.shadow.api.lang_model.shadow.type.LM_Declared;

import javax.lang.model.element.VariableElement;

public class FieldImpl extends VariableImpl implements LM_Field
{
   public FieldImpl(LM_Context context, VariableElement variableElement)
   {
      super(context, variableElement);
   }

   @Override
   public LM_Declared getSurrounding()
   {
      return LM_Adapter.generalize(getApi(), getElement().getEnclosingElement());
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
