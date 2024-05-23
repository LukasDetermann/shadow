package io.determann.shadow.meta_meta;

import io.determann.shadow.api.ImplementationDefined;

import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.ServiceLoader;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;

public class Provider
{
   private Provider()
   {
   }

   private static Map<String, ProviderSpi> providers;

   private static <TYPE extends ImplementationDefined, RESULT> Response<RESULT> _request(TYPE instance, Operation<TYPE, RESULT> operation)
   {
      init();

      ProviderSpi spi = providers.get(requireNonNull(instance.getImplementationName()));
      if (spi == null)
      {
         throw new UnsupportedOperationException("No provider found for " + instance.getImplementationName());
      }

      return spi.request(instance, operation);
   }

   public static <TYPE extends ImplementationDefined, RESULT> RESULT requestOrThrow(TYPE instance, Operation<TYPE, RESULT> operation)
   {
      return switch (_request(instance, operation))
      {
         case Response.Result<RESULT> result -> result.value();
         case Response.Unsupported<RESULT> v -> throw new UnsupportedOperationException(operation.name() +
                                                                                        " not supported for " +
                                                                                        instance +
                                                                                        " with implementation " +
                                                                                        instance.getImplementationName());
         case Response.Empty<RESULT> v -> throw new NoSuchElementException(operation.name() +
                                                                           " does not return a value for " +
                                                                           instance +
                                                                           " with implementation " +
                                                                           instance.getImplementationName());
      };
   }

   public static <TYPE extends ImplementationDefined, RESULT> Optional<RESULT> request(TYPE instance, Operation<TYPE, RESULT> operation)
   {
      return switch (_request(instance, operation))
      {
         case Response.Result<RESULT> result -> Optional.of(result.value());
         case Response.Unsupported<RESULT> v ->  Optional.empty();
         case Response.Empty<RESULT> v -> Optional.empty();
      };
   }

   private static void init()
   {
      if (providers == null)
      {
         providers = ServiceLoader.load(ProviderSpi.class).stream()
                                  .map(ServiceLoader.Provider::get)
                                  .collect(Collectors.toMap(ProviderSpi::getImplementationName, Function.identity()));
      }
   }
}
