package io.determann.shadow.internal.reflection.shadow.type;

import io.determann.shadow.api.reflection.ReflectionAdapter;
import io.determann.shadow.api.reflection.shadow.type.InterfaceReflection;
import io.determann.shadow.api.shadow.modifier.Modifier;
import io.determann.shadow.api.shadow.structure.Method;
import io.determann.shadow.api.shadow.type.Generic;
import io.determann.shadow.api.shadow.type.Interface;
import io.determann.shadow.api.shadow.type.Shadow;
import io.determann.shadow.implementation.support.api.shadow.type.InterfaceSupport;

import java.util.*;
import java.util.stream.Collector;

import static io.determann.shadow.api.shadow.Operations.*;
import static io.determann.shadow.api.shadow.Provider.requestOrThrow;
import static java.util.stream.Collector.Characteristics.UNORDERED;

public class InterfaceImpl extends DeclaredImpl implements InterfaceReflection
{
   private final List<Shadow> genericShadows;

   public InterfaceImpl(Class<?> aClass)
   {
      this(aClass, Collections.emptyList());
   }

   public InterfaceImpl(Class<?> aClass, List<Shadow> genericShadows)
   {
      super(aClass);
      this.genericShadows = genericShadows;
   }

   @Override
   public boolean isFunctional()
   {
      return getMethods()
            .stream()
            .filter(method -> requestOrThrow(method, MODIFIABLE_HAS_MODIFIER, Modifier.ABSTRACT) || isObjectMethod(method))
            .collect(Collector.
                           <Method, Set<Method>, Boolean>
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

   private boolean isObjectMethod(Method method)
   {
      return new ClassImpl(Object.class)
            .getMethods()
            .stream()
            .anyMatch(method1 -> requestOrThrow(method, METHOD_OVERRIDES, method1));
   }

   @Override
   public List<Shadow> getGenericTypes()
   {
      return genericShadows;
   }

   @Override
   public List<Generic> getGenerics()
   {
      return Arrays.stream(getaClass().getTypeParameters()).map(ReflectionAdapter::generalize).map(Generic.class::cast).toList();
   }

   @Override
   public boolean representsSameType(Shadow shadow)
   {
      return shadow instanceof Interface anInterface &&
             requestOrThrow(anInterface, INTERFACE_GET_GENERIC_TYPES)
                   .stream()
                   .allMatch(shadow1 -> getGenericTypes().stream()
                                                         .anyMatch(shadow2 -> requestOrThrow(shadow2, SHADOW_REPRESENTS_SAME_TYPE, shadow1)));
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
