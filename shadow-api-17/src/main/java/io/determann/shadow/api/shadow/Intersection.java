package io.determann.shadow.api.shadow;

import java.util.List;

/**
 * {@code T extends} <b> Collection &amp; Serializable</b>{@code >}
 */
public interface Intersection extends Shadow
{
   /**
    * {@code Collection & Serializable} -&gt;  {@code Collection & Serializable[]}
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
    * {@code T extends} <b> Collection &amp; Serializable</b>{@code >}
    */
   List<Shadow> getBounds();
}
