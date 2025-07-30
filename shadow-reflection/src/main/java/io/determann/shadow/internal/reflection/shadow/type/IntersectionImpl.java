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

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import static io.determann.shadow.api.Operations.GENERIC_GET_BOUNDS;
import static io.determann.shadow.api.Operations.TYPE_REPRESENTS_SAME_TYPE;
import static io.determann.shadow.api.Provider.requestOrThrow;
import static io.determann.shadow.api.reflection.R_Adapter.IMPLEMENTATION;


public class IntersectionImpl implements R_Generic
{
   private final java.lang.reflect.Type[] bounds;

   public IntersectionImpl(java.lang.reflect.Type[] bounds)
   {
      this.bounds = bounds;
   }

   @Override
   public R_Type getBound()
   {
      return getBounds().getFirst();
   }

   @Override
   public List<R_Type> getBounds()
   {
      return Arrays.stream(bounds).map(R_Adapter::generalize).toList();
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
      return null;
   }

   @Override
   public boolean representsSameType(C_Type type)
   {
      //noinspection unchecked
      return type instanceof C_Generic generic && sameBounds(getBounds(), (List<C_Type>) requestOrThrow(generic, GENERIC_GET_BOUNDS));
   }

   private boolean sameBounds(List<R_Type> types, List<C_Type> types1)
   {
      if (types.size() != types1.size())
      {
         return false;
      }

      Iterator<R_Type> iterator = types.iterator();
      Iterator<C_Type> iterator1 = types1.iterator();
      while (iterator.hasNext() && iterator1.hasNext())
      {
         if (!requestOrThrow(iterator.next(), TYPE_REPRESENTS_SAME_TYPE, iterator1.next()))
         {
            return false;
         }
      }
      return true;
   }

   public java.lang.reflect.Type[] getReflection()
   {
      return bounds;
   }

   @Override
   public Implementation getImplementation()
   {
      return IMPLEMENTATION;
   }

   @Override
   public List<R_AnnotationUsage> getAnnotationUsages()
   {
      return List.of();
   }

   @Override
   public List<R_AnnotationUsage> getDirectAnnotationUsages()
   {
      return List.of();
   }

   @Override
   public String getName()
   {
      return "";
   }

   @Override
   public boolean equals(Object obj)
   {
      return GenericSupport.equals(this, obj);
   }

   @Override
   public int hashCode()
   {
      return GenericSupport.hashCode(this);
   }

   @Override
   public String toString()
   {
      return GenericSupport.toString(this);
   }
}
