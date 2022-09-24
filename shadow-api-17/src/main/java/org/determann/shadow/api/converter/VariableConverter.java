package org.determann.shadow.api.converter;

import org.determann.shadow.api.shadow.EnumConstant;
import org.determann.shadow.api.shadow.Field;
import org.determann.shadow.api.shadow.Parameter;
import org.determann.shadow.api.shadow.Variable;

import java.util.Optional;

public interface VariableConverter
{
   EnumConstant toEnumConstant();

   Optional<EnumConstant> toOptionalEnumConstant();

   Field toField();

   Optional<Field> toOptionalField();

   Parameter toParameter();

   Optional<Parameter> toOptionalParameter();

   /**
    * consumes all leafs of {@link Variable}
    */
   void consumer(VariableConsumer consumer);

   /**
    * maps all leafs of {@link Variable}
    */
   <T> T mapper(VariableMapper<T> mapper);
}
