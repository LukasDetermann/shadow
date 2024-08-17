package io.determann.shadow.internal.lang_model.shadow.structure;

import io.determann.shadow.api.lang_model.LangModelAdapter;
import io.determann.shadow.api.lang_model.LangModelContext;
import io.determann.shadow.api.lang_model.shadow.structure.ExecutableLangModel;
import io.determann.shadow.api.lang_model.shadow.structure.ParameterLangModel;
import io.determann.shadow.api.shadow.Provider;
import io.determann.shadow.api.shadow.structure.Parameter;
import io.determann.shadow.api.shadow.type.Shadow;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;
import java.util.List;
import java.util.Objects;

import static io.determann.shadow.api.lang_model.LangModelQueries.query;
import static io.determann.shadow.api.shadow.Operations.*;
import static io.determann.shadow.api.shadow.Provider.requestOrEmpty;
import static io.determann.shadow.api.shadow.Provider.requestOrThrow;

public class ParameterImpl extends VariableImpl implements ParameterLangModel
{
   public ParameterImpl(LangModelContext context, VariableElement variableElement)
   {
      super(context, variableElement);
   }

   @Override
   public boolean isVarArgs()
   {
      List<? extends Parameter> parameters = requestOrThrow(getSurrounding(), EXECUTABLE_GET_PARAMETERS);
      return requestOrThrow(getSurrounding(), EXECUTABLE_IS_VAR_ARGS) &&
             query((Shadow) parameters.get(parameters.size() - 1)).representsSameType(this);
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
   public ExecutableLangModel getSurrounding()
   {
      if (getElement().getEnclosingElement() instanceof ExecutableElement executableElement)
      {
         return LangModelAdapter.generalize(getApi(), executableElement);
      }

      return LangModelAdapter.generalize(getApi(), getElement().getEnclosingElement());
   }

   @Override
   public boolean equals(Object other)
   {
      if (other == this)
      {
         return true;
      }
      if (!(other instanceof Parameter otherVariable))
      {
         return false;
      }
      return Provider.requestOrEmpty(otherVariable, NAMEABLE_GET_NAME).map(name -> Objects.equals(getName(), name)).orElse(false) &&
             Objects.equals(getType(), requestOrThrow(otherVariable, VARIABLE_GET_TYPE)) &&
             requestOrEmpty(otherVariable, MODIFIABLE_GET_MODIFIERS).map(modifiers -> Objects.equals(modifiers, getModifiers())).orElse(false) &&
             Objects.equals(isVarArgs(), requestOrThrow(otherVariable, PARAMETER_IS_VAR_ARGS));
   }
}
