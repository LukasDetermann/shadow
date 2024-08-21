package io.determann.shadow.api;

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

   public static <TYPE extends ImplementationDefined, RESULT> Response<RESULT> request(TYPE instance, Operation0<? super TYPE, RESULT> operation)
   {
      return _request(instance, operation);
   }

   public static <TYPE extends ImplementationDefined, RESULT> RESULT requestOrThrow(TYPE instance, Operation0<? super TYPE, RESULT> operation)
   {
      return switch (_request(instance, operation))
      {
         case Response.Result<RESULT>(RESULT result) -> result;
         case Response.Unsupported<RESULT> v -> throw unsupported(instance, operation);
         case Response.Empty<RESULT> v -> throw noSuchElement(instance, operation);
      };
   }

   public static <TYPE extends ImplementationDefined, RESULT> Optional<RESULT> requestOrEmpty(TYPE instance, Operation0<? super TYPE, RESULT> operation)
   {
      return switch (_request(instance, operation))
      {
         case Response.Result<RESULT>(RESULT result) -> Optional.of(result);
         case Response.Unsupported<RESULT> v -> Optional.empty();
         case Response.Empty<RESULT> v -> Optional.empty();
      };
   }

   public static <TYPE extends ImplementationDefined, PARAM_1, RESULT> Response<RESULT> request(TYPE instance,
                                                                                                Operation1<? super TYPE, PARAM_1, RESULT> operation,
                                                                                                PARAM_1 param1)
   {
      return _request(instance, operation, param1);
   }

   public static <TYPE extends ImplementationDefined, PARAM_1, RESULT> RESULT requestOrThrow(TYPE instance,
                                                                                             Operation1<? super TYPE, PARAM_1, RESULT> operation,
                                                                                             PARAM_1 param1)
   {
      return switch (_request(instance, operation, param1))
      {
         case Response.Result<RESULT>(RESULT result) -> result;
         case Response.Unsupported<RESULT> v -> throw unsupported(instance, operation);
         case Response.Empty<RESULT> v -> throw noSuchElement(instance, operation);
      };
   }

   public static <TYPE extends ImplementationDefined, PARAM_1, RESULT> Optional<RESULT> requestOrEmpty(TYPE instance,
                                                                                                       Operation1<? super TYPE, PARAM_1, RESULT> operation,
                                                                                                       PARAM_1 param1)
   {
      return switch (_request(instance, operation, param1))
      {
         case Response.Result<RESULT>(RESULT result) -> Optional.of(result);
         case Response.Unsupported<RESULT> v -> Optional.empty();
         case Response.Empty<RESULT> v -> Optional.empty();
      };
   }

   private static <TYPE extends ImplementationDefined, RESULT> Response<RESULT> _request(TYPE instance,
                                                                                         Operation<? super TYPE, RESULT> operation,
                                                                                         Object... params)
   {
      init();

      ProviderSpi spi = providers.get(requireNonNull(instance.getImplementationName()));
      if (spi == null)
      {
         throw new UnsupportedOperationException("No provider found for " + instance.getImplementationName());
      }

      return spi.request(instance, operation, params);
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

   private static <TYPE extends ImplementationDefined, PARAM_1, RESULT> NoSuchElementException noSuchElement(TYPE instance,
                                                                                                             Operation<? super TYPE, RESULT> operation)
   {
      return new NoSuchElementException(operation.getName() +
                                        " does not return a value for " +
                                        instance +
                                        " with implementation " +
                                        instance.getImplementationName());
   }

   private static <TYPE extends ImplementationDefined, PARAM_1, RESULT> UnsupportedOperationException unsupported(TYPE instance,
                                                                                                                  Operation<? super TYPE, RESULT> operation)
   {
      return new UnsupportedOperationException(operation.getName() +
                                               " not supported for " +
                                               instance +
                                               " with implementation " +
                                               instance.getImplementationName());
   }
}
