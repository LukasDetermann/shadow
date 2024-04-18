package io.determann.shadow.internal.reflection;

import io.determann.shadow.api.ImplementationDefined;
import io.determann.shadow.api.Nameable;
import io.determann.shadow.api.reflection.query.ReflectionQueries;
import io.determann.shadow.meta_meta.Operation;
import io.determann.shadow.meta_meta.Operations;
import io.determann.shadow.meta_meta.ProviderSpi;
import io.determann.shadow.meta_meta.Response;

public class ReflectionProvider implements ProviderSpi
{
   public static final String IMPLEMENTATION_NAME = "io.determann.shadow-reflection";

   @Override
   public String getImplementationName()
   {
      return IMPLEMENTATION_NAME;
   }

   @SuppressWarnings("unchecked")
   @Override
   public <RESULT, TYPE extends ImplementationDefined> Response<RESULT> request(TYPE instance, Operation<TYPE, RESULT> operation)
   {
      if (instance instanceof Nameable nameable)
      {
         if (operation.equals(Operations.NAME))
         {
            return (Response<RESULT>) new Response.Result<>(ReflectionQueries.query(nameable).getName());
         }
      }
      return new Response.Unsupported<>();
   }
}
