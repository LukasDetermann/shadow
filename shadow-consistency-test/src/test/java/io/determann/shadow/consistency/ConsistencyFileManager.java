package io.determann.shadow.consistency;

import javax.tools.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

class ConsistencyFileManager extends ForwardingJavaFileManager<JavaFileManager>
{
   private final Map<String, byte[]> files = new HashMap<>();

   ConsistencyFileManager(StandardJavaFileManager delegate) {super(delegate);}

   @Override
   public JavaFileObject getJavaFileForOutput(Location location, String className, JavaFileObject.Kind kind, FileObject sibling)
   {
      return new SimpleJavaFileObject(URI.create(className), kind)
      {
         @Override
         public OutputStream openOutputStream()
         {
            return new ByteArrayOutputStream()
            {
               @Override
               public void close() throws IOException
               {
                  super.close();
                  files.put(className, toByteArray());
               }
            };
         }
      };
   }

   public Map<String, byte[]> getCompiledClasses()
   {
      return files;
   }
}
