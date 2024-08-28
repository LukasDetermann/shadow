package io.determann.shadow.internal.reflection.shadow.type;

import io.determann.shadow.api.reflection.R_Adapter;
import io.determann.shadow.api.reflection.shadow.type.R_Intersection;
import io.determann.shadow.api.reflection.shadow.type.R_Type;
import io.determann.shadow.api.shadow.type.C_Intersection;
import io.determann.shadow.api.shadow.type.C_Type;
import io.determann.shadow.implementation.support.api.shadow.type.IntersectionSupport;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import static io.determann.shadow.api.Operations.INTERSECTION_GET_BOUNDS;
import static io.determann.shadow.api.Operations.TYPE_REPRESENTS_SAME_TYPE;
import static io.determann.shadow.api.Provider.requestOrThrow;
import static io.determann.shadow.internal.reflection.ReflectionProvider.IMPLEMENTATION_NAME;


public class IntersectionImpl implements R_Intersection
{
   private final java.lang.reflect.Type[] bounds;

   public IntersectionImpl(java.lang.reflect.Type[] bounds)
   {
      this.bounds = bounds;
   }

   @Override
   public List<R_Type> getBounds()
   {
      return Arrays.stream(bounds).map(R_Adapter::generalize).toList();
   }

   @Override
   public boolean representsSameType(C_Type type)
   {
      //noinspection unchecked
      return type instanceof C_Intersection intersection && sameBounds(getBounds(),
                                                                         (List<C_Type>) requestOrThrow(intersection, INTERSECTION_GET_BOUNDS));
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

   @Override
   public int hashCode()
   {
      return IntersectionSupport.hashCode(this);
   }

   @Override
   public boolean equals(Object other)
   {
      return IntersectionSupport.equals(this, other);
   }

   @Override
   public String toString()
   {
      return IntersectionSupport.toString(this);
   }

   public java.lang.reflect.Type[] getReflection()
   {
      return bounds;
   }


   @Override
   public String getImplementationName()
   {
      return IMPLEMENTATION_NAME;
   }
}
