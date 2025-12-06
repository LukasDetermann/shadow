package io.determann.shadow.tck;

import io.determann.shadow.api.query.Implementation;
import org.junit.platform.suite.api.IncludePackages;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;

import java.util.Collection;
import java.util.function.Consumer;

import static java.util.ServiceLoader.load;

@Suite
@SelectPackages("io.determann.shadow.tck")
@IncludePackages("io.determann.shadow.tck")
public interface Tck
{
   Tck TCK = load(Tck.class)
         .findFirst()
         .orElseThrow(() -> new IllegalStateException("No " + Tck.class.getName() + " implementation found to test"));

   void test(Collection<Source> sources, Consumer<Implementation> implementationConsumer);
}
