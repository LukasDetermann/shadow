package io.determann.shadow.impl.test;

import io.determann.shadow.api.test.CompilationTest;

import javax.tools.FileObject;
import javax.tools.JavaFileObject;
import javax.tools.SimpleJavaFileObject;
import javax.tools.StandardJavaFileManager;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.util.Iterator;
import java.util.Set;

/**
 * The compiler wants to write java sources it compiles. This is not needed for {@link CompilationTest}s.
 * Furthermore, tests should be isolated from each other. Removing some potential for coupling should help with that.
 */
class NonWritingFileManager implements StandardJavaFileManager
{
   private final StandardJavaFileManager delegate;

   NonWritingFileManager(StandardJavaFileManager delegate) {this.delegate = delegate;}

   @Override
   public ClassLoader getClassLoader(Location location)
   {
      return delegate.getClassLoader(location);
   }

   @Override
   public Iterable<JavaFileObject> list(Location location, String packageName, Set<JavaFileObject.Kind> kinds, boolean recurse)
         throws IOException
   {
      return delegate.list(location, packageName, kinds, recurse);
   }

   @Override
   public String inferBinaryName(Location location, JavaFileObject file)
   {
      return delegate.inferBinaryName(location, file);
   }

   @Override
   public boolean isSameFile(FileObject a, FileObject b)
   {
      return delegate.isSameFile(a, b);
   }

   @Override
   public boolean handleOption(String current, Iterator<String> remaining)
   {
      return delegate.handleOption(current, remaining);
   }

   @Override
   public boolean hasLocation(Location location)
   {
      return delegate.hasLocation(location);
   }

   @Override
   public JavaFileObject getJavaFileForInput(Location location, String className, JavaFileObject.Kind kind) throws IOException
   {
      return delegate.getJavaFileForInput(location, className, kind);
   }

   @Override
   public JavaFileObject getJavaFileForOutput(Location location, String className, JavaFileObject.Kind kind, FileObject sibling)
   {
      return new SimpleJavaFileObject(URI.create(className), kind)
      {
         @Override
         public OutputStream openOutputStream()
         {
            return new OutputStream()
            {
               @Override
               public void write(int b)
               {
                  //noop
               }
            };
         }
      };
   }

   @Override
   public FileObject getFileForInput(Location location, String packageName, String relativeName) throws IOException
   {
      return delegate.getFileForInput(location, packageName, relativeName);
   }

   @Override
   public FileObject getFileForOutput(Location location, String packageName, String relativeName, FileObject sibling) throws IOException
   {
      return new NonWritingFileObject(delegate.getFileForOutput(location, packageName, relativeName, sibling));
   }

   @Override
   public void flush() throws IOException
   {
      delegate.flush();
   }

   @Override
   public void close() throws IOException
   {
      delegate.close();
   }

   @Override
   public Iterable<? extends JavaFileObject> getJavaFileObjectsFromFiles(Iterable<? extends File> files)
   {
      return delegate.getJavaFileObjectsFromFiles(files);
   }

   @Override
   public Iterable<? extends JavaFileObject> getJavaFileObjects(File... files)
   {
      return delegate.getJavaFileObjects(files);
   }

   @Override
   public Iterable<? extends JavaFileObject> getJavaFileObjectsFromStrings(Iterable<String> names)
   {
      return delegate.getJavaFileObjectsFromStrings(names);
   }

   @Override
   public Iterable<? extends JavaFileObject> getJavaFileObjects(String... names)
   {
      return delegate.getJavaFileObjects(names);
   }

   @Override
   public void setLocation(Location location, Iterable<? extends File> files) throws IOException
   {
      delegate.setLocation(location, files);
   }

   @Override
   public Iterable<? extends File> getLocation(Location location)
   {
      return delegate.getLocation(location);
   }

   @Override
   public int isSupportedOption(String option)
   {
      return delegate.isSupportedOption(option);
   }
}
