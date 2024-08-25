package io.determann.shadow.internal.reflection.shadow.type;

import io.determann.shadow.api.reflection.R_Adapter;
import io.determann.shadow.api.reflection.shadow.type.R_Intersection;
import io.determann.shadow.api.reflection.shadow.type.R_Shadow;
import io.determann.shadow.api.shadow.type.C_Intersection;
import io.determann.shadow.api.shadow.type.C_Shadow;
import io.determann.shadow.implementation.support.api.shadow.type.IntersectionSupport;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import static io.determann.shadow.api.Operations.INTERSECTION_GET_BOUNDS;
import static io.determann.shadow.api.Operations.SHADOW_REPRESENTS_SAME_TYPE;
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
   public List<R_Shadow> getBounds()
   {
      return Arrays.stream(bounds).map(R_Adapter::generalize).toList();
   }

   @Override
   public boolean representsSameType(C_Shadow shadow)
   {
      //noinspection unchecked
      return shadow instanceof C_Intersection intersection && sameBounds(getBounds(),
                                                                         (List<C_Shadow>) requestOrThrow(intersection, INTERSECTION_GET_BOUNDS));
   }

   private boolean sameBounds(List<R_Shadow> shadows, List<C_Shadow> shadows1)
   {
      if (shadows.size() != shadows1.size())
      {
         return false;
      }

      Iterator<R_Shadow> iterator = shadows.iterator();
      Iterator<C_Shadow> iterator1 = shadows1.iterator();
      while (iterator.hasNext() && iterator1.hasNext())
      {
         if (!requestOrThrow(iterator.next(), SHADOW_REPRESENTS_SAME_TYPE, iterator1.next()))
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
