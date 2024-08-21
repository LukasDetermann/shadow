package io.determann.shadow.internal.lang_model.shadow.structure;

import io.determann.shadow.api.Provider;
import io.determann.shadow.api.lang_model.LM_Adapter;
import io.determann.shadow.api.lang_model.LM_Context;
import io.determann.shadow.api.lang_model.shadow.structure.LM_Executable;
import io.determann.shadow.api.lang_model.shadow.structure.LM_Parameter;
import io.determann.shadow.api.shadow.structure.C_Parameter;
import io.determann.shadow.api.shadow.type.C_Shadow;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;
import java.util.List;
import java.util.Objects;

import static io.determann.shadow.api.Operations.*;
import static io.determann.shadow.api.Provider.requestOrEmpty;
import static io.determann.shadow.api.Provider.requestOrThrow;
import static io.determann.shadow.api.lang_model.LM_Queries.query;

public class ParameterImpl extends VariableImpl implements LM_Parameter
{
   public ParameterImpl(LM_Context context, VariableElement variableElement)
   {
      super(context, variableElement);
   }

   @Override
   public boolean isVarArgs()
   {
      List<? extends C_Parameter> parameters = requestOrThrow(getSurrounding(), EXECUTABLE_GET_PARAMETERS);
      return requestOrThrow(getSurrounding(), EXECUTABLE_IS_VAR_ARGS) &&
             query((C_Shadow) parameters.get(parameters.size() - 1)).representsSameType(this);
   }

   @Override
   public int hashCode()
   {
      return Objects.hash(getKind(),
                          getName(),
                          getSurrounding(),
                          isVarArgs());
   }

   @Override
   public LM_Executable getSurrounding()
   {
      if (getElement().getEnclosingElement() instanceof ExecutableElement executableElement)
      {
         return LM_Adapter.generalize(getApi(), executableElement);
      }

      return LM_Adapter.generalize(getApi(), getElement().getEnclosingElement());
   }

   @Override
   public boolean equals(Object other)
   {
      if (other == this)
      {
         return true;
      }
      if (!(other instanceof C_Parameter otherVariable))
      {
         return false;
      }
      return Provider.requestOrEmpty(otherVariable, NAMEABLE_GET_NAME).map(name -> Objects.equals(getName(), name)).orElse(false) &&
             Objects.equals(getType(), requestOrThrow(otherVariable, VARIABLE_GET_TYPE)) &&
             requestOrEmpty(otherVariable, MODIFIABLE_GET_MODIFIERS).map(modifiers -> Objects.equals(modifiers, getModifiers())).orElse(false) &&
             Objects.equals(isVarArgs(), requestOrThrow(otherVariable, PARAMETER_IS_VAR_ARGS));
   }
}
