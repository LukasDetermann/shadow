package io.determann.shadow.impl.reflection.shadow;

import io.determann.shadow.api.TypeKind;
import io.determann.shadow.api.converter.Converter;
import io.determann.shadow.api.reflection.ReflectionAdapter;
import io.determann.shadow.api.shadow.Intersection;
import io.determann.shadow.api.shadow.Shadow;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;


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
      return Arrays.stream(bounds).map(ReflectionAdapter::getShadow).collect(collectingAndThen(toList(), Collections::unmodifiableList));
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
      if (!(other instanceof Intersection))
      {
         return false;
      }
      Intersection otherIntersection = ((Intersection) other);
      return Objects.equals(getBounds(), otherIntersection.getBounds());
   }

   public Type[] getReflection()
   {
      return bounds;
   }
}
