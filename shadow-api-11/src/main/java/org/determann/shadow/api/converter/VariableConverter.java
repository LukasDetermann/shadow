package org.determann.shadow.api.converter;

import org.determann.shadow.api.shadow.EnumConstant;
import org.determann.shadow.api.shadow.Field;
import org.determann.shadow.api.shadow.Parameter;
import org.determann.shadow.api.shadow.Variable;

import java.util.Optional;

public interface VariableConverter
{
   Optional<EnumConstant> toOptionalEnumConstant();

   Optional<Field> toOptionalField();

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
