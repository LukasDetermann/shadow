package io.determann.shadow.implementation.support.api.provider;

import io.determann.shadow.api.ProviderSpi;
import io.determann.shadow.api.Response;
import io.determann.shadow.api.operation.Operation;

import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;

public abstract class AbstractProvider implements ProviderSpi
{
   //Map<Operation<RESULT>, BiFunction<INSTANCE, PARAMETER, RESULT>>
   private final Map<Operation<?>, BiFunction<?, Object[], ?>> map;

   protected AbstractProvider()
   {
      MappingBuilder builder = new MappingBuilder();
      addMappings(builder);
      map = builder.getMap();
   }

   @Override
   public <RESULT> Response<RESULT> request(Object instance, Operation<RESULT> operation, Object... params)
   {
      BiFunction<?, Object[], ?> mapping = map.get(operation);
      if (mapping == null)
      {
         return new Response.Unsupported<>();
      }
      //noinspection unchecked
      RESULT result = ((BiFunction<Object, Object[], RESULT>) mapping).apply(instance, params);

      if (result instanceof Optional<?> optional)
      {
         //noinspection unchecked
         return optional.map(o -> (Response<RESULT>) new Response.Result<>(o)).orElseGet(Response.Empty::new);
      }

      return new Response.Result<>(result);
   }

   protected abstract void addMappings(MappingBuilder builder);
}
