package io.determann.shadow.internal.lang_model.shadow;

import io.determann.shadow.api.lang_model.LangModelAdapter;
import io.determann.shadow.api.lang_model.LangModelContext;
import io.determann.shadow.api.lang_model.query.FieldLangModel;
import io.determann.shadow.api.shadow.Declared;

import javax.lang.model.element.VariableElement;

public class FieldImpl extends VariableImpl implements FieldLangModel
{
   public FieldImpl(LangModelContext context, VariableElement variableElement)
   {
      super(context, variableElement);
   }

   @Override
   public Declared getSurrounding()
   {
      return LangModelAdapter.generalize(getApi(), getElement().getEnclosingElement());
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
