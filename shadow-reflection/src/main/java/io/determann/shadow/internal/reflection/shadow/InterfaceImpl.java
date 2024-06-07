package io.determann.shadow.internal.reflection.shadow;

import io.determann.shadow.api.converter.Converter;
import io.determann.shadow.api.reflection.ReflectionAdapter;
import io.determann.shadow.api.reflection.query.InterfaceReflection;
import io.determann.shadow.api.shadow.Generic;
import io.determann.shadow.api.shadow.Method;
import io.determann.shadow.api.shadow.Shadow;

import java.util.*;
import java.util.stream.Collector;

import static io.determann.shadow.meta_meta.Operations.INTERFACE_GET_GENERIC_TYPES;
import static io.determann.shadow.meta_meta.Operations.SHADOW_REPRESENTS_SAME_TYPE;
import static io.determann.shadow.meta_meta.Provider.requestOrThrow;
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
      return shadow != null &&
             Converter.convert(shadow)
                      .toInterface()
                      .map(anInterface -> requestOrThrow(anInterface, INTERFACE_GET_GENERIC_TYPES)
                            .stream()
                            .allMatch(shadow1 -> getGenericTypes().stream()
                                                                  .anyMatch(shadow2 -> requestOrThrow(shadow2,
                                                                                                      SHADOW_REPRESENTS_SAME_TYPE,
                                                                                                      shadow1))))
                      .orElse(false);
   }

}
