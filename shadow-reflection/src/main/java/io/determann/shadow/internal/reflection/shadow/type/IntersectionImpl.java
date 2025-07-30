package io.determann.shadow.internal.reflection.shadow.type;

import io.determann.shadow.api.C;
import io.determann.shadow.api.query.Implementation;
import io.determann.shadow.api.reflection.Adapter;
import io.determann.shadow.api.reflection.R;
import io.determann.shadow.implementation.support.api.shadow.type.GenericSupport;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import static io.determann.shadow.api.query.Operations.GENERIC_GET_BOUNDS;
import static io.determann.shadow.api.query.Operations.TYPE_REPRESENTS_SAME_TYPE;
import static io.determann.shadow.api.query.Provider.requestOrThrow;
import static io.determann.shadow.api.reflection.Adapter.IMPLEMENTATION;


public class IntersectionImpl implements R.Generic
{
   private final java.lang.reflect.Type[] bounds;

   public IntersectionImpl(java.lang.reflect.Type[] bounds)
   {
      this.bounds = bounds;
   }

   @Override
   public R.Type getBound()
   {
      return getBounds().getFirst();
   }

   @Override
   public List<R.Type> getBounds()
   {
      return Arrays.stream(bounds).map(Adapter::generalize).toList();
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
      return null;
   }

   @Override
   public boolean representsSameType(C.Type type)
   {
      //noinspection unchecked
      return type instanceof C.Generic generic && sameBounds(getBounds(), (List<C.Type>) requestOrThrow(generic, GENERIC_GET_BOUNDS));
   }

   private boolean sameBounds(List<R.Type> types, List<C.Type> types1)
   {
      if (types.size() != types1.size())
      {
         return false;
      }

      Iterator<R.Type> iterator = types.iterator();
      Iterator<C.Type> iterator1 = types1.iterator();
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
   public List<R.AnnotationUsage> getAnnotationUsages()
   {
      return List.of();
   }

   @Override
   public List<R.AnnotationUsage> getDirectAnnotationUsages()
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
