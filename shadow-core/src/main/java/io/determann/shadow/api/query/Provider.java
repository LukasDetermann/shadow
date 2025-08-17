package io.determann.shadow.api.query;

import io.determann.shadow.api.query.operation.*;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;

public final class Provider
{
   private Provider()
   {
   }

   @Nullable
   private static Map<Implementation, ProviderSpi> providers;

   //static 0

   public static <TYPE extends ImplementationDefined, RESULT> Response<RESULT> request(Implementation implementation,
                                                                                       StaticOperation0<RESULT> operation)
   {
      return _request(implementation, operation);
   }

   public static <TYPE extends ImplementationDefined, RESULT> RESULT requestOrThrow(Implementation implementation,
                                                                                    StaticOperation0<RESULT> operation)
   {
      return switch (_request(implementation, operation))
      {
         case Response.Result<RESULT>(RESULT result) -> result;
         case Response.Unsupported<RESULT> v -> throw unsupported(implementation, operation);
         case Response.Empty<RESULT> v -> throw noSuchElement(implementation, operation);
      };
   }

   public static <TYPE extends ImplementationDefined, RESULT> Optional<RESULT> requestOrEmpty(Implementation implementation,
                                                                                              StaticOperation0<RESULT> operation)
   {
      return switch (_request(implementation, operation))
      {
         case Response.Result<RESULT>(RESULT result) -> Optional.of(result);
         case Response.Unsupported<RESULT> v -> Optional.empty();
         case Response.Empty<RESULT> v -> Optional.empty();
      };
   }

   //static 1

   public static <TYPE extends ImplementationDefined, PARAM_1, RESULT> Response<RESULT> request(Implementation implementation,
                                                                                                StaticOperation1<PARAM_1, RESULT> operation,
                                                                                                PARAM_1 param1)
   {
      return _request(implementation, operation, param1);
   }

   public static <TYPE extends ImplementationDefined, PARAM_1, RESULT> RESULT requestOrThrow(Implementation implementation,
                                                                                             StaticOperation1<PARAM_1, RESULT> operation,
                                                                                             PARAM_1 param1)
   {
      return switch (_request(implementation, operation, param1))
      {
         case Response.Result<RESULT>(RESULT result) -> result;
         case Response.Unsupported<RESULT> v -> throw unsupported(implementation, operation, param1);
         case Response.Empty<RESULT> v -> throw noSuchElement(implementation, operation, param1);
      };
   }

   public static <TYPE extends ImplementationDefined, PARAM_1, RESULT> Optional<RESULT> requestOrEmpty(Implementation implementation,
                                                                                                       StaticOperation1<PARAM_1, RESULT> operation,
                                                                                                       PARAM_1 param1)
   {
      return switch (_request(implementation, operation, param1))
      {
         case Response.Result<RESULT>(RESULT result) -> Optional.of(result);
         case Response.Unsupported<RESULT> v -> Optional.empty();
         case Response.Empty<RESULT> v -> Optional.empty();
      };
   }

   //static 2

   public static <TYPE extends ImplementationDefined, PARAM_1, PARAM_2, RESULT> Response<RESULT> request(Implementation implementation,
                                                                                                         StaticOperation2<PARAM_1, PARAM_2, RESULT> operation,
                                                                                                         PARAM_1 param1,
                                                                                                         PARAM_2 param2)
   {
      return _request(implementation, operation, param1, param2);
   }

   public static <TYPE extends ImplementationDefined, PARAM_1, PARAM_2, RESULT> RESULT requestOrThrow(Implementation implementation,
                                                                                                      StaticOperation2<PARAM_1, PARAM_2, RESULT> operation,
                                                                                                      PARAM_1 param1,
                                                                                                      PARAM_2 param2)
   {
      return switch (_request(implementation, operation, param1, param2))
      {
         case Response.Result<RESULT>(RESULT result) -> result;
         case Response.Unsupported<RESULT> v -> throw unsupported(implementation, operation, param1, param2);
         case Response.Empty<RESULT> v -> throw noSuchElement(implementation, operation, param1, param2);
      };
   }

