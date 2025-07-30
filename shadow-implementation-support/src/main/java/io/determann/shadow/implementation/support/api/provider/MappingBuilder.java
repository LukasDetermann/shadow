package io.determann.shadow.implementation.support.api.provider;

import io.determann.shadow.api.query.Implementation;
import io.determann.shadow.api.query.Response;
import io.determann.shadow.api.query.operation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

public class MappingBuilder
{
   //Map<Operation<RESULT>, BiFunction<INSTANCE, PARAMETER, RESULT>>
   private final Map<Operation<?>, BiFunction<?, Object[], Response<?>>> map = new HashMap<>();

   //static 0

   public <RESULT> MappingBuilder with(StaticOperation0<RESULT> operation, Function<Implementation, ? extends RESULT> supplier)
   {
      return add(operation, (o, objects) -> supplier.apply(((Implementation) o)));
   }

   public <RESULT> MappingBuilder withOptional(StaticOperation0<RESULT> operation, Function<Implementation, Optional<? extends RESULT>> supplier)
   {
      return addOptional(operation, (o, objects) -> supplier.apply(((Implementation) o)));
   }

   public <FROM, RESULT> MappingBuilder withCast(StaticOperation0<FROM> operation, Function<Implementation, Response<RESULT>> supplier)
   {
      return addCast(operation, (o, objects) -> supplier.apply((Implementation) o));
   }

   //static 1

   public <PARAM_1, RESULT> MappingBuilder with(StaticOperation1<PARAM_1, RESULT> operation,
                                                BiFunction<Implementation, PARAM_1, ? extends RESULT> supplier)
   {
      //noinspection unchecked
      return add(operation, (o, objects) -> supplier.apply((Implementation) o, (PARAM_1) objects[0]));
   }

   public <PARAM_1, RESULT> MappingBuilder withOptional(StaticOperation1<PARAM_1, RESULT> operation,
                                                        BiFunction<Implementation, PARAM_1, Optional<? extends RESULT>> supplier)
   {
      //noinspection unchecked
      return addOptional(operation, (o, objects) -> supplier.apply((Implementation) o, (PARAM_1) objects[0]));
   }

   public <PARAM_1, FROM, RESULT> MappingBuilder withCast(StaticOperation1<PARAM_1, FROM> operation,
                                                          BiFunction<Implementation, PARAM_1, Response<RESULT>> supplier)
   {
      //noinspection unchecked
      return addCast(operation, (o, objects) -> supplier.apply(((Implementation) o), ((PARAM_1) objects[0])));
   }

   //static 2

   public <PARAM_1, PARAM_2, RESULT> MappingBuilder with(StaticOperation2<PARAM_1, PARAM_2, RESULT> operation,
                                                         TriFunction<Implementation, PARAM_1, PARAM_2, ? extends RESULT> supplier)
   {
      //noinspection unchecked
      return add(operation, (o, objects) -> supplier.apply((Implementation) o, (PARAM_1) objects[0], (PARAM_2) objects[1]));
   }

   public <PARAM_1, PARAM_2, RESULT> MappingBuilder withOptional(StaticOperation2<PARAM_1, PARAM_2, RESULT> operation,
                                                                 TriFunction<Implementation, PARAM_1, PARAM_2, Optional<? extends RESULT>> supplier)
   {
      //noinspection unchecked
      return addOptional(operation, (o, objects) -> supplier.apply((Implementation) o, (PARAM_1) objects[0], (PARAM_2) objects[1]));
   }

   public <PARAM_1, PARAM_2, FROM, RESULT> MappingBuilder withCast(StaticOperation2<PARAM_1, PARAM_2, FROM> operation,
                                                                   TriFunction<Implementation, PARAM_1, PARAM_2, Response<RESULT>> supplier)
   {
      //noinspection unchecked
      return addCast(operation, (o, objects) -> supplier.apply((Implementation) o, (PARAM_1) objects[0], (PARAM_2) objects[1]));
   }

