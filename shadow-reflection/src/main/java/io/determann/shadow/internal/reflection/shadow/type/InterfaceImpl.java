package io.determann.shadow.internal.reflection.shadow.type;

import io.determann.shadow.api.reflection.R_Adapter;
import io.determann.shadow.api.reflection.shadow.type.R_Generic;
import io.determann.shadow.api.reflection.shadow.type.R_Interface;
import io.determann.shadow.api.reflection.shadow.type.R_Type;
import io.determann.shadow.api.shadow.modifier.C_Modifier;
import io.determann.shadow.api.shadow.structure.C_Method;
import io.determann.shadow.api.shadow.type.C_Interface;
import io.determann.shadow.api.shadow.type.C_Type;
import io.determann.shadow.implementation.support.api.shadow.type.InterfaceSupport;

import java.util.*;
import java.util.stream.Collector;

import static io.determann.shadow.api.Operations.*;
import static io.determann.shadow.api.Provider.requestOrThrow;
import static java.util.stream.Collector.Characteristics.UNORDERED;

public class InterfaceImpl extends DeclaredImpl implements R_Interface
{
   private final List<R_Type> genericTypes;

   public InterfaceImpl(Class<?> aClass)
   {
      this(aClass, Collections.emptyList());
   }

   public InterfaceImpl(Class<?> aClass, List<R_Type> genericTypes)
   {
      super(aClass);
      this.genericTypes = genericTypes;
   }

   @Override
   public boolean isFunctional()
   {
      return getMethods()
            .stream()
            .filter(method -> requestOrThrow(method, MODIFIABLE_HAS_MODIFIER, C_Modifier.ABSTRACT) || isObjectMethod(method))
            .collect(Collector.
                           <C_Method, Set<C_Method>, Boolean>
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

   private boolean isObjectMethod(C_Method method)
   {
      return new ClassImpl(Object.class)
            .getMethods()
            .stream()
            .anyMatch(method1 -> requestOrThrow(method, METHOD_OVERRIDES, method1));
   }

   @Override
   public List<R_Type> getGenericTypes()
   {
      return genericTypes;
   }

   @Override
   public List<R_Generic> getGenerics()
   {
      return Arrays.stream(getaClass().getTypeParameters()).map(R_Adapter::generalize).map(R_Generic.class::cast).toList();
   }

   @Override
   public boolean representsSameType(C_Type type)
   {
      return type instanceof C_Interface anInterface &&
             requestOrThrow(anInterface, INTERFACE_GET_GENERIC_TYPES)
                   .stream()
                   .allMatch(type1 -> getGenericTypes().stream()
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
