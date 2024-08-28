package io.determann.shadow.internal.reflection.shadow.type;

import io.determann.shadow.api.reflection.R_Adapter;
import io.determann.shadow.api.reflection.shadow.type.R_Array;
import io.determann.shadow.api.reflection.shadow.type.R_Declared;
import io.determann.shadow.api.reflection.shadow.type.R_Type;
import io.determann.shadow.api.reflection.shadow.type.primitive.R_Primitive;
import io.determann.shadow.api.shadow.structure.C_RecordComponent;
import io.determann.shadow.api.shadow.structure.C_Variable;
import io.determann.shadow.api.shadow.type.C_Array;
import io.determann.shadow.api.shadow.type.C_Declared;
import io.determann.shadow.api.shadow.type.C_Type;
import io.determann.shadow.api.shadow.type.primitive.C_Primitive;
import io.determann.shadow.implementation.support.api.shadow.type.ArraySupport;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static io.determann.shadow.api.Operations.*;
import static io.determann.shadow.api.Provider.requestOrThrow;
import static io.determann.shadow.internal.reflection.ReflectionProvider.IMPLEMENTATION_NAME;

public class ArrayImpl implements R_Array,
                                  R_Type
{
   private static final List<R_Type> PRIMITIVE_SUPERTYPES = List.of(new ClassImpl(Object.class),
                                                                    new InterfaceImpl(Cloneable.class),
                                                                    new InterfaceImpl(Serializable.class));
   private final Class<?> array;

   public ArrayImpl(Class<?> array)
   {
      this.array = array;
   }

   @Override
   public boolean isSubtypeOf(C_Type type)
   {
      C_Type componentType = getComponentType();
      if (isPrimitiveOrObject(componentType))
      {
         return PRIMITIVE_SUPERTYPES.stream().anyMatch(type::equals);
      }
      if (!(type instanceof C_Array otherArray))
      {
         return false;
      }
      C_Type otherComponentType = requestOrThrow(otherArray, ARRAY_GET_COMPONENT_TYPE);

      if (componentType instanceof C_Array nestedArray && otherComponentType instanceof C_Array)
      {
         return requestOrThrow(nestedArray, ARRAY_IS_SUBTYPE_OF, otherComponentType);
      }
      if (componentType instanceof C_Declared declared)
      {
         return requestOrThrow(declared, DECLARED_IS_SUBTYPE_OF, otherComponentType);
      }
      if (componentType instanceof C_RecordComponent recordComponent)
      {
         return requestOrThrow(recordComponent, RECORD_COMPONENT_IS_SUBTYPE_OF, otherComponentType);
      }
      if (componentType instanceof C_Variable variable)
      {
         return requestOrThrow(variable, VARIABLE_IS_SUBTYPE_OF, otherComponentType);
      }
      return false;
   }

   @Override
   public R_Type getComponentType()
   {
      return R_Adapter.generalize(getArray().getComponentType());
   }

   @Override
   public R_Array asArray()
   {
      return R_Adapter.generalize(array.arrayType());
   }

   @Override
   public List<R_Type> getDirectSuperTypes()
   {
      C_Type componentType = getComponentType();
      if (isPrimitiveOrObject(componentType))
      {
         return PRIMITIVE_SUPERTYPES;
      }
      if (componentType instanceof C_Array componentArray)
      {
         return requestOrThrow(componentArray, ARRAY_GET_DIRECT_SUPER_TYPES)
               .stream()
               .map(type ->
                    {
                       if (type instanceof R_Array array)
                       {
                          return R_Adapter.particularize(array);
                       }
                       if (type instanceof R_Declared declared)
                       {
                          return R_Adapter.particularize(declared);
                       }
                       if (type instanceof R_Primitive primitive)
                       {
                          return R_Adapter.particularize(primitive);
                       }
                       throw new IllegalStateException();
                    })
               .map(aClass -> java.lang.reflect.Array.newInstance(aClass, 0).getClass())
               .map(R_Adapter::generalize)
               .map(R_Type.class::cast)
               .toList();
      }

      List<Class<?>> directSuperTypes = new ArrayList<>();

      if (componentType instanceof C_Declared declared)
      {
         Class<?> reflection = R_Adapter.particularize((R_Declared) declared);
         directSuperTypes.add(reflection.getSuperclass());
         Collections.addAll(directSuperTypes, reflection.getInterfaces());
      }

      return directSuperTypes.stream()
                             .map(aClass -> java.lang.reflect.Array.newInstance(aClass, 0).getClass())
                             .map(R_Adapter::generalize)
                             .map(R_Type.class::cast)
                             .toList();
   }

   private static boolean isPrimitiveOrObject(C_Type componentType)
   {
      return componentType instanceof C_Primitive ||
             componentType instanceof C_Declared declared &&
             requestOrThrow(declared, QUALIFIED_NAMEABLE_GET_QUALIFIED_NAME).equals("java.lang.Object");
   }

   @Override
   public boolean representsSameType(C_Type type)
   {
      return ArraySupport.representsSameType(this, type);
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
