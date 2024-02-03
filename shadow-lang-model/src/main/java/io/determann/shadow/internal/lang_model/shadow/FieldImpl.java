package io.determann.shadow.internal.lang_model.shadow;

import io.determann.shadow.api.lang_model.LangModelContext;
import io.determann.shadow.api.shadow.Declared;
import io.determann.shadow.api.shadow.Field;

import javax.lang.model.element.VariableElement;

public class FieldImpl extends VariableImpl<Declared> implements Field
{
   public FieldImpl(LangModelContext context, VariableElement variableElement)
   {
      super(context, variableElement);
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
