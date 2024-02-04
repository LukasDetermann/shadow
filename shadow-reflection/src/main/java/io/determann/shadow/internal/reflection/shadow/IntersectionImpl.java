package io.determann.shadow.internal.reflection.shadow;

import io.determann.shadow.api.TypeKind;
import io.determann.shadow.api.converter.Converter;
import io.determann.shadow.api.reflection.ReflectionAdapter;
import io.determann.shadow.api.shadow.Intersection;
import io.determann.shadow.api.shadow.Shadow;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;


public class IntersectionImpl implements Intersection
{
   private final Type[] bounds;

   public IntersectionImpl(Type[] bounds)
   {
      this.bounds = bounds;
   }

   @Override
   public List<Shadow> getBounds()
   {
      return Arrays.stream(bounds).map(ReflectionAdapter::getShadow).toList();
   }

   @Override
   public TypeKind getTypeKind()
   {
      return TypeKind.INTERSECTION;
   }

   @Override
   public boolean representsSameType(Shadow shadow)
   {
      return shadow != null &&
             Converter.convert(shadow)
                      .toIntersection()
                      .map(intersection -> sameBounds(getBounds(), intersection.getBounds()))
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
         if (!iterator.next().representsSameType(iterator1.next()))
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
      return Objects.equals(getBounds(), otherIntersection.getBounds());
   }

   public Type[] getReflection()
   {
      return bounds;
   }
}
