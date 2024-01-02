package io.determann.shadow.impl.reflection.shadow;

import io.determann.shadow.api.TypeKind;
import io.determann.shadow.api.converter.Converter;
import io.determann.shadow.api.reflection.ReflectionAdapter;
import io.determann.shadow.api.shadow.Array;
import io.determann.shadow.api.shadow.Shadow;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static io.determann.shadow.api.converter.Converter.convert;

public class ArrayImpl implements Array
{
   private static final List<Shadow> PRIMITIVE_SUPERTYPES = List.of(new ClassImpl(Object.class),
                                                                    new InterfaceImpl(Cloneable.class),
                                                                    new InterfaceImpl(Serializable.class));
   private final Class<?> array;

   public ArrayImpl(Class<?> array)
   {
      this.array = array;
   }

   @Override
   public boolean isSubtypeOf(Shadow shadow)
   {
      Shadow componentType = getComponentType();
      if (isPrimitiveOrObject(componentType))
      {
         return PRIMITIVE_SUPERTYPES.stream().anyMatch(shadow::equals);
      }
      if (!shadow.isTypeKind(TypeKind.ARRAY))
      {
         return false;
      }
      Array otherArray = convert(shadow).toArrayOrThrow();
      Shadow otherComponentType = otherArray.getComponentType();

      if (componentType.isTypeKind(TypeKind.ARRAY) && otherComponentType.isTypeKind(TypeKind.ARRAY))
      {
         Array nestedArray = convert(componentType).toArrayOrThrow();
         return nestedArray.isSubtypeOf(otherComponentType);
      }
      if (componentType.getTypeKind().isDeclared())
      {
         return convert(componentType).toDeclaredOrThrow().isSubtypeOf(otherComponentType);
      }
      if (componentType.getTypeKind().isVariable())
      {
         return convert(componentType).toVariableOrThrow().isSubtypeOf(otherComponentType);
      }
      return false;
   }

   @Override
   public Shadow getComponentType()
   {
      return ReflectionAdapter.getShadow(getArray().getComponentType());
   }

   @Override
   public List<Shadow> getDirectSuperTypes()
   {
      Shadow componentType = getComponentType();
      if (isPrimitiveOrObject(componentType))
      {
         return PRIMITIVE_SUPERTYPES;
      }
      if (getComponentType().isTypeKind(TypeKind.ARRAY))
      {
         return convert(componentType).toArrayOrThrow()
                                      .getDirectSuperTypes()
                                      .stream()
                                      .map(shadow -> {
                                         if (shadow.isTypeKind(TypeKind.ARRAY))
                                         {
                                            return ReflectionAdapter.getReflection(Converter.convert(shadow).toArrayOrThrow());
                                         }
                                         if (shadow.getTypeKind().isDeclared())
                                         {
                                            return ReflectionAdapter.getReflection(Converter.convert(shadow).toDeclaredOrThrow());
                                         }
                                         if (shadow.getTypeKind().isPrimitive())
                                         {
                                            return ReflectionAdapter.getReflection(Converter.convert(shadow).toPrimitiveOrThrow());
                                         }
                                         throw new IllegalStateException();
                                      })
                                      .map(aClass -> java.lang.reflect.Array.newInstance(aClass, 0).getClass())
                                      .map(ReflectionAdapter::getShadow)
                                      .map(Shadow.class::cast)
                                      .collect(Collectors.toUnmodifiableList());
      }

      List<Class<?>> directSuperTypes = new ArrayList<>();

      convert(componentType).toDeclared().ifPresent(declared ->
                                                    {
                                                       Class<?> reflection = ReflectionAdapter.getReflection(declared);
                                                       directSuperTypes.add(reflection.getSuperclass());
                                                       Collections.addAll(directSuperTypes, reflection.getInterfaces());
                                                    });

      return directSuperTypes.stream()
                             .map(aClass -> java.lang.reflect.Array.newInstance(aClass, 0).getClass())
                             .map(ReflectionAdapter::getShadow)
                             .map(Shadow.class::cast)
                             .collect(Collectors.toUnmodifiableList());
   }

   private static boolean isPrimitiveOrObject(Shadow componentType)
   {
      return componentType.getTypeKind().isPrimitive() ||
             convert(componentType)
                   .toDeclared()
                   .map(DeclaredImpl.class::cast)
                   .map(declared -> declared.getaClass().equals(Object.class))
                   .orElse(false);
   }

   @Override
   public TypeKind getTypeKind()
   {
      return TypeKind.ARRAY;
   }

   @Override
   public boolean representsSameType(Shadow shadow)
   {
      return shadow != null &&
             convert(shadow).toArray().map(array1 -> array1.getComponentType().representsSameType(getComponentType())).orElse(false);
   }

   public Class<?> getArray()
   {
      return array;
   }

   @Override
   public int hashCode()
   {
      return Objects.hash(getComponentType());
   }

   @Override
   public boolean equals(Object other)
   {
      if (other == this)
      {
         return true;
      }
      if (!(other instanceof Array))
      {
         return false;
      }
      Array otherArray = ((Array) other);
      return Objects.equals(getComponentType(), otherArray.getComponentType());
   }

   public Class<?> getReflection()
   {
      return array;
   }
}