   public static <TYPE extends ImplementationDefined, PARAM_1, PARAM_2, RESULT> Optional<RESULT> requestOrEmpty(Implementation implementation,
                                                                                                                StaticOperation2<PARAM_1, PARAM_2, RESULT> operation,
                                                                                                                PARAM_1 param1,
                                                                                                                PARAM_2 param2)
   {
      return switch (_request(implementation, operation, param1, param2))
      {
         case Response.Result<RESULT>(RESULT result) -> Optional.of(result);
         case Response.Unsupported<RESULT> v -> Optional.empty();
         case Response.Empty<RESULT> v -> Optional.empty();
      };
   }

   //instance 0

   public static <TYPE extends ImplementationDefined, RESULT> Response<RESULT> request(TYPE instance,
                                                                                       InstanceOperation0<? super TYPE, RESULT> operation)
   {
      return _request(instance, operation);
   }

   public static <TYPE extends ImplementationDefined, RESULT> RESULT requestOrThrow(TYPE instance,
                                                                                    InstanceOperation0<? super TYPE, RESULT> operation)
   {
      return switch (_request(instance, operation))
      {
         case Response.Result<RESULT>(RESULT result) -> result;
         case Response.Unsupported<RESULT> v -> throw unsupported(instance, operation);
         case Response.Empty<RESULT> v -> throw noSuchElement(instance, operation);
      };
   }

   public static <TYPE extends ImplementationDefined, RESULT> Optional<RESULT> requestOrEmpty(TYPE instance,
                                                                                              InstanceOperation0<? super TYPE, RESULT> operation)
   {
      return switch (_request(instance, operation))
      {
         case Response.Result<RESULT>(RESULT result) -> Optional.of(result);
         case Response.Unsupported<RESULT> v -> Optional.empty();
         case Response.Empty<RESULT> v -> Optional.empty();
      };
   }

   //instance 1

   public static <TYPE extends ImplementationDefined, PARAM_1, RESULT> Response<RESULT> request(TYPE instance,
                                                                                                InstanceOperation1<? super TYPE, PARAM_1, RESULT> operation,
                                                                                                PARAM_1 param1)
   {
      return _request(instance, operation, param1);
   }

   public static <TYPE extends ImplementationDefined, PARAM_1, RESULT> RESULT requestOrThrow(TYPE instance,
                                                                                             InstanceOperation1<? super TYPE, PARAM_1, RESULT> operation,
                                                                                             PARAM_1 param1)
   {
      return switch (_request(instance, operation, param1))
      {
         case Response.Result<RESULT>(RESULT result) -> result;
         case Response.Unsupported<RESULT> v -> throw unsupported(instance, operation, param1);
         case Response.Empty<RESULT> v -> throw noSuchElement(instance, operation, param1);
      };
   }

   public static <TYPE extends ImplementationDefined, PARAM_1, RESULT> Optional<RESULT> requestOrEmpty(TYPE instance,
                                                                                                       InstanceOperation1<? super TYPE, PARAM_1, RESULT> operation,
                                                                                                       PARAM_1 param1)
   {
      return switch (_request(instance, operation, param1))
      {
         case Response.Result<RESULT>(RESULT result) -> Optional.of(result);
         case Response.Unsupported<RESULT> v -> Optional.empty();
         case Response.Empty<RESULT> v -> Optional.empty();
      };
   }

   //instance 2

   public static <TYPE extends ImplementationDefined, PARAM_1, PARAM_2, RESULT> Response<RESULT> request(TYPE instance,
                                                                                                         InstanceOperation2<? super TYPE, PARAM_1, PARAM_2, RESULT> operation,
                                                                                                         PARAM_1 param1,
                                                                                                         PARAM_2 param2)
   {
      return _request(instance, operation, param1, param2);
   }

   public static <TYPE extends ImplementationDefined, PARAM_1, PARAM_2, RESULT> RESULT requestOrThrow(TYPE instance,
                                                                                                      InstanceOperation2<? super TYPE, PARAM_1, PARAM_2, RESULT> operation,
                                                                                                      PARAM_1 param1,
                                                                                                      PARAM_2 param2)
   {
      return switch (_request(instance, operation, param1, param2))
      {
         case Response.Result<RESULT>(RESULT result) -> result;
         case Response.Unsupported<RESULT> v -> throw unsupported(instance, operation, param1, param2);
         case Response.Empty<RESULT> v -> throw noSuchElement(instance, operation, param1, param2);
      };
   }

