package io.determann.shadow.internal.reflection.shadow;

import io.determann.shadow.api.TypeKind;
import io.determann.shadow.api.reflection.ReflectionAdapter;
import io.determann.shadow.api.reflection.query.ShadowReflection;
import io.determann.shadow.api.shadow.Array;
import io.determann.shadow.api.shadow.Shadow;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static io.determann.shadow.api.converter.Converter.convert;
import static io.determann.shadow.internal.reflection.ReflectionProvider.IMPLEMENTATION_NAME;
import static io.determann.shadow.meta_meta.Operations.SHADOW_GET_KIND;
import static io.determann.shadow.meta_meta.Operations.SHADOW_REPRESENTS_SAME_TYPE;
import static io.determann.shadow.meta_meta.Provider.requestOrThrow;

public class ArrayImpl implements Array,
                                  ShadowReflection
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
      Shadow componentShadow = getComponentType();
      if (isPrimitiveOrObject(componentShadow))
      {
         return PRIMITIVE_SUPERTYPES.stream().anyMatch(shadow::equals);
      }
      if (!TypeKind.ARRAY.equals(requestOrThrow(shadow, SHADOW_GET_KIND)))
      {
         return false;
      }
      Array otherArray = convert(shadow).toArrayOrThrow();
      Shadow otherComponentShadow = otherArray.getComponentType();

      if (TypeKind.ARRAY.equals(requestOrThrow(shadow, SHADOW_GET_KIND)) && TypeKind.ARRAY.equals(requestOrThrow(otherComponentShadow, SHADOW_GET_KIND)))
      {
         Array nestedArray = convert(componentShadow).toArrayOrThrow();
         return nestedArray.isSubtypeOf(otherComponentShadow);
      }
      if (requestOrThrow(shadow, SHADOW_GET_KIND).isDeclared())
      {
         return convert(componentShadow).toDeclaredOrThrow().isSubtypeOf(otherComponentShadow);
      }
      if (TypeKind.RECORD_COMPONENT.equals(requestOrThrow(componentShadow, SHADOW_GET_KIND)))
      {
         return convert(componentShadow).toRecordComponentOrThrow().isSubtypeOf(otherComponentShadow);
      }
      if (requestOrThrow(componentShadow, SHADOW_GET_KIND).isVariable())
      {
         return convert(componentShadow).toVariableOrThrow().isSubtypeOf(otherComponentShadow);
      }
      return false;
   }

   @Override
   public Shadow getComponentType()
   {
      return ReflectionAdapter.generalize(getArray().getComponentType());
   }

   @Override
   public List<Shadow> getDirectSuperTypes()
   {
      Shadow componentShadow = getComponentType();
      if (isPrimitiveOrObject(componentShadow))
      {
         return PRIMITIVE_SUPERTYPES;
      }
      if (TypeKind.ARRAY.equals(requestOrThrow(getComponentType(), SHADOW_GET_KIND)))
      {
         return convert(componentShadow).toArrayOrThrow()
                                        .getDirectSuperTypes()
                                        .stream()
                                        .map(shadow -> {
                                         if (TypeKind.ARRAY.equals(requestOrThrow(shadow, SHADOW_GET_KIND)))
                                         {
                                            return ReflectionAdapter.particularize(convert(shadow).toArrayOrThrow());
                                         }
                                         if (requestOrThrow(shadow, SHADOW_GET_KIND).isDeclared())
                                         {
                                            return ReflectionAdapter.particularize(convert(shadow).toDeclaredOrThrow());
                                         }
                                         if (requestOrThrow(shadow, SHADOW_GET_KIND).isPrimitive())
                                         {
                                            return ReflectionAdapter.particularize(convert(shadow).toPrimitiveOrThrow());
                                         }
                                         throw new IllegalStateException();
                                      })
                                        .map(aClass -> java.lang.reflect.Array.newInstance(aClass, 0).getClass())
                                        .map(ReflectionAdapter::generalize)
                                        .map(Shadow.class::cast)
                                        .toList();
      }

      List<Class<?>> directSuperTypes = new ArrayList<>();

      convert(componentShadow).toDeclared().ifPresent(declared ->
                                                    {
                                                       Class<?> reflection = ReflectionAdapter.particularize(declared);
                                                       directSuperTypes.add(reflection.getSuperclass());
                                                       Collections.addAll(directSuperTypes, reflection.getInterfaces());
                                                    });

      return directSuperTypes.stream()
                             .map(aClass -> java.lang.reflect.Array.newInstance(aClass, 0).getClass())
                             .map(ReflectionAdapter::generalize)
                             .map(Shadow.class::cast)
                             .toList();
   }

   private static boolean isPrimitiveOrObject(Shadow componentShadow)
   {
      return requestOrThrow(componentShadow, SHADOW_GET_KIND).isPrimitive() ||
             convert(componentShadow)
                   .toDeclared()
                   .map(DeclaredImpl.class::cast)
                   .map(declared -> declared.getaClass().equals(Object.class))
                   .orElse(false);
   }

   @Override
   public TypeKind getKind()
   {
      return TypeKind.ARRAY;
   }

   @Override
   public boolean representsSameType(Shadow shadow)
   {
      return shadow != null &&
             (equals(shadow) ||
              convert(shadow).toArray()
                             .map(array1 -> requestOrThrow(array1.getComponentType(), SHADOW_REPRESENTS_SAME_TYPE, getComponentType()))
                             .orElse(false));
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
      if (!(other instanceof Array otherArray))
      {
         return false;
      }
      return Objects.equals(getComponentType(), otherArray.getComponentType());
   }

   public Class<?> getReflection()
   {
      return array;
   }

   @Override
   public String getImplementationName()
   {
      return IMPLEMENTATION_NAME;
   }
}
