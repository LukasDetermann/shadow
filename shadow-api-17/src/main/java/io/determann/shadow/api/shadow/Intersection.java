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
    * {@code T extends} <b> Collection &amp; Serializable</b>{@code >}
    */
   List<Shadow> getBounds();
}
