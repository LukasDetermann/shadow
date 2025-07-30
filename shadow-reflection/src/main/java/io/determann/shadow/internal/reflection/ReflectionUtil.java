package io.determann.shadow.internal.reflection;

import io.determann.shadow.api.Modifier;

import java.util.*;
import java.util.function.IntFunction;
import java.util.function.IntPredicate;
import java.util.stream.Collectors;

import static io.determann.shadow.api.Modifier.*;

public class ReflectionUtil
{
   private static final Set<IntFunction<Optional<Modifier>>> MODIFIER_MAPPERS = new HashSet<>();

   static
   {
      addModifierMapper(java.lang.reflect.Modifier::isPublic, PUBLIC);
      addModifierMapper(java.lang.reflect.Modifier::isPrivate, PRIVATE);
      addModifierMapper(java.lang.reflect.Modifier::isProtected, PROTECTED);
      addModifierMapper(java.lang.reflect.Modifier::isStatic, STATIC);
      addModifierMapper(java.lang.reflect.Modifier::isFinal, FINAL);
      addModifierMapper(java.lang.reflect.Modifier::isSynchronized, SYNCHRONIZED);
      addModifierMapper(java.lang.reflect.Modifier::isVolatile, VOLATILE);
      addModifierMapper(java.lang.reflect.Modifier::isTransient, TRANSIENT);
      addModifierMapper(java.lang.reflect.Modifier::isNative, NATIVE);
      addModifierMapper(java.lang.reflect.Modifier::isAbstract, ABSTRACT);
      addModifierMapper(java.lang.reflect.Modifier::isStatic, STATIC);
   }

   private static void addModifierMapper(IntPredicate hasModifier, Modifier modifier)
   {
      MODIFIER_MAPPERS.add(value -> hasModifier.test(value) ? Optional.of(modifier) : Optional.empty());
   }

   public static Set<Modifier> getModifiers(int modifiers, boolean isSealed, boolean isNonSealed, boolean isDefault, boolean isPackagePrivate)
   {
      Set<Modifier> result = MODIFIER_MAPPERS.stream()
                                             .map(mapper -> mapper.apply(modifiers))
                                             .filter(Optional::isPresent)
                                             .map(Optional::get)
                                             .collect(Collectors.toCollection(() -> EnumSet.noneOf(Modifier.class)));

      if (isPackagePrivate)
      {
         result.add(PACKAGE_PRIVATE);
      }
      if (isSealed)
      {
         result.add(SEALED);
      }
      if (isNonSealed)
      {
         result.add(NON_SEALED);
      }
      if (isDefault)
      {
         result.add(DEFAULT);
      }
      return Collections.unmodifiableSet(result);
   }
}
