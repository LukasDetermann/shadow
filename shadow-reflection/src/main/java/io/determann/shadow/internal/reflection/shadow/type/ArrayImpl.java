package io.determann.shadow.internal.reflection.shadow.type;

import io.determann.shadow.api.reflection.R_Adapter;
import io.determann.shadow.api.reflection.shadow.type.R_Array;
import io.determann.shadow.api.reflection.shadow.type.R_Declared;
import io.determann.shadow.api.reflection.shadow.type.R_Primitive;
import io.determann.shadow.api.reflection.shadow.type.R_Shadow;
import io.determann.shadow.api.shadow.C_TypeKind;
import io.determann.shadow.api.shadow.structure.C_RecordComponent;
import io.determann.shadow.api.shadow.structure.C_Variable;
import io.determann.shadow.api.shadow.type.C_Array;
import io.determann.shadow.api.shadow.type.C_Declared;
import io.determann.shadow.api.shadow.type.C_Shadow;
import io.determann.shadow.implementation.support.api.shadow.type.ArraySupport;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static io.determann.shadow.api.Operations.*;
import static io.determann.shadow.api.Provider.requestOrThrow;
import static io.determann.shadow.internal.reflection.ReflectionProvider.IMPLEMENTATION_NAME;

public class ArrayImpl implements R_Array,
                                  R_Shadow
{
   private static final List<R_Shadow> PRIMITIVE_SUPERTYPES = List.of(new ClassImpl(Object.class),
                                                                      new InterfaceImpl(Cloneable.class),
                                                                      new InterfaceImpl(Serializable.class));
   private final Class<?> array;

   public ArrayImpl(Class<?> array)
   {
      this.array = array;
   }

   @Override
   public boolean isSubtypeOf(C_Shadow shadow)
   {
      C_Shadow componentShadow = getComponentType();
      if (isPrimitiveOrObject(componentShadow))
      {
         return PRIMITIVE_SUPERTYPES.stream().anyMatch(shadow::equals);
      }
      if (!(shadow instanceof C_Array otherArray))
      {
         return false;
      }
      C_Shadow otherComponentShadow = requestOrThrow(otherArray, ARRAY_GET_COMPONENT_TYPE);

      if (componentShadow instanceof C_Array nestedArray && otherComponentShadow instanceof C_Array)
      {
         return requestOrThrow(nestedArray, ARRAY_IS_SUBTYPE_OF, otherComponentShadow);
      }
      if (componentShadow instanceof C_Declared declared)
      {
         return requestOrThrow(declared, DECLARED_IS_SUBTYPE_OF, otherComponentShadow);
      }
      if (componentShadow instanceof C_RecordComponent recordComponent)
      {
         return requestOrThrow(recordComponent, RECORD_COMPONENT_IS_SUBTYPE_OF, otherComponentShadow);
      }
      if (componentShadow instanceof C_Variable variable)
      {
         return requestOrThrow(variable, VARIABLE_IS_SUBTYPE_OF, otherComponentShadow);
      }
      return false;
   }

   @Override
   public R_Shadow getComponentType()
   {
      return R_Adapter.generalize(getArray().getComponentType());
   }

   @Override
   public List<R_Shadow> getDirectSuperTypes()
   {
      C_Shadow componentShadow = getComponentType();
      if (isPrimitiveOrObject(componentShadow))
      {
         return PRIMITIVE_SUPERTYPES;
      }
      if (C_TypeKind.ARRAY.equals(requestOrThrow(getComponentType(), SHADOW_GET_KIND)))
      {
         return requestOrThrow(((C_Array) componentShadow), ARRAY_GET_DIRECT_SUPER_TYPES)
               .stream()
               .map(shadow ->
                    {
                       C_TypeKind typeKind = requestOrThrow(shadow, SHADOW_GET_KIND);
                       if (C_TypeKind.ARRAY.equals(typeKind))
                       {
                          return R_Adapter.particularize(((R_Array) shadow));
                       }
                       if (typeKind.isDeclared())
                       {
                          return R_Adapter.particularize(((R_Declared) shadow));
                       }
                       if (typeKind.isPrimitive())
                       {
                          return R_Adapter.particularize((R_Primitive) shadow);
                       }
                       throw new IllegalStateException();
                    })
               .map(aClass -> java.lang.reflect.Array.newInstance(aClass, 0).getClass())
               .map(R_Adapter::generalize)
               .map(R_Shadow.class::cast)
               .toList();
      }

      List<Class<?>> directSuperTypes = new ArrayList<>();

      if (componentShadow instanceof C_Declared declared)
      {
         Class<?> reflection = R_Adapter.particularize((R_Declared) declared);
         directSuperTypes.add(reflection.getSuperclass());
         Collections.addAll(directSuperTypes, reflection.getInterfaces());
      }

      return directSuperTypes.stream()
                             .map(aClass -> java.lang.reflect.Array.newInstance(aClass, 0).getClass())
                             .map(R_Adapter::generalize)
                             .map(R_Shadow.class::cast)
                             .toList();
   }

   private static boolean isPrimitiveOrObject(C_Shadow componentShadow)
   {
      return requestOrThrow(componentShadow, SHADOW_GET_KIND).isPrimitive() ||
             componentShadow instanceof C_Declared declared &&
             requestOrThrow(declared, QUALIFIED_NAMEABLE_GET_QUALIFIED_NAME).equals("java.lang.Object");
   }

   @Override
   public C_TypeKind getKind()
   {
      return C_TypeKind.ARRAY;
   }

   @Override
   public boolean representsSameType(C_Shadow shadow)
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
