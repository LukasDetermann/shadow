package io.determann.shadow.internal.annotation_processing.shadow.structure;

import io.determann.shadow.api.C;
import io.determann.shadow.api.annotation_processing.AP;
import io.determann.shadow.api.annotation_processing.adapter.Adapters;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;
import java.util.List;
import java.util.Objects;

import static io.determann.shadow.api.query.Operations.*;
import static io.determann.shadow.api.query.Provider.requestOrEmpty;
import static io.determann.shadow.api.query.Provider.requestOrThrow;

public class ParameterImpl extends VariableImpl implements AP.Parameter
{
   public ParameterImpl(AP.Context context, VariableElement variableElement)
   {
      super(context, variableElement);
   }

   @Override
   public boolean isVarArgs()
   {
      List<? extends C.Parameter> parameters = requestOrThrow(getSurrounding(), EXECUTABLE_GET_PARAMETERS);
      return requestOrThrow(getSurrounding(), EXECUTABLE_IS_VAR_ARGS) &&
             parameters.get(parameters.size() - 1).equals(this);
   }

   @Override
   public int hashCode()
   {
      return Objects.hash(getName(),
                          getSurrounding());
   }

   @Override
   public AP.Executable getSurrounding()
   {
      return Adapters.adapt(getApi(), ((ExecutableElement) getElement().getEnclosingElement()));
   }

   @Override
   public boolean equals(Object other)
   {
      if (other == this)
      {
         return true;
      }
      if (!(other instanceof C.Parameter otherVariable))
      {
         return false;
      }
      return requestOrEmpty(otherVariable, NAMEABLE_GET_NAME).map(name -> Objects.equals(getName(), name)).orElse(false) &&
             Objects.equals(getType(), requestOrThrow(otherVariable, VARIABLE_GET_TYPE)) &&
             requestOrEmpty(otherVariable, MODIFIABLE_GET_MODIFIERS).map(modifiers -> Objects.equals(modifiers, getModifiers())).orElse(false);
   }
}
