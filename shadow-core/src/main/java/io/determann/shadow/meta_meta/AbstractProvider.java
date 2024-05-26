package io.determann.shadow.meta_meta;

import io.determann.shadow.api.ImplementationDefined;

import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;

public abstract class AbstractProvider implements ProviderSpi
{
   private final Map<Operation<?, ?>, BiFunction<?, Object[], ?>> map;

   protected AbstractProvider()
   {
      MappingBuilder builder = new MappingBuilder();
      addMappings(builder);
      map = builder.getMap();
   }

   @Override
   public <RESULT, TYPE extends ImplementationDefined> Response<RESULT> request(TYPE instance, Operation<TYPE, RESULT> operation, Object... params)
   {
      BiFunction<?, Object[], ?> mapping = map.get(operation);
      if (mapping == null)
      {
         return new Response.Unsupported<>();
      }
      //noinspection unchecked
      RESULT result = ((BiFunction<TYPE, Object[], RESULT>) mapping).apply(instance, params);

      if (result instanceof Optional<?> optional)
      {
         //noinspection unchecked
         return optional.map(o -> (Response<RESULT>) new Response.Result<>(o)).orElseGet(Response.Empty::new);
      }

      return new Response.Result<>(result);
   }

   protected abstract void addMappings(MappingBuilder builder);
}
