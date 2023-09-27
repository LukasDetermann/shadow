package io.determann.shadow.api.renderer;

public interface ArrayRenderer
{
   /**
    * {@code int[]}
    */
   String type();

   /**
    * {@code new int[5][2]}
    */
   String initialisation(int... dimensions);
}
