package io.determann.shadow.internal.annotation_processing.shadow.structure;

import io.determann.shadow.api.annotation_processing.Ap;
import io.determann.shadow.api.annotation_processing.adapter.Adapters;

import javax.lang.model.element.VariableElement;

public class EnumConstantImpl extends VariableImpl implements Ap.EnumConstant
{
   public EnumConstantImpl(Ap.Context context, VariableElement variableElement)
   {
      super(context, variableElement);
   }

   @Override
   public Ap.Enum getSurrounding()
   {
      return (Ap.Enum) Adapters.adapt(getApi(), getElement().getEnclosingElement());
   }
}
