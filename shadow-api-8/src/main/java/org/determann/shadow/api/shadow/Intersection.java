package org.determann.shadow.api.shadow;

import org.jetbrains.annotations.UnmodifiableView;

import javax.lang.model.type.IntersectionType;
import javax.lang.model.type.TypeMirror;
import java.util.List;

/**
 * {@code T extends} <b> Collection & Serializable</b>{@code >}
 */
public interface Intersection extends Shadow<IntersectionType>
{
   /**
    *  Collection & Serializable -> Collection & Serializable[]
    */
   Array asArray();

   /**
    * be careful using this equals
    *
    * @see #representsSameType(Shadow)
    */
   @Override
   boolean equals(Object obj);

   /**
    * {@code T extends} <b> Collection & Serializable</b>{@code >}
    */
   @UnmodifiableView List<Shadow<TypeMirror>> getBounds();
}
