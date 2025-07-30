package io.determann.shadow.internal.lang_model.shadow.structure;

import io.determann.shadow.api.lang_model.LM;
import io.determann.shadow.api.lang_model.adapter.Adapters;

import javax.lang.model.element.VariableElement;

public class EnumConstantImpl extends VariableImpl implements LM.EnumConstant
{
   public EnumConstantImpl(LM.Context context, VariableElement variableElement)
   {
      super(context, variableElement);
   }

   @Override
   public LM.Enum getSurrounding()
   {
      return (LM.Enum) Adapters.adapt(getApi(), getElement().getEnclosingElement());
   }
}
