package io.determann.shadow.implementation.support.api.provider;

import io.determann.shadow.api.Implementation;
import io.determann.shadow.api.operation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

public class MappingBuilder
{
   //Map<Operation<RESULT>, BiFunction<INSTANCE, PARAMETER, RESULT>>
   private final Map<Operation<?>, BiFunction<?, Object[], ?>> map = new HashMap<>();

   //static 0

   public <RESULT> MappingBuilder with(StaticOperation0<RESULT> operation, Function<Implementation, ? extends RESULT> supplier)
   {
      map.put(operation, (o, objects) -> supplier.apply((Implementation) o));
      return this;
   }

   public <RESULT> MappingBuilder withOptional(StaticOperation0<RESULT> operation, Function<Implementation, ? extends RESULT> supplier)
   {
      map.put(operation, (o, objects) -> supplier.apply((Implementation) o));
      return this;
   }

   //static 1

   public <PARAM_1, RESULT> MappingBuilder with(StaticOperation1<PARAM_1, RESULT> operation,
                                                BiFunction<Implementation, PARAM_1, ? extends RESULT> supplier)
   {
      //noinspection unchecked
      map.put(operation, (o, objects) -> supplier.apply((Implementation) o, (PARAM_1) objects[0]));
      return this;
   }

   public <PARAM_1, RESULT> MappingBuilder withOptional(StaticOperation1<PARAM_1, RESULT> operation,
                                                        BiFunction<Implementation, PARAM_1, Optional<? extends RESULT>> supplier)
   {
      //noinspection unchecked
      map.put(operation, (o, objects) -> supplier.apply((Implementation) o, (PARAM_1) objects[0]));
      return this;
   }

   //static 2

   public <PARAM_1, PARAM_2, RESULT> MappingBuilder with(StaticOperation2<PARAM_1, PARAM_2, RESULT> operation,
                                                         TriFunction<Implementation, PARAM_1, PARAM_2, ? extends RESULT> supplier)
   {
      //noinspection unchecked
      map.put(operation, (o, objects) -> supplier.apply((Implementation) o, (PARAM_1) objects[0], (PARAM_2) objects[1]));
      return this;
   }

   public <PARAM_1, PARAM_2, RESULT> MappingBuilder withOptional(StaticOperation2<PARAM_1, PARAM_2, RESULT> operation,
                                                                 TriFunction<Implementation, PARAM_1, PARAM_2, Optional<? extends RESULT>> supplier)
   {
      //noinspection unchecked
      map.put(operation, (o, objects) -> supplier.apply((Implementation) o, (PARAM_1) objects[0], (PARAM_2) objects[1]));
      return this;
   }

   //instance 0

   public <TYPE, RESULT> MappingBuilder with(InstanceOperation0<TYPE, RESULT> operation, Function<TYPE, ? extends RESULT> mapping)
   {
      //noinspection unchecked
      map.put(operation, (o, objects) -> mapping.apply((TYPE) o));
      return this;
   }

   public <TYPE, RESULT> MappingBuilder withOptional(InstanceOperation0<TYPE, RESULT> operation,
                                                     Function<TYPE, Optional<? extends RESULT>> mapping)
   {
      //noinspection unchecked
      map.put(operation, (o, objects) -> mapping.apply((TYPE) o));
      return this;
   }

   //instance 1

   public <TYPE, PARAM_1, RESULT> MappingBuilder with(InstanceOperation1<TYPE, PARAM_1, RESULT> operation,
                                                      BiFunction<TYPE, PARAM_1, RESULT> mapping)
   {
      //noinspection unchecked
      map.put(operation, (o, objects) -> mapping.apply((TYPE) o, (PARAM_1) objects[0]));
      return this;
   }

   public <TYPE, PARAM_1, RESULT> MappingBuilder withOptional(InstanceOperation1<TYPE, PARAM_1, RESULT> operation,
                                                              BiFunction<TYPE, PARAM_1, Optional<RESULT>> mapping)
   {
      //noinspection unchecked
      map.put(operation, (o, objects) -> mapping.apply((TYPE) o, (PARAM_1) objects[0]));
      return this;
   }

   //instance 2

   public <TYPE, PARAM_1, PARAM_2, RESULT> MappingBuilder with(InstanceOperation2<TYPE, PARAM_1, PARAM_2, RESULT> operation,
                                                               TriFunction<TYPE, PARAM_1, PARAM_2, RESULT> mapping)
   {
      //noinspection unchecked
      map.put(operation, (o, objects) -> mapping.apply((TYPE) o, (PARAM_1) objects[0], (PARAM_2) objects[1]));
      return this;
   }

   public <TYPE, PARAM_1, PARAM_2, RESULT> MappingBuilder withOptional(InstanceOperation2<TYPE, PARAM_1, PARAM_2, RESULT> operation,
                                                                       TriFunction<TYPE, PARAM_1, PARAM_2, Optional<RESULT>> mapping)
   {
      //noinspection unchecked
      map.put(operation, (o, objects) -> mapping.apply((TYPE) o, (PARAM_1) objects[0], (PARAM_2) objects[1]));
      return this;
   }

   Map<Operation<?>, BiFunction<?, Object[], ?>> getMap()
   {
      return map;
   }
}
