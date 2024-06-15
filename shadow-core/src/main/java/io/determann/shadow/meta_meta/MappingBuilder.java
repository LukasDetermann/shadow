package io.determann.shadow.meta_meta;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

public class MappingBuilder
{
   //Map<Operation<TYPE, RESULT>, BiFunction<INSTANCE, PARAMETER, RESULT>>
   private final Map<Operation<?, ?>, BiFunction<?, Object[], ?>> map = new HashMap<>();

   public <TYPE, RESULT> MappingBuilder with(Operation0<TYPE, RESULT> operation, Function<TYPE, RESULT> mapping)
   {
      //noinspection unchecked
      map.put(operation, (o, objects) -> mapping.apply((TYPE) o));
      return this;
   }

   public <TYPE, RESULT> MappingBuilder withOptional(Operation0<TYPE, RESULT> operation, Function<TYPE, Optional<RESULT>> mapping)
   {
      //noinspection unchecked
      map.put(operation, (o, objects) -> mapping.apply((TYPE) o));
      return this;
   }

   public <TYPE, PARAM_1, RESULT> MappingBuilder with(Operation1<TYPE, PARAM_1, RESULT> operation, BiFunction<TYPE, PARAM_1, RESULT> mapping)
   {
      //noinspection unchecked
      map.put(operation, (o, objects) -> mapping.apply((TYPE) o, (PARAM_1) objects[0]));
      return this;
   }

   public <TYPE, PARAM_1, RESULT> MappingBuilder withOptional(Operation1<TYPE, PARAM_1, RESULT> operation,
                                                              BiFunction<TYPE, PARAM_1, Optional<RESULT>> mapping)
   {
      //noinspection unchecked
      map.put(operation, (o, objects) -> mapping.apply((TYPE) o, (PARAM_1) objects[0]));
      return this;
   }

   Map<Operation<?, ?>, BiFunction<?, Object[], ?>> getMap()
   {
      return map;
   }
}
