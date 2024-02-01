package io.determann.shadow.impl.test;


import io.determann.shadow.api.annotation_processing.test.ProcessorTest;

import javax.tools.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;

/**
 * The compiler wants to write java sources it compiles. This is not needed for {@link ProcessorTest}s.
 * Furthermore, tests should be isolated from each other. Removing some potential for coupling should help with that.
 */
class TestFileManager extends ForwardingJavaFileManager<JavaFileManager>
{
   TestFileManager(StandardJavaFileManager delegate)
   {
      super(delegate);
   }

   @Override
   public JavaFileObject getJavaFileForOutput(Location location, String className, JavaFileObject.Kind kind, FileObject sibling)
   {
      return new SimpleJavaFileObject(URI.create("test:///" + className.replace('.', '/') + kind.extension), kind)
      {
         private byte[] content;

         @Override
         public CharSequence getCharContent(boolean ignoreEncodingErrors)
         {
            return new String(content);
         }

         @Override
         public ByteArrayOutputStream openOutputStream()
         {
            return new ByteArrayOutputStream()
            {
               @Override
               public void close() throws IOException
               {
                  super.close();
                  content = toByteArray();
               }
            };
         }
      };
   }
}
