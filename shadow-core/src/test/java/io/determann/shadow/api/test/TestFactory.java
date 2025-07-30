package io.determann.shadow.api.test;

import java.lang.reflect.Proxy;

import static io.determann.shadow.api.test.TestProvider.IMPLEMENTATION;

public class TestFactory
{
   public static <T> T create(Class<T> toCreate)
   {
      return create(toCreate, "");
   }

   public static <T> T create(Class<T> toCreate, String redering)
   {
      //noinspection unchecked
      return (T) Proxy.newProxyInstance(TestFactory.class.getClassLoader(), new Class[]{toCreate}, (proxy, method, args) ->
      {
         if ("getImplementation".equals(method.getName()))
         {
            return IMPLEMENTATION;
         }
         if ("render".equals(method.getName()))
         {
            return redering;
         }
         throw new IllegalStateException();
      });
   }

   public static <T> T create(Class<T> toCreate,String methodName, String redering)
   {
      //noinspection unchecked
      return (T) Proxy.newProxyInstance(TestFactory.class.getClassLoader(), new Class[]{toCreate}, (proxy, method, args) ->
      {
         if ("getImplementation".equals(method.getName()))
         {
            return IMPLEMENTATION;
         }
         if (methodName.equals(method.getName()))
         {
            return redering;
         }
         throw new IllegalStateException();
      });
   }
}
