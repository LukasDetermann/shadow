package io.determann.shadow.impl.shadow;

import io.determann.shadow.api.ShadowApi;
import io.determann.shadow.api.shadow.Declared;
import io.determann.shadow.api.shadow.Field;
import io.determann.shadow.api.shadow.Shadow;

import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;

public class FieldImpl extends VariableImpl<Declared> implements Field
{
   public FieldImpl(ShadowApi shadowApi, VariableElement variableElement)
   {
      super(shadowApi, variableElement);
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

   @Override
   public Shadow<TypeMirror> erasure()
   {
      return getApi().getShadowFactory().shadowFromType(getApi().getJdkApiContext().types().erasure(getMirror()));
   }
}
