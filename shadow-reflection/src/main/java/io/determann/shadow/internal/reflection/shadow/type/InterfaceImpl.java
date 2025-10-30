package io.determann.shadow.internal.reflection.shadow.type;

import io.determann.shadow.api.C;
import io.determann.shadow.api.Modifier;
import io.determann.shadow.api.reflection.Adapter;
import io.determann.shadow.api.reflection.R;
import io.determann.shadow.implementation.support.api.shadow.type.InterfaceSupport;

import java.util.*;
import java.util.stream.Collector;

import static io.determann.shadow.api.query.Operations.*;
import static io.determann.shadow.api.query.Provider.requestOrThrow;
import static java.util.stream.Collector.Characteristics.UNORDERED;

public class InterfaceImpl extends DeclaredImpl implements R.Interface
{
   private final List<R.Type> genericTypes;

   public InterfaceImpl(Class<?> aClass)
   {
      this(aClass, Collections.emptyList());
   }

   public InterfaceImpl(Class<?> aClass, List<R.Type> genericTypes)
   {
      super(aClass);
      this.genericTypes = genericTypes;
   }

   @Override
   public boolean isFunctional()
   {
      return getMethods()
            .stream()
            .filter(method -> requestOrThrow(method, MODIFIABLE_HAS_MODIFIER, Modifier.ABSTRACT) || isObjectMethod(method))
            .collect(Collector.
                           <C.Method, Set<C.Method>, Boolean>
                           of(HashSet::new, (methods, method) -> methods.stream()
                                                                        .anyMatch(method1 -> requestOrThrow(method1, METHOD_OVERRIDES, method) ||
                                                                                             requestOrThrow(method, METHOD_OVERRIDES, method1)),
                              (methods, methods2) ->
                              {
                                 methods.addAll(methods2);
                                 return methods2;
                              },
                              methods -> methods.size() == 1,
                              UNORDERED));
   }

   private boolean isObjectMethod(C.Method method)
   {
      return new ClassImpl(Object.class)
            .getMethods()
            .stream()
            .anyMatch(method1 -> requestOrThrow(method, METHOD_OVERRIDES, method1));
   }

   @Override
   public List<R.Type> getGenericUsages()
   {
      return genericTypes;
   }

   @Override
   public List<R.Generic> getGenericDeclarations()
   {
      return Arrays.stream(getaClass().getTypeParameters()).map(Adapter::generalize).map(R.Generic.class::cast).toList();
   }

   @Override
   public boolean representsSameType(C.Type type)
   {
      return type instanceof C.Interface anInterface &&
             requestOrThrow(anInterface, INTERFACE_GET_GENERIC_USAGES)
                   .stream()
                   .allMatch(type1 -> getGenericUsages().stream()
                                                        .anyMatch(type2 -> requestOrThrow(type2, TYPE_REPRESENTS_SAME_TYPE, type1)));
   }

   @Override
   public boolean equals(Object other)
   {
      return InterfaceSupport.equals(this, other);
   }

   @Override
   public int hashCode()
   {
      return InterfaceSupport.hashCode(this);
   }

   @Override
   public String toString()
   {
      return InterfaceSupport.toString(this);
   }
}
