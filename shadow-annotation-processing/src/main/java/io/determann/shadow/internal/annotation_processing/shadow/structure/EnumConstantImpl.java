package io.determann.shadow.internal.annotation_processing.shadow.structure;

import io.determann.shadow.api.annotation_processing.AP;
import io.determann.shadow.api.annotation_processing.adapter.Adapters;

import javax.lang.model.element.VariableElement;

public class EnumConstantImpl extends VariableImpl implements AP.EnumConstant
{
   public EnumConstantImpl(AP.Context context, VariableElement variableElement)
   {
      super(context, variableElement);
   }

   @Override
   public AP.Enum getSurrounding()
   {
      return (AP.Enum) Adapters.adapt(getApi(), getElement().getEnclosingElement());
   }
}
