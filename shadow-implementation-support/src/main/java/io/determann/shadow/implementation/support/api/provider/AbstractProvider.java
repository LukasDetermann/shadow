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

   protected static <FROM, TO> Response<TO> cast(Response<FROM> response, Class<TO> toCastTo)
   {
      return switch (response)
      {
         case Response.Result<FROM>(FROM result) -> new Response.Result<>(toCastTo.cast(result));
         case Response.Unsupported<FROM> v -> new Response.Unsupported<>();
         case Response.Empty<FROM> v -> new Response.Unsupported<>();
      };
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
      Object result = ((BiFunction<Object, Object[], ?>) mapping).apply(instance, params);

      if (result instanceof Response<?> response)
      {
         //noinspection unchecked
         return ((Response<RESULT>) response);
      }
      if (result instanceof Optional<?> optional)
      {
         //noinspection unchecked
         return optional.map(o -> (Response<RESULT>) new Response.Result<>(o)).orElseGet(Response.Empty::new);
      }

      //noinspection unchecked
      return new Response.Result<>(((RESULT) result));
   }

   protected abstract void addMappings(MappingBuilder builder);
}
