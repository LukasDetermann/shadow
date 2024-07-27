package io.determann.shadow.implementation.support.internal.shadow;

import io.determann.shadow.api.ImplementationDefined;
import io.determann.shadow.api.shadow.Operation;
import io.determann.shadow.api.shadow.Operation0;

import java.util.Objects;

import static io.determann.shadow.api.shadow.Provider.requestOrEmpty;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.joining;

public class SupportSupport
{
   @SafeVarargs
   public static <TYPE extends ImplementationDefined> boolean equals(TYPE type,
                                                                     Class<TYPE> tClass,
                                                                     Object other,
                                                                     Operation0<TYPE, ?>... operations)
   {
      if (type == other)
      {
         return true;
      }
      if (!tClass.isInstance(other))
      {
         return false;
      }
      //noinspection unchecked
      TYPE otherTYPE = (TYPE) other;

      return stream(operations)
            .allMatch(tOperation -> Objects.equals(requestOrEmpty(type, tOperation), requestOrEmpty(otherTYPE, tOperation)));
   }

   @SafeVarargs
   public static <TYPE extends ImplementationDefined> int hashCode(TYPE type, Operation0<TYPE, ?>... operations)
   {
      return Objects.hash(stream(operations).map(typeOperation -> requestOrEmpty(type, typeOperation)).toArray());
   }

   @SafeVarargs
   public static <TYPE extends ImplementationDefined> String toString(TYPE type, Class<TYPE> typeClass, Operation0<TYPE, ?>... operations)
   {
      return typeClass.getSimpleName() +
             " {" +
             stream(operations)
                   .map(operation -> getOperationName(operation) + "=" + requestOrEmpty(type, operation).map(Object::toString).orElse(""))
                   .collect(joining(", ")) +
             "}";
   }

   private static String getOperationName(Operation<?, ?> operation)
   {
      String name = operation.getName();
      int index = name.lastIndexOf(".");
      if (index == -1)
      {
         return name;
      }

      return name.substring(index + 1);
   }
}
