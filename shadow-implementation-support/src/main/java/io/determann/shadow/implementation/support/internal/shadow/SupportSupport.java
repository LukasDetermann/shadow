package io.determann.shadow.implementation.support.internal.shadow;

import io.determann.shadow.api.ImplementationDefined;
import io.determann.shadow.api.shadow.Operation;
import io.determann.shadow.api.shadow.Operation0;
import io.determann.shadow.api.shadow.Response;

import java.util.*;

import static io.determann.shadow.api.shadow.Provider.request;
import static io.determann.shadow.api.shadow.Provider.requestOrEmpty;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.joining;

public class SupportSupport
{
   @SafeVarargs
   public static <TYPE extends ImplementationDefined> boolean equals(TYPE type,
                                                                     Class<TYPE> tClass,
                                                                     Object other,
                                                                     Operation0<? super TYPE, ?>... operations)
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
            .allMatch(tOperation ->
                      {
                         Response<?> first = request(type, tOperation);
                         Response<?> second = request(otherTYPE, tOperation);

                         return first instanceof Response.Unsupported<?> ||
                                second instanceof Response.Unsupported<?> ||
                                Objects.equals(first, second);
                      });
   }

   @SafeVarargs
   public static <TYPE extends ImplementationDefined> int hashCode(TYPE type, Operation0<? super TYPE, ?>... operations)
   {
      return Objects.hash(stream(operations).map(typeOperation -> request(type, typeOperation)).toArray());
   }

   @SafeVarargs
   public static <TYPE extends ImplementationDefined> String toString(TYPE type, Class<TYPE> typeClass, Operation0<? super TYPE, ?>... operations)
   {
      return typeClass.getSimpleName() +
             " {" +
             stream(operations)
                   .map(operation -> getOperationName(operation) + "=" + requestOrEmpty(type, operation)
                         .map(SupportSupport::sortCollection)
                         .map(Object::toString)
                         .orElse(""))
                   .collect(joining(", ")) +
             "}";
   }
   
   private static Object sortCollection(Object o)
   {
      if (!(o instanceof Collection<?> collection) || collection.isEmpty() || !(collection.iterator().next() instanceof Comparable))
      {
         return o;
      }
      //noinspection unchecked
      List<Comparable<Comparable<?>>> objects = (List<Comparable<Comparable<?>>>) new ArrayList<>((collection));
      Collections.sort(objects);

      return objects;
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
