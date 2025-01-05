package io.determann.shadow.internal.lang_model.shadow.structure;

import io.determann.shadow.api.lang_model.LM_Context;
import io.determann.shadow.api.lang_model.adapter.LM_Adapters;
import io.determann.shadow.api.lang_model.shadow.structure.LM_EnumConstant;
import io.determann.shadow.api.lang_model.shadow.type.LM_Enum;

import javax.lang.model.element.VariableElement;

public class EnumConstantImpl extends VariableImpl implements LM_EnumConstant
{
   public EnumConstantImpl(LM_Context context, VariableElement variableElement)
   {
      super(context, variableElement);
   }

   @Override
   public LM_Enum getSurrounding()
   {
      return (LM_Enum) LM_Adapters.adapt(getApi(), getElement().getEnclosingElement());
   }
}
