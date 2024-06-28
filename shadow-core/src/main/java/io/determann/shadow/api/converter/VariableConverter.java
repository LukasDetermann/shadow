package io.determann.shadow.api.converter;

import io.determann.shadow.api.shadow.structure.EnumConstant;
import io.determann.shadow.api.shadow.structure.Field;
import io.determann.shadow.api.shadow.structure.Parameter;
import io.determann.shadow.api.shadow.structure.Variable;

import java.util.Optional;

public interface VariableConverter
{
   EnumConstant toEnumConstantOrThrow();

   Optional<EnumConstant> toEnumConstant();

   Field toFieldOrThrow();

   Optional<Field> toField();

   Parameter toParameterOrThrow();

   Optional<Parameter> toParameter();

   /**
    * consumes all leafs of {@link Variable}
    */
   void consume(VariableConsumer consumer);

   /**
    * maps all leafs of {@link Variable}
    */
   <T> T map(VariableMapper<T> mapper);
}
