package io.determann.shadow.impl.reflection.shadow;

import io.determann.shadow.api.converter.Converter;
import io.determann.shadow.api.reflection.ReflectionAdapter;
import io.determann.shadow.api.shadow.Generic;
import io.determann.shadow.api.shadow.Interface;
import io.determann.shadow.api.shadow.Method;
import io.determann.shadow.api.shadow.Shadow;

import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import static java.util.stream.Collector.Characteristics.UNORDERED;

public class InterfaceImpl extends DeclaredImpl implements Interface
{
   private final List<Shadow> genericTypes;

   public InterfaceImpl(Class<?> aClass)
   {
      this(aClass, Collections.emptyList());
   }

   public InterfaceImpl(Class<?> aClass, List<Shadow> genericTypes)
   {
      super(aClass);
      this.genericTypes = genericTypes;
   }

   @Override
   public boolean isFunctional()
   {
      return getMethods()
            .stream().filter(method -> method.isAbstract() || isObjectMethod(method))
            .collect(Collector.<Method, Set<Method>, Boolean>of(HashSet::new,
                                                                (methods, method) -> methods.stream()
                                                                                            .anyMatch(method1 -> method1.overrides(method) ||
                                                                                                                 method.overrides(method1)),
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
            .anyMatch(method::overrides);
   }

   @Override
   public List<Shadow> getGenericTypes()
   {
      return genericTypes;
   }

   @Override
   public List<Generic> getGenerics()
   {
      return Arrays.stream(getaClass().getTypeParameters())
                   .map(ReflectionAdapter::getShadow)
                   .map(Generic.class::cast)
                   .collect(Collectors.toUnmodifiableList());
   }

   @Override
   public boolean representsSameType(Shadow shadow)
   {
      return shadow != null &&
             Converter.convert(shadow)
                      .toInterface()
                      .map(anInterface -> anInterface
                            .getGenericTypes()
                            .stream()
                            .allMatch(shadow1 -> getGenericTypes().stream()
                                                                  .anyMatch(shadow2 -> shadow2.representsSameType(shadow1))))
                      .orElse(false);
   }
}
