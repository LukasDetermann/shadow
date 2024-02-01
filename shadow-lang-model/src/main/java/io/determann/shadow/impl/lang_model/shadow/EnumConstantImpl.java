package io.determann.shadow.impl.lang_model.shadow;

import io.determann.shadow.api.lang_model.LangModelContext;
import io.determann.shadow.api.shadow.Enum;
import io.determann.shadow.api.shadow.EnumConstant;

import javax.lang.model.element.VariableElement;

public class EnumConstantImpl extends VariableImpl<Enum> implements EnumConstant
{
   public EnumConstantImpl(LangModelContext context, VariableElement variableElement)
   {
      super(context, variableElement);
   }
}
