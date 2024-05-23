package io.determann.shadow.meta_meta;

import io.determann.shadow.api.ImplementationDefined;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

public abstract class AbstractProvider implements ProviderSpi
{
   private final Map<Operation<?, ?>, Function<?, ?>> map;

   protected AbstractProvider()
   {
      MappingBuilder builder = new MappingBuilder();
      addMappings(builder);
      map = builder.getMap();
   }

   @Override
   public <RESULT, TYPE extends ImplementationDefined> Response<RESULT> request(TYPE instance, Operation<TYPE, RESULT> operation)
   {
      Function<?, ?> mapping = map.get(operation);
      if (mapping == null)
      {
         return new Response.Unsupported<>();
      }
      //noinspection unchecked
      RESULT result   = ((Function<TYPE, RESULT>) mapping).apply(instance);

      if (result instanceof Optional<?> optional)
      {
         //noinspection unchecked
         return optional.map(o -> (Response<RESULT>) new Response.Result<>(o)).orElseGet(Response.Empty::new);
      }

      return new Response.Result<>(result);
   }

   protected abstract void addMappings(MappingBuilder builder);
}
