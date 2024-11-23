package io.determann.shadow.tck;

import io.determann.shadow.api.Implementation;
import org.junit.platform.suite.api.IncludePackages;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;

import java.util.Collection;
import java.util.ServiceLoader;
import java.util.function.Consumer;

@Suite
@SelectPackages("io.determann.shadow.tck")
@IncludePackages("io.determann.shadow.tck")
public interface Tck
{
   public static Tck TCK = ServiceLoader.load(Tck.class)
                                        .findFirst()
                                        .orElseThrow(() -> new IllegalStateException("No " +
                                                                                     Tck.class.getName() +
                                                                                     " implementation found to test"));

   void test(Collection<Source> sources, Consumer<Implementation> implementationConsumer);
}
