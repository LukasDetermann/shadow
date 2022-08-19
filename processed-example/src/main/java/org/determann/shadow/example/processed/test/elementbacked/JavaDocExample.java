package org.determann.shadow.example.processed.test.elementbacked;

/**
 * Class level doc
 */
public class JavaDocExample</** a */T>
{
   /**
    * Field level doc
    */
   private Long id;

   /**
    * Constructor level doc
    */
   public JavaDocExample(/** */ Long id)
   {
   }

   /**
    * Method level doc
    */
   @Override
   public String toString()
   {
      return "JavaDocExample{}";
   }
}
