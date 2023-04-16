package io.determann.shadow.api.converter;

import io.determann.shadow.api.shadow.EnumConstant;
import io.determann.shadow.api.shadow.Field;
import io.determann.shadow.api.shadow.Parameter;
import io.determann.shadow.api.shadow.Variable;

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
   void consumer(VariableConsumer consumer);

   /**
    * maps all leafs of {@link Variable}
    */
   <T> T mapper(VariableMapper<T> mapper);
}
