package io.determann.shadow.implementation.support.internal.shadow;

import io.determann.shadow.api.C;
import io.determann.shadow.api.query.ImplementationDefined;
import io.determann.shadow.api.query.Response;
import io.determann.shadow.api.query.operation.InstanceOperation;
import io.determann.shadow.api.query.operation.InstanceOperation0;

import java.util.*;

import static io.determann.shadow.api.query.Operations.TYPE_REPRESENTS_SAME_TYPE;
import static io.determann.shadow.api.query.Provider.request;
import static io.determann.shadow.api.query.Provider.requestOrEmpty;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.joining;

public class SupportSupport
{
   @SafeVarargs
   public static <TYPE extends ImplementationDefined> boolean equals(TYPE type,
                                                                     Class<TYPE> tClass,
                                                                     Object other,
                                                                     InstanceOperation0<? super TYPE, ?>... operations)
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
   public static <TYPE extends ImplementationDefined> int hashCode(TYPE type, InstanceOperation0<? super TYPE, ?>... operations)
   {
      return Objects.hash(stream(operations).map(typeOperation -> request(type, typeOperation)).toArray());
   }

   @SafeVarargs
   public static <TYPE extends ImplementationDefined> String toString(TYPE type,
                                                                      Class<TYPE> typeClass,
                                                                      InstanceOperation0<? super TYPE, ?>... operations)
   {
      String simpleName = typeClass.getSimpleName();
      int index = simpleName.indexOf('.');
      if (index != -1 && index + 1 < simpleName.length())
      {
         simpleName = simpleName.substring(index + 1);
      }

      return simpleName +
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

   private static String getOperationName(InstanceOperation<?, ?> instanceOperation)
   {
      String name = instanceOperation.getName();
      int index = name.lastIndexOf(".");
      if (index == -1)
      {
         return name;
      }

      return name.substring(index + 1);
   }


   @SafeVarargs
   public static <TYPE extends C.Type> boolean representsSameType(TYPE type,
                                                                  Class<TYPE> tClass,
                                                                  Object other,
                                                                  InstanceOperation0<? super TYPE, ?>... operations)
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

                                (first instanceof Response.Result<?>(Object firstValue) &&
                                 firstValue instanceof C.Type firstType &&
                                 second instanceof Response.Result<?>(Object secondValue) &&
                                 secondValue instanceof C.Type secondType &&
                                 requestOrEmpty(firstType, TYPE_REPRESENTS_SAME_TYPE, secondType).orElse(false)) ||

                                Objects.equals(first, second);
                      });
   }
}
