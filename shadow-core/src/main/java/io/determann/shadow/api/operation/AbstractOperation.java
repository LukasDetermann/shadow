package io.determann.shadow.api.operation;

public abstract sealed class AbstractOperation<RESULT> implements Operation<RESULT> permits InstanceOperation0,
                                                                                            InstanceOperation1,
                                                                                            InstanceOperation2,
                                                                                            StaticOperation0,
                                                                                            StaticOperation1,
                                                                                            StaticOperation2
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