   //instance 0

   public <TYPE, RESULT> MappingBuilder with(InstanceOperation0<TYPE, RESULT> operation, Function<TYPE, ? extends RESULT> mapping)
   {
      //noinspection unchecked
      return add(operation, (o, objects) -> mapping.apply((TYPE) o));
   }

   public <TYPE, RESULT> MappingBuilder withOptional(InstanceOperation0<TYPE, RESULT> operation,
                                                     Function<TYPE, Optional<? extends RESULT>> mapping)
   {
      //noinspection unchecked
      return addOptional(operation, (o, objects) -> mapping.apply((TYPE) o));
   }

   public <TYPE, FROM, RESULT> MappingBuilder withCast(InstanceOperation0<TYPE, FROM> operation,
                                                       Function<TYPE, Response<RESULT>> mapping)
   {
      //noinspection unchecked
      return addCast(operation, (o, objects) -> mapping.apply((TYPE) o));
   }

   //instance 1

   public <TYPE, PARAM_1, RESULT> MappingBuilder with(InstanceOperation1<TYPE, PARAM_1, RESULT> operation,
                                                      BiFunction<TYPE, PARAM_1, RESULT> mapping)
   {
      //noinspection unchecked
      return add(operation, (o, objects) -> mapping.apply((TYPE) o, (PARAM_1) objects[0]));
   }

   public <TYPE, PARAM_1, RESULT> MappingBuilder withOptional(InstanceOperation1<TYPE, PARAM_1, RESULT> operation,
                                                              BiFunction<TYPE, PARAM_1, Optional<RESULT>> mapping)
   {
      //noinspection unchecked
      return addOptional(operation, (o, objects) -> mapping.apply((TYPE) o, (PARAM_1) objects[0]));
   }

   public <TYPE, PARAM_1, FROM, RESULT> MappingBuilder withCast(InstanceOperation1<FROM, PARAM_1, RESULT> operation,
                                                                BiFunction<TYPE, PARAM_1, Response<RESULT>> mapping)
   {
      //noinspection unchecked
      return addCast(operation, (o, objects) -> mapping.apply((TYPE) o, (PARAM_1) objects[0]));
   }

   //instance 2

   public <TYPE, PARAM_1, PARAM_2, RESULT> MappingBuilder with(InstanceOperation2<TYPE, PARAM_1, PARAM_2, RESULT> operation,
                                                               TriFunction<TYPE, PARAM_1, PARAM_2, RESULT> mapping)
   {
      //noinspection unchecked
      return add(operation, (o, objects) -> mapping.apply((TYPE) o, (PARAM_1) objects[0], (PARAM_2) objects[1]));
   }

   public <TYPE, PARAM_1, PARAM_2, RESULT> MappingBuilder withOptional(InstanceOperation2<TYPE, PARAM_1, PARAM_2, RESULT> operation,
                                                                       TriFunction<TYPE, PARAM_1, PARAM_2, Optional<RESULT>> mapping)
   {
      //noinspection unchecked
      return addOptional(operation, (o, objects) -> mapping.apply((TYPE) o, (PARAM_1) objects[0], (PARAM_2) objects[1]));
   }

   private MappingBuilder add(Operation<?> operation, BiFunction<Object, Object[], ?> function)
   {
      map.put(operation, (o, params) -> new Response.Result<>(function.apply(o, params)));
      return this;
   }

   private MappingBuilder addOptional(Operation<?> operation, BiFunction<Object, Object[], Optional<?>> function)
   {
      map.put(operation, (o, params) -> function.apply(o, params).<Response<?>>map(Response.Result::new).orElseGet(Response.Empty::new));
      return this;
   }

   private MappingBuilder addCast(Operation<?> operation, BiFunction<Object, Object[], Response<?>> function)
   {
      map.put(operation, function);
      return this;
   }

   Map<Operation<?>, BiFunction<?, Object[], Response<?>>> getMap()
   {
      return map;
   }
}
