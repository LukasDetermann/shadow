package io.determann.shadow.internal.reflection.shadow.type;

import io.determann.shadow.api.reflection.ReflectionAdapter;
import io.determann.shadow.api.reflection.shadow.type.IntersectionReflection;
import io.determann.shadow.api.shadow.TypeKind;
import io.determann.shadow.api.shadow.type.Intersection;
import io.determann.shadow.api.shadow.type.Shadow;
import io.determann.shadow.implementation.support.api.shadow.type.IntersectionSupport;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import static io.determann.shadow.api.shadow.Operations.INTERSECTION_GET_BOUNDS;
import static io.determann.shadow.api.shadow.Operations.SHADOW_REPRESENTS_SAME_TYPE;
import static io.determann.shadow.api.shadow.Provider.requestOrThrow;
import static io.determann.shadow.internal.reflection.ReflectionProvider.IMPLEMENTATION_NAME;


public class IntersectionImpl implements IntersectionReflection
{
   private final java.lang.reflect.Type[] bounds;

   public IntersectionImpl(java.lang.reflect.Type[] bounds)
   {
      this.bounds = bounds;
   }

   @Override
   public List<Shadow> getBounds()
   {
      return Arrays.stream(bounds).map(ReflectionAdapter::generalize).toList();
   }

   @Override
   public TypeKind getKind()
   {
      return TypeKind.INTERSECTION;
   }

   @Override
   public boolean representsSameType(Shadow shadow)
   {
      return shadow instanceof Intersection intersection && sameBounds(getBounds(), requestOrThrow(intersection, INTERSECTION_GET_BOUNDS));
   }

   private boolean sameBounds(List<Shadow> shadows, List<Shadow> shadows1)
   {
      if (shadows.size() != shadows1.size())
      {
         return false;
      }

      Iterator<Shadow> iterator = shadows.iterator();
      Iterator<Shadow> iterator1 = shadows1.iterator();
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
