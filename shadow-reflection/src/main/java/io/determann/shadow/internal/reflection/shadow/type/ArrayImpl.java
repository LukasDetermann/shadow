package io.determann.shadow.internal.reflection.shadow.type;

import io.determann.shadow.api.reflection.ReflectionAdapter;
import io.determann.shadow.api.reflection.shadow.type.ArrayReflection;
import io.determann.shadow.api.reflection.shadow.type.ShadowReflection;
import io.determann.shadow.api.shadow.TypeKind;
import io.determann.shadow.api.shadow.structure.RecordComponent;
import io.determann.shadow.api.shadow.structure.Variable;
import io.determann.shadow.api.shadow.type.Array;
import io.determann.shadow.api.shadow.type.Declared;
import io.determann.shadow.api.shadow.type.Primitive;
import io.determann.shadow.api.shadow.type.Shadow;
import io.determann.shadow.implementation.support.api.shadow.type.ArraySupport;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static io.determann.shadow.api.shadow.Operations.*;
import static io.determann.shadow.api.shadow.Provider.requestOrThrow;
import static io.determann.shadow.internal.reflection.ReflectionProvider.IMPLEMENTATION_NAME;

public class ArrayImpl implements ArrayReflection,
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
      if (!(shadow instanceof Array otherArray))
      {
         return false;
      }
      Shadow otherComponentShadow = requestOrThrow(otherArray, ARRAY_GET_COMPONENT_TYPE);

      if (componentShadow instanceof Array nestedArray && otherComponentShadow instanceof Array)
      {
         return requestOrThrow(nestedArray, ARRAY_IS_SUBTYPE_OF, otherComponentShadow);
      }
      if (componentShadow instanceof Declared declared)
      {
         return requestOrThrow(declared, DECLARED_IS_SUBTYPE_OF, otherComponentShadow);
      }
      if (componentShadow instanceof RecordComponent recordComponent)
      {
         return requestOrThrow(recordComponent, RECORD_COMPONENT_IS_SUBTYPE_OF, otherComponentShadow);
      }
      if (componentShadow instanceof Variable variable)
      {
         return requestOrThrow(variable, VARIABLE_IS_SUBTYPE_OF, otherComponentShadow);
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
         return requestOrThrow(((Array) componentShadow), ARRAY_GET_DIRECT_SUPER_TYPES)
               .stream()
               .map(shadow ->
                    {
                       TypeKind typeKind = requestOrThrow(shadow, SHADOW_GET_KIND);
                       if (TypeKind.ARRAY.equals(typeKind))
                       {
                          return ReflectionAdapter.particularize(((Array) shadow));
                       }
                       if (typeKind.isDeclared())
                       {
                          return ReflectionAdapter.particularize(((Declared) shadow));
                       }
                       if (typeKind.isPrimitive())
                       {
                          return ReflectionAdapter.particularize((Primitive) shadow);
                       }
                       throw new IllegalStateException();
                    })
               .map(aClass -> java.lang.reflect.Array.newInstance(aClass, 0).getClass())
               .map(ReflectionAdapter::generalize)
               .map(Shadow.class::cast)
               .toList();
      }

      List<Class<?>> directSuperTypes = new ArrayList<>();

      if (componentShadow instanceof Declared declared)
      {
         Class<?> reflection = ReflectionAdapter.particularize(declared);
         directSuperTypes.add(reflection.getSuperclass());
         Collections.addAll(directSuperTypes, reflection.getInterfaces());
      }

      return directSuperTypes.stream()
                             .map(aClass -> java.lang.reflect.Array.newInstance(aClass, 0).getClass())
                             .map(ReflectionAdapter::generalize)
                             .map(Shadow.class::cast)
                             .toList();
   }

   private static boolean isPrimitiveOrObject(Shadow componentShadow)
   {
      return requestOrThrow(componentShadow, SHADOW_GET_KIND).isPrimitive() ||
             componentShadow instanceof Declared declared &&
             requestOrThrow(declared, QUALIFIED_NAMEABLE_GET_QUALIFIED_NAME).equals("java.lang.Object");
   }

   @Override
   public TypeKind getKind()
   {
      return TypeKind.ARRAY;
   }

   @Override
   public boolean representsSameType(Shadow shadow)
   {
      return ArraySupport.representsSameType(this, shadow);
   }

   public Class<?> getArray()
   {
      return array;
   }

   @Override
   public int hashCode()
   {
      return ArraySupport.hashCode(this);
   }

   @Override
   public boolean equals(Object other)
   {
      return ArraySupport.equals(this, other);
   }

   @Override
   public String toString()
   {
      return ArraySupport.toString(this);
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
