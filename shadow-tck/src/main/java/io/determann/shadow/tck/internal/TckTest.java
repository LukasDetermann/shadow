package io.determann.shadow.tck.internal;

import io.determann.shadow.api.query.Implementation;
import io.determann.shadow.tck.Source;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

import static io.determann.shadow.tck.Tck.TCK;

public class TckTest
{
   public static Builder withSource(String name, String content)
   {
      return new Builder(Collections.singletonList(new Source(name, content)));
   }

   public static void test(Consumer<Implementation> test)
   {
      TCK.test(Collections.emptyList(), test);
   }

   public static class Builder
   {
      private final List<Source> sources;

      public Builder(List<Source> sources) {this.sources = sources;}

      public Builder withSource(String name, String content)
      {
         List<Source> copy = new ArrayList<>(sources);
         copy.add(new Source(name, content));
         return new Builder(copy);
      }

      public void test(Consumer<Implementation> test)
      {
         TCK.test(sources, test);
      }
   }
}
