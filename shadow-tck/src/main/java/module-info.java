module io.determann.shadow.tck {

   requires io.determann.shadow.api;

   requires org.junit.jupiter.api;
   requires org.junit.platform.suite.api;

   exports io.determann.shadow.tck;
   exports io.determann.shadow.tck.internal to documentation;

   uses io.determann.shadow.tck.Tck;
}