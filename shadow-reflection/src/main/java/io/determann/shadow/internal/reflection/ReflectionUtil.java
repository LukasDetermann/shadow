package io.determann.shadow.internal.reflection;

import io.determann.shadow.api.shadow.modifier.Modifier;

import java.util.*;
import java.util.function.IntFunction;
import java.util.function.IntPredicate;
import java.util.stream.Collectors;

public class ReflectionUtil
{
   private static final Set<IntFunction<Optional<Modifier>>> MODIFIER_MAPPERS = new HashSet<>();

   static
   {
      addModifierMapper(java.lang.reflect.Modifier::isPublic, Modifier.PUBLIC);
      addModifierMapper(java.lang.reflect.Modifier::isPrivate, Modifier.PRIVATE);
      addModifierMapper(java.lang.reflect.Modifier::isProtected, Modifier.PROTECTED);
      addModifierMapper(java.lang.reflect.Modifier::isStatic, Modifier.STATIC);
      addModifierMapper(java.lang.reflect.Modifier::isFinal, Modifier.FINAL);
      addModifierMapper(java.lang.reflect.Modifier::isSynchronized, Modifier.SYNCHRONIZED);
      addModifierMapper(java.lang.reflect.Modifier::isVolatile, Modifier.VOLATILE);
      addModifierMapper(java.lang.reflect.Modifier::isTransient, Modifier.TRANSIENT);
      addModifierMapper(java.lang.reflect.Modifier::isNative, Modifier.NATIVE);
      addModifierMapper(java.lang.reflect.Modifier::isAbstract, Modifier.ABSTRACT);
      addModifierMapper(java.lang.reflect.Modifier::isStatic, Modifier.STATIC);
   }

   private static void addModifierMapper(IntPredicate hasModifier, Modifier modifier)
   {
      MODIFIER_MAPPERS.add(value -> hasModifier.test(value) ? Optional.of(modifier) : Optional.empty());
   }

   public static Set<Modifier> getModifiers(int modifiers, boolean isSealed, boolean isNonSealed, boolean isDefault)
   {
      Set<Modifier> result = MODIFIER_MAPPERS.stream()
                                             .map(mapper -> mapper.apply(modifiers))
                                             .filter(Optional::isPresent)
                                             .map(Optional::get)
                                             .collect(Collectors.toCollection(() -> EnumSet.noneOf(Modifier.class)));
      if (isSealed)
      {
         result.add(Modifier.SEALED);
      }
      if (isNonSealed)
      {
         result.add(Modifier.NON_SEALED);
      }
      if (isDefault)
      {
         result.add(Modifier.DEFAULT);
      }
      return Collections.unmodifiableSet(result);
   }
}
