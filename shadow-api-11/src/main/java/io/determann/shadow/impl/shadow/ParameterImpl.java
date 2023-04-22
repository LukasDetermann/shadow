package io.determann.shadow.impl.shadow;

import io.determann.shadow.api.ShadowApi;
import io.determann.shadow.api.shadow.Executable;
import io.determann.shadow.api.shadow.Parameter;
import io.determann.shadow.api.shadow.Shadow;

import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;

public class ParameterImpl extends VariableImpl<Executable> implements Parameter
{
   public ParameterImpl(ShadowApi shadowApi, VariableElement variableElement)
   {
      super(shadowApi, variableElement);
   }

   @Override
   public Shadow<TypeMirror> erasure()
   {
      return getApi().getShadowFactory().shadowFromType(getApi().getJdkApiContext().getProcessingEnv().getTypeUtils().erasure(getMirror()));
   }
}
