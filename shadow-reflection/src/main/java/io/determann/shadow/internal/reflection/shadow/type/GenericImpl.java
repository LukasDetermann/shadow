package io.determann.shadow.internal.reflection.shadow.type;

import io.determann.shadow.api.C;
import io.determann.shadow.api.query.Implementation;
import io.determann.shadow.api.reflection.Adapter;
import io.determann.shadow.api.reflection.R;
import io.determann.shadow.implementation.support.api.shadow.type.GenericSupport;

import java.lang.reflect.TypeVariable;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static io.determann.shadow.api.query.Operations.GENERIC_GET_BOUND;
import static io.determann.shadow.api.query.Operations.TYPE_REPRESENTS_SAME_TYPE;
import static io.determann.shadow.api.query.Provider.requestOrThrow;
import static io.determann.shadow.api.reflection.Adapter.IMPLEMENTATION;
import static io.determann.shadow.api.reflection.Adapter.generalize;

public class GenericImpl implements R.Generic
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
   public List<R.AnnotationUsage> getAnnotationUsages()
   {
      return Arrays.stream(getTypeVariable().getAnnotations()).map(Adapter::generalize).toList();
   }

   @Override
   public List<R.AnnotationUsage> getDirectAnnotationUsages()
   {
      return Arrays.stream(getTypeVariable().getDeclaredAnnotations()).map(Adapter::generalize).toList();
   }

   @Override
   public R.Type getBound()
   {
      return getBounds().getFirst();
   }

   @Override
   public List<R.Type> getBounds()
   {
      return Arrays.stream(getTypeVariable().getBounds())
                   .map(Adapter::generalize)
                   .toList();
   }

   @Override
   public List<R.Interface> getAdditionalBounds()
   {
      List<R.Type> bounds = getBounds();
      if (bounds.size() <= 1)
      {
         return Collections.emptyList();
      }
      return bounds.stream().skip(1)
                   .map(R.Interface.class::cast)
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
   public boolean representsSameType(C.Type type)
   {
      if (!(type instanceof C.Generic generic))
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