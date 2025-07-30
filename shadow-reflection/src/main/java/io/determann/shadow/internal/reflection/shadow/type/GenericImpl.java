package io.determann.shadow.internal.reflection.shadow.type;

import io.determann.shadow.api.Implementation;
import io.determann.shadow.api.reflection.R_Adapter;
import io.determann.shadow.api.reflection.shadow.R_AnnotationUsage;
import io.determann.shadow.api.reflection.shadow.type.R_Generic;
import io.determann.shadow.api.reflection.shadow.type.R_Interface;
import io.determann.shadow.api.reflection.shadow.type.R_Type;
import io.determann.shadow.api.shadow.type.C_Generic;
import io.determann.shadow.api.shadow.type.C_Type;
import io.determann.shadow.implementation.support.api.shadow.type.GenericSupport;

import java.lang.reflect.TypeVariable;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static io.determann.shadow.api.Operations.GENERIC_GET_BOUND;
import static io.determann.shadow.api.Operations.TYPE_REPRESENTS_SAME_TYPE;
import static io.determann.shadow.api.Provider.requestOrThrow;
import static io.determann.shadow.api.reflection.R_Adapter.IMPLEMENTATION;
import static io.determann.shadow.api.reflection.R_Adapter.generalize;

public class GenericImpl implements R_Generic
{
   private final TypeVariable<?> typeVariable;

   public GenericImpl(TypeVariable<?> typeVariable)
   {
      this.typeVariable = typeVariable;
   }

   @Override
   public String getName()
   {
      return getTypeVariable().getName();
   }

   @Override
   public List<R_AnnotationUsage> getAnnotationUsages()
   {
      return Arrays.stream(getTypeVariable().getAnnotations()).map(R_Adapter::generalize).toList();
   }

   @Override
   public List<R_AnnotationUsage> getDirectAnnotationUsages()
   {
      return Arrays.stream(getTypeVariable().getDeclaredAnnotations()).map(R_Adapter::generalize).toList();
   }

   @Override
   public R_Type getBound()
   {
      return getBounds().getFirst();
   }

   @Override
   public List<R_Type> getBounds()
   {
      return Arrays.stream(getTypeVariable().getBounds())
                   .map(R_Adapter::generalize)
                   .toList();
   }

   @Override
   public List<R_Interface> getAdditionalBounds()
   {
      List<R_Type> bounds = getBounds();
      if (bounds.size() <= 1)
      {
         return Collections.emptyList();
      }
      return bounds.stream().skip(1)
                   .map(R_Interface.class::cast)
                   .toList();
   }

   @Override
   public Object getEnclosing()
   {
      if (getTypeVariable().getGenericDeclaration() instanceof Class<?> aClass)
      {
         return generalize(aClass);
      }
      if (getTypeVariable().getGenericDeclaration() instanceof java.lang.reflect.Executable executable)
      {
         return generalize(executable);
      }
      throw new IllegalStateException();
   }

   @Override
   public boolean representsSameType(C_Type type)
   {
      if (!(type instanceof C_Generic generic))
      {
         return false;
      }

      return requestOrThrow(requestOrThrow(generic, GENERIC_GET_BOUND), TYPE_REPRESENTS_SAME_TYPE, getBound());
   }

   public TypeVariable<?> getTypeVariable()
   {
      return typeVariable;
   }

   @Override
   public String toString()
   {
      return GenericSupport.toString(this);
   }

   @Override
   public int hashCode()
   {
      return GenericSupport.hashCode(this);
   }

   @Override
   public boolean equals(Object other)
   {
      return GenericSupport.equals(this, other);
   }

   public TypeVariable<?> getReflection()
   {
      return getTypeVariable();
   }


   @Override
   public Implementation getImplementation()
   {
      return IMPLEMENTATION;
   }
}