package io.determann.shadow.impl.shadow;

import io.determann.shadow.api.MirrorAdapter;
import io.determann.shadow.api.ShadowApi;
import io.determann.shadow.api.shadow.Executable;
import io.determann.shadow.api.shadow.Parameter;
import io.determann.shadow.api.shadow.Shadow;

import javax.lang.model.element.VariableElement;
import java.util.List;

public class ParameterImpl extends VariableImpl<Executable> implements Parameter
{
   public ParameterImpl(ShadowApi shadowApi, VariableElement variableElement)
   {
      super(shadowApi, variableElement);
   }

   @Override
   public boolean isVarArgs()
   {
      List<Parameter> parameters = getSurrounding().getParameters();
      return getSurrounding().isVarArgs() && parameters.get(parameters.size() - 1).representsSameType(this);
   }

   @Override
   public Shadow erasure()
   {
      return MirrorAdapter.getShadow(getApi(), getApi().getJdkApiContext().getProcessingEnv().getTypeUtils().erasure(getMirror()));
   }
}
