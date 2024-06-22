package io.determann.shadow.internal.reflection.shadow;

import io.determann.shadow.api.TypeKind;
import io.determann.shadow.api.converter.Converter;
import io.determann.shadow.api.reflection.ReflectionAdapter;
import io.determann.shadow.api.reflection.query.IntersectionReflection;
import io.determann.shadow.api.shadow.Intersection;
import io.determann.shadow.api.shadow.Shadow;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import static io.determann.shadow.internal.reflection.ReflectionProvider.IMPLEMENTATION_NAME;
import static io.determann.shadow.meta_meta.Operations.INTERSECTION_GET_BOUNDS;
import static io.determann.shadow.meta_meta.Operations.SHADOW_REPRESENTS_SAME_TYPE;
import static io.determann.shadow.meta_meta.Provider.request;
import static io.determann.shadow.meta_meta.Provider.requestOrThrow;


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
      return shadow != null &&
             Converter.convert(shadow)
                      .toIntersection()
                      .map(intersection -> sameBounds(getBounds(), requestOrThrow(intersection, INTERSECTION_GET_BOUNDS)))
                      .orElse(false);
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
      return Objects.hash(getBounds());
   }

   @Override
   public boolean equals(Object other)
   {
      if (other == this)
      {
         return true;
      }
      if (!(other instanceof Intersection otherIntersection))
      {
         return false;
      }
      return request(otherIntersection, INTERSECTION_GET_BOUNDS).map(shadow -> Objects.equals(shadow, getBounds())).orElse(false);
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
