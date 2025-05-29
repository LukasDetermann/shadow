import io.determann.shadow.api.ProviderSpi;
import io.determann.shadow.api.test.TestProvider;

module io.determann.shadow.api.test {

   requires io.determann.shadow.api;
   requires transitive org.junit.jupiter.api;
   requires transitive org.junit.jupiter.engine;

   provides ProviderSpi with TestProvider;

   opens io.determann.shadow.api.test.dsl to org.junit.platform.commons;
}