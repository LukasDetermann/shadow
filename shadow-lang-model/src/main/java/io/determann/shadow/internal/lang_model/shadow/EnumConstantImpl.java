package io.determann.shadow.internal.lang_model.shadow;

import io.determann.shadow.api.lang_model.LangModelAdapter;
import io.determann.shadow.api.lang_model.LangModelContext;
import io.determann.shadow.api.shadow.Enum;
import io.determann.shadow.api.shadow.EnumConstant;

import javax.lang.model.element.VariableElement;

public class EnumConstantImpl extends VariableImpl implements EnumConstant
{
   public EnumConstantImpl(LangModelContext context, VariableElement variableElement)
   {
      super(context, variableElement);
   }

   @Override
   public Enum getSurrounding()
   {
      return LangModelAdapter.generalize(getApi(), getElement().getEnclosingElement());
   }
}
