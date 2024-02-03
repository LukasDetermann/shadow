package io.determann.shadow.internal.reflection.shadow;

import io.determann.shadow.api.TypeKind;
import io.determann.shadow.api.converter.Converter;
import io.determann.shadow.api.reflection.ReflectionAdapter;
import io.determann.shadow.api.shadow.Intersection;
import io.determann.shadow.api.shadow.Shadow;

import java.lang.reflect.Type;
import java.util.Arrays;
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
                      .map(intersection -> intersection.getBounds()
                                                       .stream()
                                                       .allMatch(shadow1 -> getBounds().stream()
                                                                                       .anyMatch(shadow1::representsSameType)))
                      .orElse(false);
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
