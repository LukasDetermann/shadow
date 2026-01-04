package io.determann.shadow.annotation_processing;

import java.lang.reflect.Proxy;


public class TestFactory
{
   public static <T> T create(Class<T> toCreate)
   {
      return create(toCreate, "");
   }

   public static <T> T create(Class<T> toCreate, String rendering)
   {
      return create(toCreate, "render", rendering);
   }

   public static <T> T create(Class<T> toCreate, String methodName, String rendering)
   {
      //noinspection unchecked
      return (T) Proxy.newProxyInstance(TestFactory.class.getClassLoader(), new Class[]{toCreate}, (proxy, method, args) ->
      {
         if (methodName.equals(method.getName()))
         {
            return rendering;
         }
         throw new IllegalStateException("unhanded method '" + method.getName() + "'");
      });
   }
}
