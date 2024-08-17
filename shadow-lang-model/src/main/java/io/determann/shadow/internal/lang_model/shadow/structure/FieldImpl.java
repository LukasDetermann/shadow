package io.determann.shadow.internal.lang_model.shadow.structure;

import io.determann.shadow.api.lang_model.LangModelAdapter;
import io.determann.shadow.api.lang_model.LangModelContext;
import io.determann.shadow.api.lang_model.shadow.structure.FieldLangModel;
import io.determann.shadow.api.lang_model.shadow.type.DeclaredLangModel;

import javax.lang.model.element.VariableElement;

public class FieldImpl extends VariableImpl implements FieldLangModel
{
   public FieldImpl(LangModelContext context, VariableElement variableElement)
   {
      super(context, variableElement);
   }

   @Override
   public DeclaredLangModel getSurrounding()
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
