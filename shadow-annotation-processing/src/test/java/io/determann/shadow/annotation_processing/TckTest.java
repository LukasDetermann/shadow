package io.determann.shadow.annotation_processing;

import io.determann.shadow.api.annotation_processing.test.ProcessorTest;
import io.determann.shadow.api.query.Implementation;
import io.determann.shadow.tck.Source;
import io.determann.shadow.tck.Tck;

import java.util.Collection;
import java.util.function.Consumer;

public class TckTest implements Tck
{
   @Override
   public void test(Collection<Source> sources, Consumer<Implementation> implementationConsumer)
   {
      ProcessorTest processorTest = ProcessorTest.process(context -> implementationConsumer.accept(context.getImplementation()));

      for (Source source : sources)
      {
         processorTest = processorTest.withCodeToCompile(source.name(), source.content());
      }

      processorTest.compile();
   }
}