   public static <TYPE extends ImplementationDefined, PARAM_1, PARAM_2, RESULT> Optional<RESULT> requestOrEmpty(TYPE instance,
                                                                                                                InstanceOperation2<? super TYPE, PARAM_1, PARAM_2, RESULT> operation,
                                                                                                                PARAM_1 param1,
                                                                                                                PARAM_2 param2)
   {
      return switch (_request(instance, operation, param1, param2))
      {
         case Response.Result<RESULT>(RESULT result) -> Optional.of(result);
         case Response.Unsupported<RESULT> v -> Optional.empty();
         case Response.Empty<RESULT> v -> Optional.empty();
      };
   }

   private static <TYPE extends ImplementationDefined, RESULT> Response<RESULT> _request(TYPE instance,
                                                                                         InstanceOperation<? super TYPE, RESULT> instanceOperation,
                                                                                         Object... params)
   {
      requireNonNull(instance);
      return _request(instance.getImplementation(), instance, instanceOperation, params);
   }

   private static <TYPE extends ImplementationDefined, RESULT> Response<RESULT> _request(Implementation implementation,
                                                                                         StaticOperation<RESULT> staticOperation,
                                                                                         Object... params)
   {
      return _request(implementation, implementation, staticOperation, params);
   }

   private static <RESULT> Response<RESULT> _request(Implementation implementation,
                                                     Object instance,
                                                     Operation<RESULT> operation,
                                                     Object... params)
   {
      requireNonNull(implementation);
      requireNonNull(implementation.getName());
      requireNonNull(operation);
      init();

      ProviderSpi spi = providers.get(implementation);
      if (spi == null)
      {
         throw new UnsupportedOperationException("No provider found for " + implementation.getName());
      }

      return spi.request(instance, operation, params);
   }

   private static void init()
   {
      if (providers == null)
      {
         providers = ServiceLoader.load(ProviderSpi.class, ProviderSpi.class.getClassLoader())
                                  .stream()
                                  .map(ServiceLoader.Provider::get)
                                  .collect(Collectors.toMap(ProviderSpi::getImplementation, Function.identity()));
      }
   }

   private static NoSuchElementException noSuchElement(Implementation implementation,
                                                       StaticOperation<?> instanceOperation,
                                                       Object... params)
   {
      return new NoSuchElementException(buildExceptionText(instanceOperation,
                                                           "does not return a value",
                                                           null,
                                                           implementation,
                                                           params));
   }

   private static NoSuchElementException noSuchElement(ImplementationDefined instance,
                                                       InstanceOperation<?, ?> instanceOperation,
                                                       Object... params)
   {
      return new NoSuchElementException(buildExceptionText(instanceOperation,
                                                           "does not return a value",
                                                           instance,
                                                           instance.getImplementation(),
                                                           params));
   }

   private static UnsupportedOperationException unsupported(Implementation implementation,
                                                            StaticOperation<?> instanceOperation,
                                                            Object... params)
   {

      return new UnsupportedOperationException(buildExceptionText(instanceOperation,
                                                                  "is not supported",
                                                                  null,
                                                                  implementation,
                                                                  params));
   }

   private static UnsupportedOperationException unsupported(ImplementationDefined instance,
                                                            InstanceOperation<?, ?> instanceOperation,
                                                            Object... params)
   {
      return new UnsupportedOperationException(buildExceptionText(instanceOperation,
                                                                  "is not supported",
                                                                  instance,
                                                                  instance.getImplementation(),
                                                                  params));
   }

   private static String buildExceptionText(Operation<?> operation,
                                            String msg,
                                            @Nullable ImplementationDefined instance,
                                            Implementation implementation,
                                            Object... params)
   {
      return operation.getName() +
             " " + msg + " " +
             (params.length == 0 ? "" : "with params " + Arrays.toString(params) + " ") +
             (instance == null ? "" : "for " + instance + " ") +
             "with implementation " +
             implementation;
   }
}
