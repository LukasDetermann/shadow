package io.determann.shadow.api.shadow;

public abstract sealed class AbstractOperation<TYPE, RESULT> implements Operation<TYPE, RESULT> permits Operation0,
                                                                                                        Operation1
{
   private final String name;

   protected AbstractOperation(String name) {this.name = name;}

   public String getName()
   {
      return name;
   }

   @Override
   public String toString()
   {
      return "Operation{" +
             "name='" + name + '\'' +
             '}';
   }

   @Override
   public final boolean equals(Object o)
   {
      return super.equals(o);//object reference by design. makes collisions impossible
   }

   @Override
   public int hashCode()
   {
      return super.hashCode();
   }
}
