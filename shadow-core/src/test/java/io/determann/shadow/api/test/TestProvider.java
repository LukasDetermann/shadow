package io.determann.shadow.api.test;

import io.determann.shadow.api.query.Implementation;
import io.determann.shadow.api.query.ProviderSpi;
import io.determann.shadow.api.query.Response;
import io.determann.shadow.api.query.operation.Operation;

import java.util.ArrayList;
import java.util.List;

public class TestProvider
      implements ProviderSpi
{
   public static final Implementation IMPLEMENTATION = new Implementation("io.determann.shadow-test");
   private static final TestProvider INSTANCE = new TestProvider();

   public static void addValue(Object value)
   {
      INSTANCE.nextResponses.add(new Response.Result<>(value));
   }

   public static void addResult(Response.Result<?> result)
   {
      INSTANCE.nextResponses.add(result);
   }

   private final List<Response<?>> nextResponses = new ArrayList<>();

   public static void reset()
   {
      INSTANCE.nextResponses.clear();
   }

   public static ProviderSpi provider()
   {
      return INSTANCE;
   }

   public TestProvider()
   {
   }

   @Override
   public Implementation getImplementation()
   {
      return IMPLEMENTATION;
   }

   @Override
   public <RESULT> Response<RESULT> request(Object instance, Operation<RESULT> operation, Object... params)
   {
      //noinspection unchecked
      return (Response<RESULT>) nextResponses.removeFirst();
   }
}
