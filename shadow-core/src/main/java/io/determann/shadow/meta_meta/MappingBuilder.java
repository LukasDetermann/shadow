package io.determann.shadow.meta_meta;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

public class MappingBuilder
{
   private final Map<Operation<?, ?>, Function<?, ?>> map = new HashMap<>();

   public <TYPE, RESULT> MappingBuilder with(Operation<TYPE, RESULT> operation, Function<TYPE, RESULT> mapping)
   {
      map.put(operation, mapping);
      return this;
   }
   public <TYPE, RESULT> MappingBuilder withOptional(Operation<TYPE, RESULT> operation, Function<TYPE, Optional<RESULT>> mapping)
   {
      map.put(operation, mapping);
      return this;
   }

   Map<Operation<?, ?>, Function<?, ?>> getMap()
   {
      return map;
   }
}
